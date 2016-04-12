package controllers.front;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import models.Order;
import service.OrderService;
import service.PayService;
import service.wx.WXPay;
import service.wx.common.Configure;
import service.wx.common.RandomStringGenerator;
import service.wx.common.Signature;
import service.wx.dto.unifiedOrder.UnifiedOrderReqDto;
import service.wx.dto.unifiedOrder.UnifiedOrderRspDto;
import common.constants.MessageCode;
import common.core.FrontController;
import exception.BusinessException;


public class Pay extends FrontController {

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
    	String jsRequestBody = PayService.wxPay(order);
    	BigDecimal b = new BigDecimal(order.getTotalFee()/100D);  
		double totalFee = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
    	render("/Front/Pay/wxPay.html", jsRequestBody, totalFee, order);
    }
    
    /**
     * 前台回调
     */
    public static void frontCallBack() {
    	//TODO 2016-04-12 23:00 做到这里
    	//把merchant_trade_order去掉，将字段搬到order表，还没删与merchant_trade_order有关的表与代码
    	//order的MODEL也还没改，整个创建订单到支付的流程重新梳理一下 
    }
}
