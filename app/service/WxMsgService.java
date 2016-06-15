package service;

import java.util.List;

import models.Order;
import models.RefundOrder;
import models.User;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Play;
import service.wx.service.msg.WxTplMsgServcie;
import common.constants.OrderStatus;
import common.constants.RefundStatus;
import common.constants.wx.WxCallbackStatus;
import dto.OrderDetail;
import dto.OrderDetail.OrderGoodsInfo;
import dto.WxMsgRspDto;

public class WxMsgService {
	private static String goodsBoughtTplId = "OBF7J8Nqo8E92lz25ZUfzvA77BtjVbru7nUTJRJ6hY8"; //购买成功
	private static String goodsDeliveredTplId = "ZqEYpGZMjP6iNVpEYebApogq-ISHElqqZ6Osg3zCU-M"; //已发货
	private static String refundMoneyTplId = "CPHMUWtbIJFNsIwtmkzmXYs-LoEDWv1mVPoJvq8teUY"; //退款结果
	private static String withdrawMoneyTplId = "zpdgRzxoOppFrfePDDNpgVii-gsBW4djaV-FvRhCPd4"; //提现结果
	
	private static Order checkOrder(long orderId) {
		if(orderId <= 0) {
			return null;
		}
		
		return OrderService.get(orderId);
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
		if(order.getCallbackStatus() != WxCallbackStatus.CALLBACK_SUCC.getStatus()) {
			rsp.setMsg("发送购买成功通知失败, 订单非支付成功状态");
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
		json.put("url", Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn") 
				+ "/front/Users/order?orderId="+orderId);
		json.put("touser", openId);
		
		//商品信息：{{name.DATA}}
		//{{remark.DATA}}
		JSONObject name = new JSONObject();
		name.put("value", goodTitle);
		name.put("color", "#000000");
		
		JSONObject remark = new JSONObject();
		remark.put("value", "欢迎再次购买!");
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
			rsp.setMsg("发送已发货通知失败, 订单已发货状态");
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
		json.put("url", Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn") 
				+ "/front/Users/order?orderId="+orderId);
		json.put("touser", openId);
		
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
		remark.put("value", "欢迎再次购买!");
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
	 */
	public static WxMsgRspDto refundMoneyResultMsg(long orderId) {
		WxMsgRspDto rsp = new WxMsgRspDto();
		rsp.setSuccess(false);
		rsp.setMsg("");
		
		RefundOrder refundOrder = RefundOrderService.getByOrder(orderId);
		if(null == refundOrder) {
			rsp.setMsg("发送退款结果通知失败, 无法获取退款单");
			return rsp;
		}
		if(refundOrder.getRefundState() == RefundStatus.APPLY.getCode() || 
				refundOrder.getRefundState() == RefundStatus.ING.getCode()) {
			rsp.setMsg("发送退款结果通知失败, 退款单状态为"+RefundStatus.APPLY.getDesc()+"或"+RefundStatus.ING.getDesc());
			return rsp;
		}
		Order order = checkOrder(orderId);
		if(null == order) {
			rsp.setMsg("发送退款结果通知失败, 无法获取订单");
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
		//TODO 这里加上退款单url, 退款金额再商量
		json.put("url", "");
		json.put("touser", openId);
		
		//{{first.DATA}}
		//订单编号：{{keyword1.DATA}}
		//订单金额：{{keyword2.DATA}}
		//退款金额：{{keyword3.DATA}}
		//{{remark.DATA}}
		JSONObject first = new JSONObject();
		first.put("value","您好，您的退款结果如下");
		first.put("color", "#000000");
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", order.getId());
		keyword1.put("color", "#000000");
		
		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", order.getTotalFee());
		keyword2.put("color", "#000000");
		
		JSONObject keyword3 = new JSONObject();
		keyword3.put("value", order.getTotalFee());
		keyword3.put("color", "#000000");
		
		JSONObject remark = new JSONObject();
		remark.put("value", "感谢您的购买!");
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
	public static WxMsgRspDto withdrawMoneyResultMsg(long orderId) {
		WxMsgRspDto rsp = new WxMsgRspDto();
		rsp.setSuccess(false);
		rsp.setMsg("");
		//TODO 提现结果通知，等有表结构再完善，记得把下面url补全
		String openId = "olVhYv2ogEVJYgaRhEpIh83NZh5c";
		User user = UserService.getByOpenId(openId);
		
		//包装请求body
		JSONObject json = new JSONObject();
		json.put("touser", openId);
		json.put("template_id", withdrawMoneyTplId);
		json.put("url", "");
		json.put("touser", openId);
		
		//{{first.DATA}}
		//提现商户：{{keyword1.DATA}}
		//提现金额：{{keyword2.DATA}}
		//提现账户：{{keyword3.DATA}}
		//处理时间：{{keyword4.DATA}}
		//{{remark.DATA}}
		JSONObject first = new JSONObject();
		first.put("value","您好，您的提现结果如下");
		first.put("color", "#000000");
		
		JSONObject keyword1 = new JSONObject();
		keyword1.put("value", user.getNickname());
		keyword1.put("color", "#000000");

		JSONObject keyword2 = new JSONObject();
		keyword2.put("value", 10);
		keyword2.put("color", "#000000");
		
		JSONObject keyword3 = new JSONObject();
		keyword3.put("value", "微信钱包");
		keyword3.put("color", "#000000");
		
		JSONObject keyword4 = new JSONObject();
		keyword4.put("value", "2016-06-15 12:00:00");
		keyword4.put("color", "#000000");
		
		JSONObject remark = new JSONObject();
		remark.put("value", "感谢您的关注!");
		remark.put("color", "#000000");
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("first", first);
		dataJson.put("keyword1", keyword1);
		dataJson.put("keyword2", keyword2);
		dataJson.put("keyword3", keyword3);
		dataJson.put("keyword4", keyword4);
		dataJson.put("remark", remark);
		
		json.put("data", dataJson);
		
		return WxTplMsgServcie.sendWxTplMsg(json);
	}
}
