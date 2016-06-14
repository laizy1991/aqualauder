package controllers.ajax;

import java.io.IOException;

import models.Order;
import models.User;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import service.OrderService;
import service.UserService;
import service.wx.common.Configure;
import utils.WxAccessTokenUtil;
import utils.http.HttpRequester;
import utils.http.HttpRespons;

import common.constants.wx.WxCallbackStatus;
import common.core.AjaxController;

public class WxMsg extends AjaxController {
	private static String goodsBoughtTplId = "OBF7J8Nqo8E92lz25ZUfzvA77BtjVbru7nUTJRJ6hY8"; //购买成功
	private static String goodsSendedTplId = "ZqEYpGZMjP6iNVpEYebApogq-ISHElqqZ6Osg3zCU-M"; //已发货
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
	public static void sendBuyResultMsg(long orderId) {
		Order order = checkOrder(orderId); 
		if(null == order) {
			renderErrorJson("发送购买成功通知失败--无法获取订单");
		}
		if(order.getCallbackStatus() != WxCallbackStatus.CALLBACK_SUCC.getStatus()) {
			renderErrorJson("发送购买成功通知失败--订单非支付成功状态，");
		}
		String openId = order.getOpenId();
		if(StringUtils.isEmpty(openId)) {
			User user = UserService.get(order.getUserId());
			if(null == user) {
				renderErrorJson("发送购买成功通知失败--无法获取下单用户openId");
			}
			openId = user.getOpenId();
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
		JSONObject nameJson = new JSONObject();
		//TODO 这里把商品名称加上
		nameJson.put("value", "服装商品");
		nameJson.put("color", "#000000");
		
		JSONObject remarkJson = new JSONObject();
		remarkJson.put("value", "欢迎再次购买!");
		remarkJson.put("color", "#000000");
		
		JSONObject dataJson = new JSONObject();
		dataJson.put("name", nameJson);
		dataJson.put("remark", remarkJson);
		json.put("data", dataJson);
		
		String url = String.format(Configure.SEND_TPL_MSG_API, WxAccessTokenUtil.getAccessToken());
		HttpRespons resp = null;
		try {
			resp = HttpRequester.sendPost(url, json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(null == resp) {
			Logger.error("发送模板消息返回内容为空，发送消息为: %s", json.toString());
		}
		JSONObject respJson = JSONObject.fromObject(resp.getContent());
		if(respJson.optString("errmsg").equalsIgnoreCase("ok") && respJson.optInt("errcode")==0) {
			Logger.info("发送模板消息的成功, 内容为: %s", json.toString());
			renderSuccessJson();
		} else {
			Logger.error("发送模板消息的成功, 出错内容为: %s, 消息内容为: %s", 
					respJson.optString("errmsg"), json.toString());
			renderErrorJson(respJson.optString("errmsg"));
		}
	}
}
