package utils;

import net.sf.json.JSONObject;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import play.cache.Cache;
import utils.http.HttpRequester;
import utils.http.HttpRespons;

public class WxUtil {
	public static final String CACHE_PREFIX = "wx_";
	private static String ACCESS_TOKEN_CACHE_KEY = null;		//access_token缓存key
	public static String ACCESS_TOKEN_CACHE_TIMEOUT = null;		//access_token在缓存中有效时间
	
	static {
		ACCESS_TOKEN_CACHE_KEY = CACHE_PREFIX + "accessToken";
		ACCESS_TOKEN_CACHE_TIMEOUT = Play.configuration.getProperty("wx.access.token.timeout", "90mn");  //有效时间一个半小时
	}
	
	/**
	 * 获取微信AccessToken
	 * @return
	 */
	public static String getAccessToken() {
		String accessToken = Cache.get(ACCESS_TOKEN_CACHE_KEY, String.class);
		if(StringUtils.isEmpty(accessToken)) {
			accessToken = getAccessTokenFromWx();
		}
		return accessToken;
	}
	
	/**
	 * 向微信服务器获取AccessToken
	 * expiration Ex: 10s, 3mn, 8h
	 * @return
	 */
	private static String getAccessTokenFromWx() {
		String accessToken = getAccessTokenFromWxWithValidation();
		//代码写在这里
		if(StringUtils.isEmpty(accessToken)) {
			Logger.error("从微信获取AccessToken失败，准备再试一次");
			//可能是网络原因，再试一次
			accessToken = getAccessTokenFromWxWithValidation();
			if(StringUtils.isEmpty(accessToken)) {
				Logger.error("从微信获取AccessToken又失败，打个日志算了");
				return null;
			}
		}
		Cache.set(ACCESS_TOKEN_CACHE_KEY, accessToken, ACCESS_TOKEN_CACHE_TIMEOUT);
		return accessToken;
	}
	
	/**
	 * 通过appid跟secret换取token
	 * @return
	 */
	private static String getAccessTokenFromWxWithValidation() {
		//获取配置项
		String appId = Play.configuration.getProperty("wx.config.appid");
		String appSecret = Play.configuration.getProperty("wx.config.appsecret");
		String accessTokenUrl = Play.configuration.getProperty("wx.access.token.url");
		if(StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret) ||
				StringUtils.isBlank(accessTokenUrl)) {
			return null;
		}
		String realUrl = String.format(accessTokenUrl, appId, appSecret);
		HttpRespons resp = null;
		try {
			resp = HttpRequester.sendGet(realUrl);
		} catch (Exception e) {
			Logger.error("请求微信基础支持AccessToken发生错误");
			e.printStackTrace();
		}
		if(null == resp)
			return null;
		JSONObject json = JSONObject.fromObject(resp.getContent());
		if(StringUtils.isBlank(json.optString("access_token"))) {
			Logger.error("微信返回的基础支持AccessToken错误，错误码：%s，错误信息：%s", 
					json.getString("errcode"), json.getString("errmsg"));
			return null;
		}
		return json.getString("access_token");
	}
	
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
		String accessCodeUrl = Play.configuration.getProperty("wx.access.code.url");
		if(StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret) ||
				StringUtils.isBlank(accessCodeUrl)) {
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
		String accessUserUrl = Play.configuration.getProperty("wx.access.user.url");
		if(StringUtils.isBlank(accessToken) || StringUtils.isBlank(accessUserUrl) ) {
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
		if(null == json || StringUtils.isNotEmpty(json.optString("errcode"))) {
			Logger.error("微信返回用户信息时发生错误，错误码：%s，错误信息：%s", 
					json.getString("errcode"), json.getString("errmsg"));
			return null;
		}
		
		return json;
	}
}
