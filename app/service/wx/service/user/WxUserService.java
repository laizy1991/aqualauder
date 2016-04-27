package service.wx.service.user;

import models.User;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import play.cache.Cache;
import service.UserService;
import service.wx.dto.user.SubscribeReqDto;
import utils.EmojiFilter;
import utils.WxUtil;
import utils.http.HttpRequester;
import utils.http.HttpRespons;

import com.google.gson.Gson;

import common.constants.RegType;

public class WxUserService {
	public static Gson gson = new Gson();
	/**
	 * 获取用户OpenID
	 * @return
	 */
	public static String getUserOpenId(String code) {
		if(StringUtils.isEmpty(code))
			return null;
		//通过code换取openid
		String appId = Play.configuration.getProperty("wx.config.appid");
		String appSecret = Play.configuration.getProperty("wx.config.appsecret");
		String accessCodeUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
		if(StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)) {
			return null;
		}
		String realUrl = String.format(accessCodeUrl, appId, appSecret, code);
		HttpRespons resp = null;
		try {
			resp = HttpRequester.sendGet(realUrl);
		} catch (Exception e) {
			Logger.error("通过code向微信请求获取用户OpenId发生错误");
			e.printStackTrace();
		}
		if(null == resp)
			return null;
		JSONObject json = JSONObject.fromObject(resp.getContent());
		String openId = json.optString("openid");
		if(StringUtils.isBlank(openId)) {
			Logger.error("微信返回用户OpenId时发生错误，错误码：%s，错误信息：%s", 
					json.getString("errcode"), json.getString("errmsg"));
			return null;
		}
		
		return openId;
	}
	
	/**
	 * 通过openID获取用户信息
	 * @param openId
	 * @return
	 */
	public static JSONObject getUserInfoByOpenId(String openId) {
		if(StringUtils.isEmpty(openId)) {
			return null;
		}
		String accessToken = WxUtil.getAccessToken();
		String accessUserUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
		if(StringUtils.isBlank(accessToken)) {
			return null;
		}
		String realUrl = String.format(accessUserUrl, accessToken, openId);
		HttpRespons resp = null;
		try {
			resp = HttpRequester.sendGet(realUrl);
		} catch (Exception e) {
			Logger.error("向微信请求获取用户信息发生错误");
			e.printStackTrace();
		}
		if(null == resp)
			return null;
		JSONObject json = JSONObject.fromObject(resp.getContent());
		if(null == json || StringUtils.isNotBlank(json.optString("errcode"))) {
			Logger.error("微信返回用户信息时发生错误，错误码：%s，错误信息：%s", 
					json.getString("errcode"), json.getString("errmsg"));
			return null;
		}
		
		return json;
	}
	
	/**
	 * 用户关注事件
	 */
	public static boolean subscribe(SubscribeReqDto subscribeReqDto) {
		if(null == subscribeReqDto) {
			Logger.error("传入的关注事件Dto为空");
			return false;
		}
		Logger.info("传入的关注参数为: %s", gson.toJson(subscribeReqDto));
		String openId = subscribeReqDto.getFromUserName();
		//微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次。
		//关于重试的消息排重，推荐使用FromUserName + CreateTime 排重。
		String isSubscribed = Cache.get(subscribeReqDto.getFromUserName()+"_"+subscribeReqDto.getCreateTime(), String.class);
		if(!StringUtils.isBlank(isSubscribed)) {
			return true;
		}
		//向微信获取用户的信息，新增入库
		JSONObject userJson = WxUserService.getUserInfoByOpenId(openId);
		if(null == userJson) {
			Logger.error("用户关注后向微信请求用户信息失败, openId[%s]", openId);
			return false;
		}
		int sex = userJson.optInt("sex", 0);
		String nickname = userJson.optString("nickname", "用户");
		nickname = EmojiFilter.filterEmoji(nickname);
		String headImgUrl = userJson.optString("headimgurl");
		long subTime = userJson.optLong("subscribe_time");
		long subscribeTime = subTime*1000;
		String ticket = subscribeReqDto.getTicket();
		//先通过用户openId查询是否存在
		User user = UserService.getByOpenId(openId);
		if(null != user) {
			user.setSex(sex);
			user.setNickname(nickname);
			user.setSubscribeTime(subscribeTime);
			user.setUpdateTime(System.currentTimeMillis());
			user.setTicket(ticket);
			Logger.info("用户已关注过该公众号，进行更新操作, 参数为: %s", gson.toJson(user));
			UserService.update(user);
			return true;
		}
		Logger.info("用户未关注过该公众号，进行入表操作, openId[%s]", openId);
		user = new User();
		user.setOpenId(openId);
		user.setRegType(RegType.WeiXin.getValue());
		user.setNickname(nickname);
		user.setSex(sex);
		user.setHeadImgUrl(headImgUrl);
		user.setSubscribeTime(subscribeTime);
		user.setCreateTime(System.currentTimeMillis());
		user.setUpdateTime(System.currentTimeMillis());
		user.setTicket(ticket);
		if(!UserService.add(user)) {
			Logger.error("新增用户失败, 参数为: %s", gson.toJson(user));
			return false;
		}
		Cache.set(subscribeReqDto.getFromUserName()+"_"+subscribeReqDto.getCreateTime(), "added", "30mn");
		Logger.error("新增用户成功, 参数为: %s", gson.toJson(user)); 
		
		return true;
	}
	
}
