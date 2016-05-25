package controllers.front;

import javax.persistence.Query;

import models.CashInfo;
import models.Distributor;
import models.Order;
import models.RefundOrder;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import play.db.jpa.Model;
import service.CashInfoService;
import service.DistributorService;
import service.OrderService;
import service.OutTradeNo;
import service.RedPackService;
import service.RefundOrderService;
import service.UserService;
import service.wx.WXPay;
import service.wx.dto.jspai.JsapiConfig;
import service.wx.dto.order.OrderQueryReqDto;
import service.wx.dto.order.OrderQueryRspDto;
import service.wx.dto.qrcode.CreateQrCodeRspDto;
import service.wx.dto.qrcode.limit.CreateLimitQrCodeReqDto;
import service.wx.dto.qrcode.tmp.CreateTmpQrCodeReqDto;
import service.wx.dto.redpack.QueryRedpackRspDto;
import service.wx.dto.redpack.SendRedpackRspDto;
import service.wx.dto.refund.QueryRefundReqDto;
import service.wx.dto.refund.QueryRefundRspDto;
import service.wx.dto.refund.SendRefundReqDto;
import service.wx.dto.refund.SendRefundRspDto;
import service.wx.service.jsapi.JsApiService;
import service.wx.service.user.WxUserService;
import utils.IdGenerator;
import utils.NumberUtil;
import utils.WxAccessTokenUtil;

import com.google.gson.Gson;

import common.constants.OrderStatus;
import common.constants.PayType;
import common.constants.RefundStatus;
import common.constants.wx.PayStatus;
import common.constants.wx.TradeStatus;
import common.constants.wx.WxCallbackStatus;
import common.constants.wx.WxRefundStatus;
import common.core.FrontController;
import dto.DistributorDetail;
import dto.MySpaceDto;
import exception.BusinessException;


public class Demo extends FrontController {
	
	public static Gson gson = new Gson();
	
	public static void getAT() {
		String accessToken = WxAccessTokenUtil.getAccessToken();
		Logger.info("accessToken: %s", accessToken);
    }
	
