package controllers.front;

import models.CashInfo;
import models.Order;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import service.CashInfoService;
import service.OrderService;
import service.OutTradeNo;
import service.RedPackService;
import service.UserService;
import service.wx.WXPay;
import service.wx.dto.order.OrderQueryReqDto;
import service.wx.dto.order.OrderQueryRspDto;
import service.wx.dto.redpack.QueryRedpackReqDto;
import service.wx.dto.redpack.QueryRedpackRspDto;
import service.wx.dto.redpack.SendRedpackRspDto;
import service.wx.service.user.WxUserService;
import utils.IdGenerator;
import utils.NumberUtil;

import com.google.gson.Gson;
import common.constants.OrderStatus;
import common.constants.PayType;
import common.constants.wx.PayStatus;
import common.constants.wx.TradeStatus;
import common.constants.wx.WxCallbackStatus;
import common.core.FrontController;

import exception.BusinessException;


public class Demo extends FrontController {
	
	public static Gson gson = new Gson();
	public static void test() {
		String jsRequestBody = "";
		double totalFee = 0.01;
		Order order = OrderService.get(1462009398678000000L);
		int payFail = PayStatus.PAY_FAIL.getStatus();
		int paySucc = PayStatus.PAY_SUCC.getStatus();
		int payCancel = PayStatus.PAY_CANCEL.getStatus();
		render("/Front/Pay/wxPay.html", jsRequestBody, totalFee, order, payFail, paySucc, payCancel);
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
    	//TODO 使用微信支付，预支付订单有效期为2小时
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
    
    /**
     * 查询订单状态
     * @throws BusinessException
     */
    public static void queryOrderStatus() throws BusinessException {
    	long id = 1462009398678000000L;
    	Order order = OrderService.get(id);
    	if(StringUtils.isBlank(order.getPlatformTransationId())) {
    		renderText("订单商户交易单号为空, 订单详情为: %s", gson.toJson(order));
    	}
    	
    	OrderQueryReqDto req = new OrderQueryReqDto(order.getPlatformTransationId(), order.getOutTradeNo());
    	OrderQueryRspDto rsp = WXPay.requestOrderQueryService(req);
    	if(null == rsp) {
    		renderText("向微信查询订单状态返回数据为空, 订单详情为: %s", gson.toJson(order));
    	}
    	if(rsp.getTrade_state().equals(TradeStatus.SUCCESS.getStatus())) {
    		renderText("订单状态为成功, 订单详情为: %s，查询状态数据为: %s", gson.toJson(order), gson.toJson(rsp));
    	} 
		TradeStatus trade = TradeStatus.getPayStatus(rsp.getTrade_state());
		if(null == trade) {
			renderText("获取订单status[%s]对应的状态失败", rsp.getTrade_state());
		}
		renderText("订单状态为%s, 订单详情为: %s，查询状态数据为: %s", trade.getDesc(), 
				gson.toJson(order), gson.toJson(rsp ));
    }
    
    public static void sendRedPack() throws BusinessException {
    	long cashId = 1L;
    	Logger.info("发送现金红包记录, cashId: %d", cashId);
    	SendRedpackRspDto rsp = RedPackService.sendRedPack(cashId);
    	//1.若return_code为FAIL，则通信失败
    	//2.若return_code为SUCCESS，则有
    	//	1) result_code为SUCCESS，则发送成功
    	//	2) result_code为FAIL，则err_code_des为出错信息
    	renderText(gson.toJson(rsp));
    }
    
    public static void queryRedPack() throws BusinessException {
    	long cashId = 1L;
    	Logger.info("查询提现记录, cashId: %d", cashId);
    	CashInfo ci = CashInfoService.get(cashId);
    	if(null == ci) {
    		Logger.error("查询提现记录失败，cashId: %d", cashId);
    		renderText("查询提现记录失败，cashId: %d", cashId);
    	}
    	QueryRedpackRspDto rsp = RedPackService.queryRedPack(cashId);
    	//1.若return_code为FAIL，则通信失败
    	//2.若return_code为SUCCESS，则有
    	//	1) result_code为SUCCESS，则status代表红包状态(如RECEIVED)，对应的enum为RedPackStatus
    	//	2) result_code为FAIL，则err_code_des为出错信息
    	renderText(gson.toJson(rsp));
    }
}
