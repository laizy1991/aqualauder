package service;

import java.math.BigDecimal;
import java.util.List;

import models.CashInfo;
import models.Order;
import models.RefundOrder;
import models.User;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import service.wx.service.msg.WxTplMsgServcie;
import utils.DateUtil;
import common.constants.CashStatus;
import common.constants.CashType;
import common.constants.OrderStatus;
import common.constants.RefundStatus;
import common.constants.wx.WxCallbackStatus;
import dto.OrderDetail;
import dto.OrderDetail.OrderGoodsInfo;
import dto.WxMsgRspDto;

public class WxMsgService {
	private static String orderNotPayTplId = "b5c5mL51nYHwHoQpxkCpNfOOxXLQChsaGcXQzzfersI"; //订单未支付
	private static String goodsBoughtTplId = "OBF7J8Nqo8E92lz25ZUfzvA77BtjVbru7nUTJRJ6hY8"; //购买成功
	private static String goodsDeliveredTplId = "ZqEYpGZMjP6iNVpEYebApogq-ISHElqqZ6Osg3zCU-M"; //已发货
	private static String refundMoneyTplId = "EUzz6nAllMgLDQlw20KMpNApUeVahJRuHw993kKJ46o"; //退款结果
	private static String withdrawMoneyTplId = "mGp6nX75oO1Wp_QvY42SmQn1xtIO-rwq_JTYSs5yd9g"; //提现结果
	
	private static Order checkOrder(long orderId) {
		if(orderId <= 0) {
			return null;
		}
		
		return OrderService.get(orderId);
	}
	
	/**
	 * 发送订单未支付通知
	 * @param orderId
	 * @return
	 */
	public static WxMsgRspDto sendNotPayResultMsg(long orderId) {
		WxMsgRspDto rsp = new WxMsgRspDto();
		rsp.setSuccess(false);
		rsp.setMsg("");
		
		Order order = checkOrder(orderId); 
		if(null == order) {
			rsp.setMsg("发送催付通知失败, 无法获取订单");
			return rsp;
		}
		//这里回调失败当作未支付状态，让用户与客户联系
		if(null != order.getCallbackStatus() && order.getCallbackStatus() == WxCallbackStatus.CALLBACK_SUCC.getStatus()) {
			rsp.setMsg("发送催付通知失败, 该订单已成功支付");
			return rsp;
		}
		
		String openId = order.getOpenId();
		if(StringUtils.isEmpty(openId)) {
			User user = UserService.get(order.getUserId());
			if(null == user) {
				rsp.setMsg("发送催付通知失败, 无法获取下单用户openId");
				return rsp;
			}
			openId = user.getOpenId();
		}
		
		//获取商品详情
		OrderDetail od = OrderService.getOrderDetail(orderId);
		if(null == od) {
			rsp.setMsg("发送催付通知失败, 无法获取商品详情");
			return rsp;
		}
		//包装请求body
		JSONObject json = new JSONObject();
		json.put("touser", openId);
		json.put("template_id", orderNotPayTplId);
		json.put("url", "");
		
		//{{first.DATA}}
		//下单时间：{{ordertape.DATA}}
		//订单号：{{ordeID.DATA}}
		//{{remark.DATA}}
		JSONObject first = new JSONObject();
		first.put("value","亲，您好！您有一笔订单未支付");
		first.put("color", "#000000");
		
		JSONObject ordertape = new JSONObject();
		ordertape.put("value", DateUtil.getDateString(order.getCreateTime()));
		ordertape.put("color", "#000000");
		
		JSONObject ordeID = new JSONObject();
		ordeID.put("value", String.valueOf(order.getId()));
		ordeID.put("color", "#000000");
		
		JSONObject remark = new JSONObject();
		remark.put("value", "请及时付款，感谢您的关注!");
		remark.put("color", "#000000");
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("first", first);
		dataJson.put("ordertape", ordertape);
		dataJson.put("ordeID", ordeID);
		dataJson.put("remark", remark);
		json.put("data", dataJson);
		
		return WxTplMsgServcie.sendWxTplMsg(json);
	}
	
	/**
	 * 发送购买成功通知
	 * @param orderId
	 */
	public static WxMsgRspDto sendBuyResultMsg(long orderId) {
		WxMsgRspDto rsp = new WxMsgRspDto();
		rsp.setSuccess(false);
		rsp.setMsg("");
		
		Order order = checkOrder(orderId); 
		if(null == order) {
			rsp.setMsg("发送购买成功通知失败, 无法获取订单");
			return rsp;
		}
		String openId = order.getOpenId();
		if(StringUtils.isEmpty(openId)) {
			User user = UserService.get(order.getUserId());
			if(null == user) {
				rsp.setMsg("发送购买成功通知失败, 无法获取下单用户openId");
				return rsp;
			}
			openId = user.getOpenId();
		}
		
		//获取商品详情
		OrderDetail od = OrderService.getOrderDetail(orderId);
		if(null == od) {
			rsp.setMsg("发送购买成功通知失败, 无法获取商品详情");
			return rsp;
		}
		String goodTitle = "服装商品";
		List<OrderGoodsInfo> goodsInfo = od.getGoodsInfo();
		if(null != goodsInfo && goodsInfo.size()>0) {
			goodTitle = goodsInfo.get(0).getGoodsName();
		}
		//包装请求body
		JSONObject json = new JSONObject();
		json.put("touser", openId);
		json.put("template_id", goodsBoughtTplId);
		json.put("url", "");
//		json.put("url", Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn") 
//				+ "/front/Users/order?orderId="+orderId);
		
		//商品信息：{{name.DATA}}
		//{{remark.DATA}}
		JSONObject name = new JSONObject();
		name.put("value", goodTitle);
		name.put("color", "#000000");
		
		JSONObject remark = new JSONObject();
		remark.put("value", "订单号为"+String.valueOf(order.getId())+",欢迎再次购买!");
		remark.put("color", "#000000");
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("name", name);
		dataJson.put("remark", remark);
		json.put("data", dataJson);
		
		return WxTplMsgServcie.sendWxTplMsg(json);
	}
	
