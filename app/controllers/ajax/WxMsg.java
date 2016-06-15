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
import service.WxMsgService;
import service.wx.common.Configure;
import service.wx.service.msg.WxTplMsgServcie;
import utils.WxAccessTokenUtil;
import utils.http.HttpRequester;
import utils.http.HttpRespons;
import common.constants.wx.WxCallbackStatus;
import common.core.AjaxController;
import dto.WxMsgRspDto;

public class WxMsg extends AjaxController {
	
	/**
	 * 发送已发货通知，只有在检测到货品是已发货状态才能发送消息
	 * @param orderId
	 */
	public static void sendDeliveredResultMsg(long orderId) {
		WxMsgRspDto rsp = WxMsgService.sendDeliveredResultMsg(orderId);
		if(rsp.isSuccess()) {
			renderSuccessJson();
		} else {
			renderErrorJson(rsp.getMsg());
		}
	}
	
	/**
	 * 发送退款通知
	 * @param orderId
	 */
	public static void refundMoneyResultMsg(long orderId) {
		WxMsgRspDto rsp = WxMsgService.refundMoneyResultMsg(orderId);
		if(rsp.isSuccess()) {
			renderSuccessJson();
		} else {
			renderErrorJson(rsp.getMsg());
		}
	}
	
	/**
	 * 发送提通知
	 * @param orderId
	 */
	public static void withdrawMoneyResultMsg(long orderId) {
		WxMsgRspDto rsp = WxMsgService.withdrawMoneyResultMsg(orderId);
		if(rsp.isSuccess()) {
			renderSuccessJson();
		} else {
			renderErrorJson(rsp.getMsg());
		}
	}
}
