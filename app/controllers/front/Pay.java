package controllers.front;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Query;

import models.CashInfo;
import models.Order;
import models.RefundOrder;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Model;
import service.CashInfoService;
import service.OrderService;
import service.PayService;
import service.RefundOrderService;
import service.UserService;
import service.wx.WXPay;
import service.wx.common.Configure;
import service.wx.common.Signature;
import service.wx.common.Util;
import service.wx.dto.order.UnifiedOrderCallbackDto;
import service.wx.dto.redpack.QueryRedpackReqDto;
import service.wx.dto.redpack.QueryRedpackRspDto;
import service.wx.dto.redpack.SendRedpackReqDto;
import service.wx.dto.redpack.SendRedpackRspDto;
import service.wx.dto.refund.SendRefundReqDto;
import service.wx.dto.refund.SendRefundRspDto;
import utils.DateUtil;

import com.google.gson.Gson;

import common.constants.GlobalConstants;
import common.constants.MessageCode;
import common.constants.OrderStatus;
import common.constants.Separator;
import common.constants.wx.PayStatus;
import common.constants.wx.WxCallbackStatus;
import common.core.FrontController;
import exception.BusinessException;


public class Pay extends FrontController {
	
	public static Gson gson = new Gson();

	/**
	 * 使用微信支付
	 * @param orderId
	 * @throws BusinessException
	 */
    public static void wxPay(long orderId) throws BusinessException{
    	if(orderId <= 0) {
    		throw new BusinessException(MessageCode.ORDER_ID_INVALID.getMsg());
    	}
    	Order order = OrderService.get(orderId);
    	//订单检验
    	if(null == order) {
    		throw new BusinessException(MessageCode.ORDER_NULL_ERROR.getMsg());
    	}
    	if(StringUtils.isBlank(order.getMobilePhone())) {
    		throw new BusinessException(MessageCode.ORDER_SHIPPING_ADDRESS_EMPTY.getMsg());
    	}
    	if(StringUtils.isBlank(order.getShippingAddress())) {
    		throw new BusinessException(MessageCode.ORDER_SHIPPING_ADDRESS_EMPTY.getMsg());
    	}
    	String jsRequestBody = PayService.wxPay(orderId);
    	BigDecimal b = new BigDecimal(order.getTotalFee()/100D);  
		double totalFee = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		int payFail = PayStatus.PAY_FAIL.getStatus();
		int paySucc = PayStatus.PAY_SUCC.getStatus();
		int payCancel = PayStatus.PAY_CANCEL.getStatus();
    	render("/Front/Pay/wxPay.html", jsRequestBody, totalFee, order, payFail, paySucc, payCancel);
    }
    
    /**
     * 前台回调，订单id用String类型是因为从js传过来时最后一位会被截断
     */
    public static void payCallBack(String idStr, int payStatus) {
    	if(StringUtils.isBlank(idStr)) {
    		Logger.error("微信统一下单后前台传来的订单id[%s]为空", idStr);
    		return;
    	}
    	long id = 0;
    	try {
    		id = Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			id = 0;
		}
    	if(id <= 0) {
    		Logger.error("微信统一下单后前台传来的订单id[%d]不正确", id);
    		return;
    	}
    	if(payStatus <= PayStatus.PAY_READY.getStatus()) {
    		Logger.error("微信统一下单后前台传来的订单支付状态payStatus[%d]有误", payStatus);
    		return;
    	}
    	Order order = OrderService.get(id);
    	if(null == order) {
    		Logger.error("通过订单id[%d]获取到的订单为空", id);
    		return;
    	}
    	
    	//更新订单前台回调时间
    	String updateSql = "UPDATE `order` SET pay_status=?,pay_time=?,update_time=? WHERE id=?";
    	Query query = Model.em().createNativeQuery(updateSql);
    	query.setParameter(1, payStatus);
    	query.setParameter(2, System.currentTimeMillis());
    	query.setParameter(3, System.currentTimeMillis());
    	query.setParameter(4, id);
    	if(query.executeUpdate() > 0) {
    		Logger.info("更新订单前台回调状态和时间成功，id[%d]", id);
    	} else {
    		Logger.error("更新订单前台回调状态和时间失败，id[%d]", id);
    	}
    }
    