	/**
	 * 发送已发货通知
	 * @param orderId
	 */
	public static WxMsgRspDto sendDeliveredResultMsg(long orderId) {
		WxMsgRspDto rsp = new WxMsgRspDto();
		rsp.setSuccess(false);
		rsp.setMsg("");
		
		Order order = checkOrder(orderId); 
		if(null == order) {
			rsp.setMsg("发送已发货通知失败, 无法获取订单");
			return rsp;
		}
		if(order.getState() != OrderStatus.DELIVERED.getState()) {
			rsp.setMsg("发送已发货通知失败, 订单非已发货状态");
			return rsp;
		}
		if(StringUtils.isBlank(order.getExpressName()) || StringUtils.isBlank(order.getExpressNum())) {
			rsp.setMsg("发送已发货通知失败, 快递公司或单号为空");
			return rsp;
		}
		String openId = order.getOpenId();
		if(StringUtils.isEmpty(openId)) {
			User user = UserService.get(order.getUserId());
			if(null == user) {
				rsp.setMsg("发送已发货通知失败, 无法获取下单用户openId");
				return rsp;
			}
			openId = user.getOpenId();
		}
		
		//获取商品详情
		OrderDetail od = OrderService.getOrderDetail(orderId);
		if(null == od) {
			rsp.setMsg("发送已发货通知失败, 无法获取商品详情");
			return rsp;
		}
		String goodTitle = "服装商品";
		List<OrderGoodsInfo> goodsInfo = od.getGoodsInfo();
		if(null != goodsInfo && goodsInfo.size()>0) {
			goodTitle = goodsInfo.get(0).getGoodsName();
		}
		//包装请求body
		JSONObject json = new JSONObject();
		json.put("touser", openId);
		json.put("template_id", goodsDeliveredTplId);
		json.put("url", "");
//		json.put("url", Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn") 
//				+ "/front/Users/order?orderId="+orderId);
		
		//{{first.DATA}} 
		//快递公司：{{delivername.DATA}}
		//快递单号：{{ordername.DATA}}
		//{{remark.DATA}}  
		JSONObject first = new JSONObject();
		first.put("value","亲，您的\""+goodTitle+"\"已经发货了");
		first.put("color", "#000000");
		
		JSONObject delivername = new JSONObject();
		delivername.put("value", order.getExpressName());
		delivername.put("color", "#000000");
		
		JSONObject ordername = new JSONObject();
		ordername.put("value", order.getExpressNum());
		ordername.put("color", "#000000");
		
		JSONObject remark = new JSONObject();
		remark.put("value", "订单号为"+String.valueOf(order.getId())+",欢迎再次购买!");
		remark.put("color", "#000000");
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("first", first);
		dataJson.put("delivername", delivername);
		dataJson.put("ordername", ordername);
		dataJson.put("remark", remark);
		
		json.put("data", dataJson);
		
		return WxTplMsgServcie.sendWxTplMsg(json);
	}
	
