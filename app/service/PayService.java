package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.CashInfo;
import models.Order;
import models.OrderGoods;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import service.wx.WXPay;
import service.wx.common.Configure;
import service.wx.common.RandomStringGenerator;
import service.wx.common.Signature;
import service.wx.dto.order.UnifiedOrderReqDto;
import service.wx.dto.order.UnifiedOrderRspDto;
import service.wx.dto.redpack.QueryRedpackReqDto;
import service.wx.dto.redpack.QueryRedpackRspDto;
import service.wx.dto.redpack.SendRedpackReqDto;
import service.wx.dto.redpack.SendRedpackRspDto;

import com.google.gson.Gson;

import common.constants.BillType;
import common.constants.CashStatus;
import common.constants.MessageCode;
import common.constants.OrderStatus;
import common.constants.PayType;
import common.constants.wx.PayStatus;
import common.constants.wx.WxCallbackStatus;
import common.constants.wx.PayMode;
import dao.OrderDao;
import dao.OrderGoodsDao;
import exception.BusinessException;

public class PayService {
	public static Gson gson = new Gson();
	/**
	 * 订单使用微信支付
	 */
	public static String wxPay(long orderId) throws BusinessException{
		if(orderId <= 0) {
			throw new BusinessException(MessageCode.ORDER_ID_INVALID.getMsg());
		}
		//获取订单信息
		Order order = OrderService.get(orderId);
		if(null == order) {
			throw new BusinessException(MessageCode.ORDER_NULL_ERROR.getMsg());
		}
		//获取用户信息
		User user = UserService.get(order.getUserId());
		if(null == user || StringUtils.isBlank(user.getOpenId())) {
			throw new BusinessException(MessageCode.GET_USER_FAILED.getMsg());
		}
		
		// TODO 这里是否要写死，商量后再确定
		String subject = Play.configuration.getProperty("wx.redpack.sendName") + "服装商品";
		String callbackUrl = Play.configuration.getProperty("local.host.domain") + 
				Play.configuration.getProperty("wx.pay.callback.path");
				
		String clientIp = order.getClientIp();
		if(StringUtils.isBlank(clientIp) || clientIp.equals("127.0.0.1")) {
			clientIp = Play.configuration.getProperty("local.host.ip");
		}
		UnifiedOrderReqDto req = new UnifiedOrderReqDto("WEB", subject, order.getOutTradeNo(), order.getTotalFee(),
				clientIp, callbackUrl, PayMode.JS.getType(), user.getOpenId());
		
		UnifiedOrderRspDto rsp  = WXPay.requestUnifiedOrderService(req);
		if(null != rsp && rsp.getResult_code().equals("SUCCESS")) {
			//对订单表进行更新
			order.setCallbackUrl(callbackUrl);
			order.setPayStatus(PayStatus.PAY_READY.getStatus());
			order.setPayType(PayType.WX.getCode());
			order.setPlatformTradeNo(rsp.getPrepay_id());
			order.setPlatformTradeMsg("");
			order.setUpdateTime(System.currentTimeMillis());
			if(OrderDao.update(order)) {
				Logger.info("微信统一下单后更新订单表成功，订单参数为：%s", gson.toJson(order));
			}
		}
		//准备生成签名
		Map<String, Object> signMap = new HashMap<String,Object>();
		String timeStamp = "" + System.currentTimeMillis()/1000;
		String nonceStr = RandomStringGenerator.getRandomStringByLength(32);
		signMap.put("appId", Configure.getAppid());
		signMap.put("timeStamp", "" + timeStamp);
		signMap.put("nonceStr", nonceStr);
		signMap.put("package", "prepay_id="+rsp.getPrepay_id());
		signMap.put("signType", "MD5");
		String paySign = "";
		paySign = Signature.getSign(signMap);
		
		//准备将参数赋值给前台页面
		String jsRequestBody = "{appId:'" + Configure.getAppid() + "',"; 
		jsRequestBody += "timeStamp:'" + timeStamp + "',"; 
		jsRequestBody += "nonceStr:'" + nonceStr + "',"; 
		jsRequestBody += "package:'prepay_id=" + rsp.getPrepay_id() + "',"; 
		jsRequestBody += "signType:'MD5',"; 
		jsRequestBody += "paySign:'" + paySign + "'}"; 
		
		return jsRequestBody;
	}

	/**
	 * 余额支付
	 * @param orderId
	 */
	public static boolean balancePay(long orderId) throws BusinessException{
	    if(orderId <= 0) {
            throw new BusinessException(MessageCode.ORDER_ID_INVALID.getMsg());
        }
        //获取订单信息
        Order order = OrderService.get(orderId);
        if(null == order) {
            throw new BusinessException(MessageCode.ORDER_NULL_ERROR.getMsg());
        }
        //获取用户信息
        User user = UserService.get(order.getUserId());
        if(null == user || StringUtils.isBlank(user.getOpenId())) {
            throw new BusinessException(MessageCode.GET_USER_FAILED.getMsg());
        }
        
        boolean isSucc = UserWalletService.spend(user.getUserId(), order.getTotalFee(), order.getOutTradeNo(),
                BillType.PAY, user.getUserId(), user.getUserId());
        
        if(isSucc) {
            order.setPayTime(System.currentTimeMillis());
            order.setPayStatus(PayStatus.PAY_SUCC.getStatus());
            order.setPayType(PayType.BALANCE.getCode());
            order.setState(OrderStatus.DELIVERING.getState());
            return OrderService.setStatusAndUpdate(order, OrderStatus.DELIVERING);
        }
        
        return isSucc;
	}
	
}