    /**
     * 微信回调
     */
    public static void wxCallback() {
    	InputStream in = request.body;
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	String args = "";
    	StringBuffer backStr = new StringBuffer();
    	UnifiedOrderCallbackDto rsp = null;
    	try {
			while((args=reader.readLine()) != null){
				backStr.append(args);
			}
			if(StringUtils.isBlank(backStr.toString())) {
				Logger.error("微信回调返回的参数为空");
				renderXml("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数为空]]></return_msg></xml>");
			}
			Logger.info("微信回调返回的参数为：%s", backStr);
			if (!Signature.checkIsSignValidFromResponseString(backStr.toString())) {
				Logger.error("统一下单API返回的数据签名验证失败，有可能数据被篡改了");
				renderXml("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名失败]]></return_msg></xml>");
			}
			rsp = (UnifiedOrderCallbackDto)Util.getObjectFromXMLWithXStream(backStr.toString(), UnifiedOrderCallbackDto.class);
		} catch (Exception e) {
			e.printStackTrace();
			rsp = null;
		}
    	if(null == rsp) {
    		Logger.error("解析微信回调返回参时失败");
    		renderXml("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数格式校验错误]]></return_msg></xml>");
    	}
    	Logger.info("微信回调经解析为对象后的结果为：%s", gson.toJson(rsp));
    	String outTradeNo = rsp.getOut_trade_no();
    	if(StringUtils.isEmpty(outTradeNo)) {
    		Logger.error("微信回调返回的outTradeNo为空");
    		renderXml("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[商户系统的订单号为空]]></return_msg></xml>");
    	}
    	
    	//查看订单状态
    	Order order = OrderService.getOrderByOutTradeNo(outTradeNo);
    	if(null == order) {
    		Logger.error("微信回调获取到的订单为空，outTradeNo[%s]", outTradeNo);
    		return;
    	}
    	
    	if(order.getCallbackStatus() == WxCallbackStatus.CALLBACK_SUCC.getStatus()) {
    		Logger.info("微信订单之前已回调成功，不再进行更新，outTradeNo[%s], callbackTime[%s], 订单参数: %s", 
    				outTradeNo, GlobalConstants.fullTimeSdf.format(new Date(order.getCallbackTime())), gson.toJson(order));
    		renderXml("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
    	}
    	
    	long nowTime = System.currentTimeMillis();
    	//查看返回的参数
    	if(rsp.getReturn_code().equals("SUCCESS") && rsp.getResult_code().equals("SUCCESS")) {
    		Logger.info("微信回调返回的结果正确");
    		String updateSql = "UPDATE `order` SET callback_status=?,callback_time=?,platform_trade_msg=?," +
    				"platform_transation_id=?,state=?,state_history=?,update_time=? WHERE id=?";
    		Query query = Model.em().createNativeQuery(updateSql);
    		query.setParameter(1, WxCallbackStatus.CALLBACK_SUCC.getStatus());
    		query.setParameter(2, nowTime);
    		query.setParameter(3, "回调成功");
    		query.setParameter(4, rsp.getTransaction_id());
    		query.setParameter(5, OrderStatus.PAYED.getState());
    		String dbHis = StringUtils.isBlank(order.getStateHistory()) ? "" : order.getStateHistory();
    		dbHis += OrderStatus.DELIVERED.getState() + Separator.COMMON_SEPERATOR_BL
    				+ DateUtil.getDateString(System.currentTimeMillis(), "yyyyMMddHHmmss")
    				+ Separator.COMMON_SEPERATOR_COMME;
    		query.setParameter(6, dbHis);
    		query.setParameter(7, nowTime);
    		query.setParameter(8, order.getId());
            if(query.executeUpdate() > 0) {
            	Logger.info("微信回调成功，更新回调结果成功，orderId：%d", order.getId());
            	renderXml("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
            } else {
            	Logger.error("微信回调成功，更新回调结果失败，orderId：%d", order.getId());
            }
    	} else {
    		Logger.error("微信回调返回的结果错误");
    		String updateSql = "UPDATE `order` SET callback_status=?,callback_time=?,platform_trade_msg=?,update_time=? WHERE id=?";
    		Query query = Model.em().createNativeQuery(updateSql);
    		query.setParameter(1, WxCallbackStatus.CALLBACK_FAIL.getStatus());
    		query.setParameter(2, nowTime);
    		query.setParameter(3, rsp.getErr_code_des());
    		query.setParameter(4, nowTime);
    		query.setParameter(5, order.getId());
    		if(query.executeUpdate() > 0) {
            	Logger.info("微信回调失败，更新回调结果成功，orderId：%d", order.getId());
            } else {
            	Logger.error("微信回调失败，更新回调结果失败，orderId：%d", order.getId());
            }
    	}
    	renderXml("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[回调失败]]></return_msg></xml>");
    }
    
    public static boolean sendRedPack(long cashId) {
    	if(cashId <= 0) {
    		Logger.error("发送现金红包入参不正确，cashId: %d", cashId);
    		return false;
    	}
    	CashInfo ci = CashInfoService.get(cashId);
    	if(null == ci) {
    		Logger.error("获取提现的记录为空，cashId: %d", cashId);
    		return false;
    	}
    	if(ci.getCashStatus() == 2) {
    		//已提现成功
    		Logger.info("该订单之前已提现成功，cashId: %d", cashId);
    		return false;
    	}
    	String mchBillno = ci.getMchBillno();
    	User user = UserService.get(ci.getUserId());
    	if(null == user || StringUtils.isEmpty(user.getOpenId())) {
    		Logger.error("发送微信红包时无法获取用户Openid，cashId: %d", cashId);
    		return false;
    	}
    	String openid = user.getOpenId();
    	int totalAmount = ci.getAmount();
    	SendRedpackReqDto sendRedpackReqDto = new SendRedpackReqDto(mchBillno, openid, totalAmount);
    	SendRedpackRspDto rsp = null;
		try {
			rsp = WXPay.sendRedpackService(sendRedpackReqDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null == rsp) {
			return false;
		}
		
		return true;
    }
    
    public static void queryRedPack(long cashId) {
    	if(cashId <= 0) {
    		Logger.error("发送现金红包入参不正确，cashId: %d", cashId);
    		renderText("发送现金红包入参不正确，cashId: %d", cashId);
    	}
    	CashInfo ci = CashInfoService.get(cashId);
    	if(null == ci) {
    		Logger.error("获取提现的记录为空，cashId: %d", cashId);
    		renderText("获取提现的记录为空，cashId: %d", cashId);
    	}
    	if(ci.getCashStatus() == 2) {
    		//已提现成功
    		Logger.info("该订单之前已提现成功，cashId: %d", cashId);
    		renderText("该订单之前已提现成功，cashId: %d", cashId);
    	}
    	String mchBillno = ci.getMchBillno();
    	String bill_type = "MCHT";
    	QueryRedpackReqDto queryRedpackReqDto = new QueryRedpackReqDto(mchBillno, bill_type);
    	QueryRedpackRspDto rsp = null;
		try {
			rsp = WXPay.queryRedpackStatusService(queryRedpackReqDto);
			renderText(gson.toJson(rsp));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void sendRefund(long refundId) {
    	if(refundId <= 0) {
    		Logger.error("请求退款入参不正确，refundId: %d", refundId);
    		return;
    	}
    	RefundOrder refundOrder = RefundOrderService.get(refundId);
    	if(null == refundOrder) {
    		Logger.error("获取退款记录为空，refundId: %d, orderId: %d", refundId, refundOrder.getOrderId());
    		return;
    	}
    	if(null == refundOrder.getOrderId() || refundOrder.getOrderId() <= 0) {
    		Logger.error("从退款记录中获取订单ID不正确, orderId: %d", refundOrder.getOrderId());
    		return;
    	}
    	Order order = OrderService.get(refundOrder.getOrderId());
    	if(null == order) {
    		Logger.error("获取订单记录记录为空，orderId: %d", refundOrder.getOrderId());
    		return;
    	}
    	SendRefundReqDto sendRefundReqDto = new SendRefundReqDto(order.getOutTradeNo(), ""+refundOrder.getId(), 
    			order.getTotalFee(), order.getTotalFee(), Configure.getMchid());
    	SendRefundRspDto rsp = null;
		try {
			rsp = WXPay.sendRefundServcie(sendRefundReqDto);
			renderText(gson.toJson(rsp));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