	/**
	 * 退款结果通知
	 * @param orderId
	 * @param refundCode 对应于common.constants.RefundStatus
	 */
	public static WxMsgRspDto refundMoneyResultMsg(long refundId) {
		WxMsgRspDto rsp = new WxMsgRspDto();
		rsp.setSuccess(false);
		rsp.setMsg("");

		RefundOrder ro = RefundOrderService.get(refundId);
		if(null == ro) {
			rsp.setMsg("发送退款结果通知失败, 无法获取退款单");
			return rsp;
		}
		
		if(StringUtils.isBlank(ro.getSellerMemo())) {
			rsp.setMsg("发送退款结果通知失败, 商家退款备注必须填写");
			return rsp;
		}
		
		Order order = OrderService.get(ro.getOrderId());
		if(null == order) {
			rsp.setMsg("发送退款结果通知失败, 无法获取订单");
			return rsp;
		}
		
		RefundStatus rs = RefundStatus.resolveType(ro.getRefundState());
		if(null == rs) {
			rsp.setMsg("发送退款结果通知失败, 无法获取退款状态");
			return rsp;
		}
		
		if(!(ro.getRefundState() == RefundStatus.SUCCESS.getCode() || 
				ro.getRefundState() == RefundStatus.FAIL.getCode())) {
			rsp.setMsg("发送退款结果通知失败, 发送通知的退款单状态只能为"+RefundStatus.SUCCESS.getDesc()+
					"或"+RefundStatus.FAIL.getDesc());
			return rsp;
		}

		String openId = order.getOpenId();
		if(StringUtils.isEmpty(openId)) {
			User user = UserService.get(order.getUserId());
			if(null == user) {
				rsp.setMsg("发送退款结果失败, 无法获取下单用户openId");
				return rsp;
			}
			openId = user.getOpenId();
		}
		
		//包装请求body
		JSONObject json = new JSONObject();
		json.put("touser", openId);
		json.put("template_id", refundMoneyTplId);
		json.put("url", "");
		
		//{{first.DATA}}
		//退款状态：{{keyword1.DATA}}
		//退款金额：{{keyword2.DATA}}
		//审核说明：{{keyword3.DATA}}
		//{{remark.DATA}}
		JSONObject first = new JSONObject();
		first.put("value","您好，您的退款申请处理结果如下");
		first.put("color", "#000000");
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", rs.getDesc());
		keyword1.put("color", "#000000");
		
		BigDecimal b = new BigDecimal(order.getTotalFee()/100D);  
		double fee = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", fee +"元");
		keyword2.put("color", "#000000");
		
		JSONObject keyword3 = new JSONObject();
		keyword3.put("value", ro.getSellerMemo());
		keyword3.put("color", "#000000");
		
		JSONObject remark = new JSONObject();
		remark.put("value", "有任何疑问，请致电客服。");
		remark.put("color", "#000000");
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("first", first);
		dataJson.put("keyword1", keyword1);
		dataJson.put("keyword2", keyword2);
		dataJson.put("keyword3", keyword3);
		dataJson.put("remark", remark);
		
		json.put("data", dataJson);
		
		return WxTplMsgServcie.sendWxTplMsg(json);
	}
	
	/**
	 * 提现结果通知
	 * @param orderId
	 */
	public static WxMsgRspDto withdrawMoneyResultMsg(long cashInfoId) {
		WxMsgRspDto rsp = new WxMsgRspDto();
		rsp.setSuccess(false);
		rsp.setMsg("");
		
		CashInfo ci = CashInfoService.get(cashInfoId);
		if(null == ci) {
			rsp.setMsg("发送提现结果通知失败, 无法获取提现单");
			return rsp;
		}
		
		CashStatus cs = CashStatus.resolveType(ci.getCashStatus());
		if(null == cs) {
			rsp.setMsg("发送提现结果通知失败, 无法获取提现状态");
			return rsp;
		}
		CashType ct = CashType.resolveType(ci.getCashType());
		if(null == ct) {
			rsp.setMsg("发送提现结果通知失败, 无法获取提现方式");
			return rsp;
		}
		
		User user = UserService.get(ci.getUserId());
		if(null == user || StringUtils.isBlank(user.getOpenId())) {
			rsp.setMsg("发送退款结果失败, 无法获取提现单用户openId");
			return rsp;
		}
		String openId = user.getOpenId();
		
		//包装请求body
		JSONObject json = new JSONObject();
		json.put("touser", openId);
		json.put("template_id", withdrawMoneyTplId);
		json.put("url", "");
		
		//{{first.DATA}}
		//提现金额：{{keyword1.DATA}}
		//提现方式：{{keyword2.DATA}}
		//申请时间：{{keyword3.DATA}}
		//审核结果：{{keyword4.DATA}}
		//审核时间：{{keyword5.DATA}}
		//{{remark.DATA}}
		JSONObject first = new JSONObject();
		first.put("value","您好，您的提现申请处理结果如下");
		first.put("color", "#000000");
		
		BigDecimal b = new BigDecimal(ci.getAmount()/100D);  
		double fee = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", fee+"元");
		keyword1.put("color", "#000000");

		
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", ct.getDesc());
		keyword2.put("color", "#000000");
		
		JSONObject keyword3 = new JSONObject();
		keyword3.put("value", DateUtil.getOnlyDateFormat(ci.getCreateTime()));
		keyword3.put("color", "#000000");
		
		
		JSONObject keyword4 = new JSONObject();
		keyword4.put("value", cs.getDesc());
		keyword4.put("color", "#000000");
		
		JSONObject keyword5 = new JSONObject();
		keyword5.put("value", DateUtil.getOnlyDateFormat(ci.getUpdateTime()));
		keyword5.put("color", "#000000");
		
		JSONObject remark = new JSONObject();
		remark.put("value", "有任何疑问，请致电客服。");
		remark.put("color", "#000000");
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("first", first);
		dataJson.put("keyword1", keyword1);
		dataJson.put("keyword2", keyword2);
		dataJson.put("keyword3", keyword3);
		dataJson.put("keyword4", keyword4);
		dataJson.put("keyword5", keyword5);
		dataJson.put("remark", remark);
		
		json.put("data", dataJson);
		
		return WxTplMsgServcie.sendWxTplMsg(json);
	}
}
