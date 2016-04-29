package controllers.front;

import models.Order;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import service.OrderService;
import service.OutTradeNo;
import service.UserService;
import service.wx.service.user.WxUserService;
import utils.IdGenerator;
import utils.NumberUtil;

import com.google.gson.Gson;
import common.constants.OrderStatus;
import common.constants.PayType;
import common.constants.wx.PayStatus;
import common.constants.wx.WxCallbackStatus;
import common.core.FrontController;

import exception.BusinessException;


public class Demo extends FrontController {
	
	public static Gson gson = new Gson();
	public static void test() {
		renderXml("<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数格式校验错误]]></return_msg></xml>");
	}
	
    public static void getUserInfo(String code) {
    	if(StringUtils.isBlank(code)) {
    		renderText("code为空");
    	}
    	String openId = WxUserService.getUserOpenIdByCode(code);
    	if(StringUtils.isBlank(openId)) {
    		renderText("获取到的openId为空");
    	}
    	User user = WxUserService.getUserInfo(openId);
    	if(null == user) {
    		renderText("获取用户信息失败, openId: %s", openId);
    	}
    	renderText(gson.toJson(user));
    	session.put("openId", openId);
    }
    
    /**
     * 统一下单
     */
    public static void unifiedOrder(String code) throws BusinessException {
    	String openId = session.get("openId");
    	if(StringUtils.isEmpty(openId)) {
    		Logger.info("从session中获取用户openId为空，通过code[%s]向微信换取", code);
    		openId = WxUserService.getUserOpenIdByCode(code);
    	}
    	User user = UserService.getByOpenId(openId);
    	String outTradeNo = OutTradeNo.getOutTradeNo();
    	Order order = new Order();
    	order.setId(IdGenerator.getId());
    	order.setUserId(user.getUserId());
    	order.setOutTradeNo(outTradeNo);
    	order.setPayType(PayType.WX.getCode());
    	order.setExpressName("中国邮政");
    	order.setExpressNum(NumberUtil.getRandomNumberString(10));
    	order.setTotalFee(1);
    	order.setState(OrderStatus.INIT.getState());
    	order.setForbidRefund(0);
    	order.setOrderMemo("快发货");
    	order.setReceiver("林守煌");
    	order.setMobilePhone("15989164056");
    	order.setShippingAddress("广东省广州市天河区车陂路");
    	order.setCreateTime(System.currentTimeMillis());
    	order.setUpdateTime(System.currentTimeMillis());
    	order.setOpenId(user.getOpenId());
    	order.setClientIp(request.remoteAddress.equals("127.0.0.1")?Play.configuration.getProperty("local.host.ip"):request.remoteAddress);
    	order.setPayStatus(PayStatus.INIT.getStatus());
    	order.setCallbackStatus(WxCallbackStatus.INIT.getStatus());
    	order.setCallbackUrl(Play.configuration.getProperty("local.host.domain")+Play.configuration.getProperty("wx.pay.callback.path"));
    	
    	boolean addFlag = OrderService.add(order);
    	if(!addFlag) {
    		renderText("新增订单出错了");
    	}
    	order = OrderService.getOrderByOutTradeNo(outTradeNo);
		Pay.wxPay(order.getId());
    }
}
