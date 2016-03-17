package utils;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import play.cache.Cache;

public class WxUtil {
	public static String CACHE_PREFIX = "wx_";
	private static String ACCESS_TOKEN_KEY = CACHE_PREFIX + "accessToken";
	public static String ACCESS_TOKEN_TIMEOUT = 
			Play.configuration.getProperty("wx.access.token.timeout", "90mn");  //有效时间一个半小时
	
	/**
	 * 获取微信AccessToken
	 * @return
	 */
	public static String getAccessToken() {
		String accessToken = Cache.get(ACCESS_TOKEN_KEY, String.class);
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
		Cache.set(ACCESS_TOKEN_KEY, accessToken, ACCESS_TOKEN_TIMEOUT);
		return accessToken;
	}
	
	/**
	 * 通过appid跟secret换取token
	 * @return
	 */
	private static String getAccessTokenFromWxWithValidation() {
		return "";
	}
}
