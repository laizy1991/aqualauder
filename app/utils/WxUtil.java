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
	private static String ACCESS_TOKEN_CACHE_LOCKED = "access_token_cache_locked";
	
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
		//判断是否加锁了
		DistributeCacheLock lockMgr = DistributeCacheLock.getInstance();
		if(lockMgr.isCacheLocked(ACCESS_TOKEN_CACHE_LOCKED)) {
			Logger.error("微信accessToken缓存被加锁了，可能是别的线程正在更新accessToken");
			return null;
		}
		
		//加锁5秒
		if(lockMgr.tryCacheLock(ACCESS_TOKEN_CACHE_LOCKED, "locked", "5s")) {
			Logger.info("微信accessToken缓存加锁成功，加锁时间为5秒");
		} else {
			Logger.error("微信accessToken缓存加锁失败");
		}
		
		String accessToken = getAccessTokenFromWxWithValidation();
		//代码写在这里
		if(StringUtils.isEmpty(accessToken)) {
			Logger.error("从微信获取AccessToken失败，准备再试一次");
			//可能是网络原因，再试一次
			accessToken = getAccessTokenFromWxWithValidation();
			if(StringUtils.isEmpty(accessToken)) {
				//解锁
				lockMgr.unLock(ACCESS_TOKEN_CACHE_LOCKED);
				Logger.error("从微信获取AccessToken又失败，打个日志算了");
				return null;
			}
		}
		Cache.set(ACCESS_TOKEN_CACHE_KEY, accessToken, ACCESS_TOKEN_CACHE_TIMEOUT);
		lockMgr.unLock(ACCESS_TOKEN_CACHE_LOCKED);
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
		String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
		if(StringUtils.isBlank(appId) || StringUtils.isBlank(appSecret)) {
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
		Logger.info("get access_token from weixin: %s", json.getString("access_token"));
		return json.getString("access_token");
	}
	
}
