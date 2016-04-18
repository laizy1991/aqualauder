package controllers.front;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import models.Order;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import play.Logger;
import service.OrderService;
import service.PayService;
import service.wx.common.Signature;
import service.wx.common.Util;
import service.wx.dto.unifiedOrder.UnifiedOrderCallbackDto;

import com.google.gson.Gson;

import common.constants.GlobalConstants;
import common.constants.MessageCode;
import common.constants.wx.OutTradeStatus;
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
		
    	render("/Front/Pay/wxPay.html", jsRequestBody, totalFee, order);
    }
    
    /**
     * 前台回调
     */
    public static void payCallBack(long id, int payStatus) {
    	if(id <= 0) {
    		Logger.error("微信统一下单后前台传来的订单编号为空");
    		return;
    	}
    	if(payStatus <= 0 || (payStatus != OutTradeStatus.PAY_FAILED && payStatus != OutTradeStatus.PAY_SUCC)) {
    		Logger.error("微信统一下单后前台传来的订单支付状态有误，payStatus[%d]", payStatus);
    		return;
    	}
    	Order order = OrderService.get(id);
    	if(null == order) {
    		Logger.error("获取到的订单为空，id[%d]", id);
    	}
    	if(order.getPayStatus() >= OutTradeStatus.PAY_SUCC) {
    		Logger.info("该订单之前已支付成功，id[%d]", id);
    		return;
    	}
    	
    	//更新订单前台回调时间
    	order.setPayTime(System.currentTimeMillis());
    	order.setPayStatus(payStatus);
    	if(!OrderService.update(order)) {
    		Logger.error("更新订单前台回调时间时失败，id[%d]", id);
    	}
    	Logger.info("更新订单前台回调时间时成功，id[%d]", id);
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
				return;
			}
			Logger.info("微信回调返回的参数为：%s", backStr);
			if (!Signature.checkIsSignValidFromResponseString(backStr.toString())) {
				Logger.error("统一下单API返回的数据签名验证失败，有可能数据被篡改了");
			}
			rsp = (UnifiedOrderCallbackDto)Util.getObjectFromXMLWithXStream(backStr.toString(), UnifiedOrderCallbackDto.class);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
    	
    	if(null == rsp) {
    		Logger.error("解析微信回调返回参时失败");
    		return;
    	}
    	Logger.info("微信回调经解析为对象后的结果为：%s", gson.toJson(rsp));
    	String outTradeNo = rsp.getOut_trade_no();
    	if(StringUtils.isEmpty(outTradeNo)) {
    		Logger.error("微信回调返回的outTradeNo为空");
    	}
    	
    	//查看订单状态
    	Order order = OrderService.getOrderByOutTradeNo(outTradeNo);
    	if(null == order) {
    		Logger.error("微信回调获取到的订单为空，outTradeNo[%s]", outTradeNo);
    		return;
    	}
    	
    	if(order.getPayStatus() == OutTradeStatus.CALLBACK_SUCC) {
    		Logger.info("微信订单之前已回调成功，不再进行更新，outTradeNo[%s], callbackTime[%s], 订单参数: %s", 
    				outTradeNo, GlobalConstants.fullTimeSdf.format(new Date(order.getCallbackTime())), gson.toJson(order));
    		return;
    	}
    	
    	long nowTime = System.currentTimeMillis();
    	//查看返回的参数
    	if(!rsp.getResult_code().equals("SUCCESS")) {
    		Logger.error("微信回调返回的结果错误");
    		order.setPayStatus(OutTradeStatus.CALLBACK_FAILED);
    		order.setPlatformTradeMsg(rsp.getReturn_msg());
    	} else {
    		Logger.error("微信回调返回的结果正确");
    		order.setPayStatus(OutTradeStatus.CALLBACK_SUCC);
    		order.setPlatformTradeMsg("回调成功");
    	}
    	order.setCallbackTime(nowTime);
    	order.setUpdateTime(nowTime);
    	//开始更新订单表
    	if(!OrderService.update(order)) {
    		Logger.error("更新微信回调结果失败，order参数为：%s", gson.toJson(order));
    		return;
    	}
    	Logger.info("更新微信回调结果成功，order参数为：%s", gson.toJson(order));
    }
}