	/**
	 * 公众号自定义菜单进入后获取用户信息
	 * @param code
	 */
    public static void getUserInfo(String code) {
    	if(StringUtils.isBlank(code)) {
    	    Logger.error("code为空");
    	    renderText("非法请求");
    	}
    	String openId = WxUserService.getUserOpenIdByCode(code);
    	if(StringUtils.isBlank(openId)) {
    	    Logger.error("openId为空");
    	    renderText("非法请求");
    	}
    	User user = WxUserService.getUserInfo(openId);
    	if(null == user) {
    		Logger.error("获取用户信息失败, openId: %s", openId);
    		renderText("非法请求");
    	}
        
        DistributorDetail detail = DistributorService.distributorDetail(user.getUserId());
        
        MySpaceDto data = new MySpaceDto();
        data.setUser(user);
        data.setDetail(detail);
        render("/Front/user/myspace.html", data);
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
    
    /**
     * 发送现金红包
     * @throws BusinessException
     */
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
    
    /**
     * 查询现金红包状态
     * @throws BusinessException
     */
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
    
    /**
     * 查询退款状态
     * @throws BusinessException
     */
    public static void queryRefundOrder() throws BusinessException {
    	long id = 1L;
    	RefundOrder ro = RefundOrderService.get(id);
    	if(null == ro) {
    		Logger.error("查询退款记录失败，refundId: %d", id);
    		renderText("查询退款记录失败，refundId: %d", id);
    	}
    	if(ro.getRefundState() == RefundStatus.SUCCESS.getCode() ||
    			ro.getRefundState() == RefundStatus.REFUSE.getCode() ||
    			ro.getRefundState() == RefundStatus.CANCEL.getCode() ||
				ro.getRefundState() == RefundStatus.NOTREFUND.getCode()) {
    		Logger.info("该笔订单状态为%s, 禁止退款，refundId: %d", RefundStatus.resolveType(ro.getRefundState()).getDesc(),
    				id);
    		renderText("该笔订单状态为%s, 禁止退款，refundId: %d", RefundStatus.resolveType(ro.getRefundState()).getDesc(),
    				id);
    	}
    	QueryRefundReqDto req = new QueryRefundReqDto(ro.getTransactionId());
    	QueryRefundRspDto rsp = WXPay.queryRefundServcie(req);
    	if(null != rsp && rsp.getReturn_code().equals("SUCCESS") && rsp.getResult_code().equals("SUCCESS")) {
    		Logger.info("开始更新退款记录状态，refundId: %s", id);
    		String updateSql = "UPDATE `refund_order` SET refund_state=?,update_time=?,refund_recv_accout=? WHERE id=?";
        	Query query = Model.em().createNativeQuery(updateSql);
        	query.setParameter(1, WxRefundStatus.getRefundStatus(rsp.getRefund_status_0()).getStatus());
        	query.setParameter(2, System.currentTimeMillis());
        	query.setParameter(3, rsp.getRefund_recv_accout_0());
        	query.setParameter(4, id);
        	if(query.executeUpdate() > 0) {
        		Logger.info("更新退款订单状态和时间成功，id: %d", id);
        		renderText("更新退款订单状态和时间成功，id: %d", id);
        	} else {
        		Logger.error("更新退款订单状态和时间失败，id: %d", id);
        		renderText("更新退款订单状态和时间失败，id: %d", id);
        	}
    	} else {
    		renderText(gson.toJson(rsp));
    	}
    }
    
    /**
     * 发送退款请求
     * @throws BusinessException
     */
    public static void sendRefundOrder() throws BusinessException {
    	long id = 1L;
    	RefundOrder ro = RefundOrderService.get(id);
    	if(null == ro) {
    		Logger.error("查询退款记录失败，refundId: %d", id);
    		renderText("查询退款记录失败，refundId: %d", id);
    	}
    	if(ro.getRefundState() == RefundStatus.ING.getCode() ||
    			ro.getRefundState() == RefundStatus.SUCCESS.getCode() ||
    			ro.getRefundState() == RefundStatus.REFUSE.getCode() ||
    			ro.getRefundState() == RefundStatus.CANCEL.getCode() ||
				ro.getRefundState() == RefundStatus.NOTREFUND.getCode()) {
    		Logger.info("该笔订单状态为%s, 禁止退款，refundId: %d", RefundStatus.resolveType(ro.getRefundState()).getDesc(),
    				id);
    		renderText("该笔订单状态为%s, 禁止退款，refundId: %d", RefundStatus.resolveType(ro.getRefundState()).getDesc(),
    				id);
    	}
    	Order order = OrderService.get(ro.getOrderId());
    	if(null == order) {
    		Logger.error("查询订单记录失败，orderId: %d", ro.getOrderId());
    		renderText("查询订单记录失败，orderId: %d", ro.getOrderId());
    	}
    	SendRefundReqDto req = new SendRefundReqDto(ro.getTransactionId(), ro.getOutTradeNo(), 
    				order.getTotalFee(), order.getTotalFee(), String.valueOf(order.getUserId()));
    	SendRefundRspDto rsp = WXPay.sendRefundServcie(req);
    	if(null != rsp && rsp.getReturn_code().equals("SUCCESS") && rsp.getResult_code().equals("SUCCESS")) {
    		Logger.info("开始更新退款记录，refundId: %s", id);
    		String updateSql = "UPDATE `refund_order` SET refund_id=?,update_time=? WHERE id=?";
        	Query query = Model.em().createNativeQuery(updateSql);
        	query.setParameter(1, rsp.getRefund_id());
        	query.setParameter(2, System.currentTimeMillis());
        	query.setParameter(3, id);
        	if(query.executeUpdate() > 0) {
        		Logger.info("更新退款订单支付平台退款单号和时间成功，refundId: %d", id);
        		renderText("更新退款订单支付平台退款单号和时间成功，refundId: %d", id);
        	} else {
        		Logger.error("更新退款订单支付平台退款单号和时间失败，refundId: %d", id);
        		renderText("更新退款订单支付平台退款单号和时间失败，refundId: %d", id);
        	}
    	} else {
    		renderText(gson.toJson(rsp));
    	}
    }
    
    /**
     * 生成临时二维码，CreateTmpQrCodeReqDto中的scene_id为用户ID
     * @throws BusinessException
     */
    public static void createTmpQrCode(int userId) throws BusinessException {
    	User user = UserService.get(userId);
    	if(null == user) {
    		Logger.error("查询用户记录失败，userId: %d", userId);
    		renderText("查询用户记录失败，userId: %d", userId);
    	}
    	
    	CreateTmpQrCodeReqDto req = new CreateTmpQrCodeReqDto(user.getUserId());
    	CreateQrCodeRspDto rsp = WXPay.CreateTmpQrCodeService(req);
    	if(null == rsp || rsp.isSuccess() == false) {
    		Logger.error("获取用户二维码图片失败，userId: %d", userId);
    		renderText("获取用户二维码图片失败，userId: %d", userId);
    	}
    	Distributor dis = DistributorService.get(userId);
    	long nowTime = System.currentTimeMillis();
    	//这里expire_seconds一定要先转为long
    	long expireTime = nowTime + Long.parseLong(rsp.getExpire_seconds()+"") * 1000;
    	//TODO 前台展示二维码时，用如下规则，nginx对qrcode作解析
    	//Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn") + Play.configuration.getProperty("wx.qrcode.prefix", "/qrcode/")+ rsp.getPicRelPath();
    	if(null == dis) {
    		Logger.info("该用户较前时间未成为分销商，准备新增，userId: %d", userId);
    		//将二维码地址和本地相对路径入库
        	dis = new Distributor();
        	dis.setUserId(userId);
        	dis.setType(0);
        	dis.setStatus(1);
        	dis.setRealName(user.getNickname());
        	dis.setJoinTime(System.currentTimeMillis());
        	dis.setLink("");
        	dis.setQrcodeTmpWxUrl(rsp.getUrl());
        	dis.setQrcodeTmpPath(rsp.getPicRelPath());
        	dis.setQrcodeTmpTicket(rsp.getTicket());
        	dis.setQrcodeTmpExpireTime(expireTime);
        	dis.setQrcodeLimitWxUrl("");
        	dis.setQrcodeLimitPath("");
        	dis.setQrcodeLimitTicket("");
        	dis.setCreateTime(nowTime);
        	dis.setUpdateTime(nowTime);
        	if(DistributorService.add(dis)) {
        		renderText("创建用户推广记录成功，入参为: %s", gson.toJson(dis));
        	}
        	renderText("创建用户推广记录失败，入参为: %s", gson.toJson(dis));
    	} else {
    		Logger.info("该用户较前时间已成为分销商，准备更新，userId: %d", userId);
    		dis.setQrcodeTmpWxUrl(rsp.getUrl());
    		dis.setQrcodeTmpPath(rsp.getPicRelPath());
    		dis.setQrcodeTmpTicket(rsp.getTicket());
    		dis.setQrcodeTmpExpireTime(expireTime);
    		dis.setUpdateTime(nowTime);
    		if(DistributorService.update(dis)) {
        		renderText("更新用户推广记录成功，入参为: %s", gson.toJson(dis));
        	}
        	renderText("更新用户推广记录失败，入参为: %s", gson.toJson(dis));
    	}
    }
    
    /**
     * 生成永久二维码，CreateLimitQrCodeReqDto中的scene_str为用户ID
     * @throws BusinessException
     */
    public static void createLimitQrCode(int userId) throws BusinessException {
    	User user = UserService.get(userId);
    	if(null == user) {
    		Logger.error("查询用户记录失败，userId: %d", userId);
    		renderText("查询用户记录失败，userId: %d", userId);
    	}
    	
    	CreateLimitQrCodeReqDto req = new CreateLimitQrCodeReqDto(user.getOpenId());
    	CreateQrCodeRspDto rsp = WXPay.CreateLimitQrCodeService(req);
    	if(null == rsp || rsp.isSuccess() == false) {
    		Logger.error("获取用户二维码图片失败，userId: %d", userId);
    		renderText("获取用户二维码图片失败，userId: %d", userId);
    	}
    	
    	Distributor dis = DistributorService.get(userId);
    	long nowTime = System.currentTimeMillis();
    	//TODO 前台展示二维码时，用如下规则，nginx对qrcode作解析
    	//Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn") + Play.configuration.getProperty("wx.qrcode.prefix", "/qrcode/")+ rsp.getPicRelPath();
    	if(null == dis) {
    		Logger.info("该用户较前时间未成为分销商，准备新增，userId: %d", userId);
    		//将二维码地址和本地相对路径入库
        	dis = new Distributor();
        	dis.setUserId(userId);
        	dis.setType(0);
        	dis.setStatus(1);
        	dis.setRealName(user.getNickname());
        	dis.setJoinTime(System.currentTimeMillis());
        	dis.setLink("");
        	dis.setQrcodeTmpWxUrl("");
        	dis.setQrcodeTmpPath("");
        	dis.setQrcodeTmpTicket("");
        	dis.setQrcodeTmpExpireTime(0L);
        	dis.setQrcodeLimitWxUrl(rsp.getUrl());
        	dis.setQrcodeLimitPath(rsp.getPicRelPath());
        	dis.setQrcodeLimitTicket(rsp.getTicket());
        	dis.setCreateTime(nowTime);
        	dis.setUpdateTime(nowTime);
        	if(DistributorService.add(dis)) {
        		renderText("创建用户推广记录成功，入参为: %s", gson.toJson(dis));
        	}
        	renderText("创建用户推广记录失败，入参为: %s", gson.toJson(dis));
    	} else {
    		Logger.info("该用户较前时间已成为分销商，准备更新，userId: %d", userId);
    		dis.setQrcodeLimitWxUrl(rsp.getUrl());
    		dis.setQrcodeLimitPath(rsp.getPicRelPath());
    		dis.setQrcodeLimitTicket(rsp.getTicket());
    		dis.setUpdateTime(nowTime);
    		if(DistributorService.update(dis)) {
        		renderText("更新用户推广记录成功，入参为: %s", gson.toJson(dis));
        	}
        	renderText("更新用户推广记录失败，入参为: %s", gson.toJson(dis));
    	}
    }
    
    public static void share(int userId) {
    	String querystring = request.querystring;
    	String protocol = request.secure?"https://":"http://";
    	String action = request.action.replace(".", "/");
    	String url =  protocol + request.domain +"/"+ action + "?" + querystring;
    	Logger.info("生成的分享链接为: %s", url);
    	JsapiConfig config = JsApiService.getSign(url);
    	Logger.info("config参数为: %s", gson.toJson(config));
    	render("Front/Demo/share.html", config, userId);
    }
    
    public static void qrcode() {
    	render("Front/Demo/qrcode.html");
    }
    
    public static void companyQrcode() {
    	render("Front/user/companyQrcode.html");
    }
    public static void redit() {
    	redirect("/front/Demo/aaa?userId=333333");
    }
    public static void aaa(String userId) {
    	renderText("AAAAAAAAAA___userId="+userId);
    }
}
