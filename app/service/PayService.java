package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.MerchantTradeOrder;
import models.Order;
import models.OrderGoods;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Play;
import service.wx.WXPay;
import service.wx.common.Configure;
import service.wx.common.RandomStringGenerator;
import service.wx.common.Signature;
import service.wx.dto.unifiedOrder.UnifiedOrderReqDto;
import service.wx.dto.unifiedOrder.UnifiedOrderRspDto;

import common.constants.MessageCode;
import common.constants.wx.OutTradeStatus;
import common.constants.wx.OutTradeType;
import common.constants.wx.PayType;

import dao.OrderGoodsDao;
import exception.BusinessException;

public class PayService {
	/**
	 * 订单使用微信支付
	 */
	public static String wxPay(Order order) throws BusinessException{
		if(null == order) {
			throw new BusinessException(MessageCode.ORDER_NULL_ERROR.getMsg());
		}
		//获取用户信息
		User user = UserService.get(order.getUserId());
		if(null == user || StringUtils.isBlank(user.getOpenId())) {
			throw new BusinessException(MessageCode.GET_USER_FAILED.getMsg());
		}
		List<OrderGoods> goods = OrderGoodsDao.getByOrder(order.getId());
		String subject = "";
		if(null != goods && goods.size() > 0) {
			for(int i=0; i<goods.size(); i++) {
				if(0 != i) {
					subject += "_";
				}
				subject += goods.get(i).getGoodsTitle();
			}
		} else {
			subject = "服装商品";
		}
		// TODO 记得改callbackUrl(不能带参数)，改交易金额
		String callbackUrl = Play.configuration.getProperty("local.host.domain");;
		//准备插入微信商户订单表
		long createTime = System.currentTimeMillis(); 
		MerchantTradeOrder tradeOrder = new MerchantTradeOrder();
		tradeOrder.setOutTradeNo(order.getOutTradeNo());
		tradeOrder.setOutTradeType(OutTradeType.WXPAY.getType());
		tradeOrder.setUserId(order.getUserId());
		tradeOrder.setTotalFee(order.getTotalFee());
		tradeOrder.setSubject(subject);
		tradeOrder.setCallbackUrl(callbackUrl);
		tradeOrder.setStatus(OutTradeStatus.ADDED);
		tradeOrder.setTradeMsg("");
		tradeOrder.setTradeNo("");
		tradeOrder.setOpenid(user.getOpenId());
		tradeOrder.setCreateTime(createTime);
		tradeOrder.setPayTime(null);
		tradeOrder.setCallbackTime(null);
		if(!MerchantTradeOrderService.add(tradeOrder)) {
			throw new BusinessException(MessageCode.ADD_MECHANT_TRADE_ORDER_FAILED.getMsg());
		}
		// TODO 获取客户端地址，在controller那里，写入order表
		String clientIp = "";
		UnifiedOrderReqDto req = new UnifiedOrderReqDto("WEB", subject, order.getOutTradeNo(), order.getTotalFee(),
				clientIp, callbackUrl, PayType.JS.getType(), user.getOpenId());
		
		UnifiedOrderRspDto rsp  = WXPay.requestUnifiedOrderService(req);
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
}
