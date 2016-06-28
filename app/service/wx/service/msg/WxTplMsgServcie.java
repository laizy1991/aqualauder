package service.wx.service.msg;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import common.constants.wx.WxCallbackStatus;

import models.Order;
import models.User;
import net.sf.json.JSONObject;
import play.Logger;
import play.Play;
import service.UserService;
import service.wx.common.Configure;
import utils.WxAccessTokenUtil;
import utils.http.HttpRequester;
import utils.http.HttpRespons;
import dto.WxMsgRspDto;

public class WxTplMsgServcie {
	/**
	 * 发送消息接口
	 * @param json
	 * @return
	 */
	public static WxMsgRspDto sendWxTplMsg(JSONObject json) {
		WxMsgRspDto rsp = new WxMsgRspDto();
		rsp.setSuccess(false);
		rsp.setMsg("");
		
		String url = String.format(Configure.SEND_TPL_MSG_API, WxAccessTokenUtil.getAccessToken());
		HttpRespons httpResp = null;
		try {
			httpResp = HttpRequester.sendPost(url, json.toString());
		} catch (IOException e) {
			e.printStackTrace();
			rsp.setMsg("发送请求失败");
			return rsp;
		}
		if(null == httpResp) {
			Logger.error("发送模板消息返回内容为空，发送消息为: %s", json.toString());
			rsp.setMsg("发送模板消息返回内容为空");
			return rsp;
		}
		
		JSONObject respJson = JSONObject.fromObject(httpResp.getContent());
		if(respJson.optString("errmsg").equalsIgnoreCase("ok") && respJson.optInt("errcode")==0) {
			Logger.info("发送模板消息成功, 内容为: %s", json.toString());
			rsp.setSuccess(true);
			rsp.setMsg("");
		} else {
			Logger.error("发送模板消息失败, 出错内容为: %s, 消息内容为: %s", 
					respJson.optString("errmsg"), json.toString());
			rsp.setMsg(respJson.optString("errmsg"));
		}
		
		return rsp;
	}
}
