package utils;

import net.sf.json.JSONObject;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import play.cache.Cache;
import utils.http.HttpRequester;
import utils.http.HttpRespons;

public class WxJsApiUtil {
	public static final String CACHE_PREFIX = "wx_";
	private static String JS_API_CACHE_KEY = null;		//jsapi缓存key
	public static String JS_API_CACHE_TIMEOUT = null;		//jsapi在缓存中有效时间
	private static String JS_API_CACHE_LOCKED = "js_api_cache_locked";
	
	static {
		JS_API_CACHE_KEY = CACHE_PREFIX + "jsApi";
		JS_API_CACHE_TIMEOUT = Play.configuration.getProperty("wx.js.api.timeout", "90mn");  //有效时间一个半小时
	}
	
	/**
	 * 获取微信JSAPI_TICKET
	 * @return
	 */
	public static String getJsApiTicket() {
		String ticket = Cache.get(JS_API_CACHE_KEY, String.class);
		if(StringUtils.isEmpty(ticket)) {
			ticket = getJsApiTicketFromWx();
		}
		return ticket;
	}
	
	/**
	 * 向微信服务器获取JSAPI_TICKET
	 * expiration Ex: 10s, 3mn, 8h
	 * @return
	 */
	private static String getJsApiTicketFromWx() {
		//判断是否加锁了
		DistributeCacheLock lockMgr = DistributeCacheLock.getInstance();
		if(lockMgr.isCacheLocked(JS_API_CACHE_LOCKED)) {
			Logger.error("微信jsApiTicket缓存被加锁了，可能是别的线程正在更新jsApiTicket");
			return null;
		}
		
		//加锁5秒
		if(lockMgr.tryCacheLock(JS_API_CACHE_LOCKED, "locked", "5s")) {
			Logger.info("微信jsApiTicket缓存加锁成功，加锁时间为5秒");
		} else {
			Logger.error("微信jsApiTicket缓存加锁失败");
		}
		
		String ticket = getJsApiTicketFromWxWithValidation();
		if(StringUtils.isEmpty(ticket)) {
			Logger.error("从微信获取jsApiTicket失败，准备再试一次");
			//可能是网络原因，再试一次
			ticket = getJsApiTicketFromWxWithValidation();
			if(StringUtils.isEmpty(ticket)) {
				//解锁
				lockMgr.unLock(JS_API_CACHE_LOCKED);
				Logger.error("从微信获取jsApiTicket又失败，打个日志算了");
				return null;
			}
		}
		Cache.set(JS_API_CACHE_KEY, ticket, JS_API_CACHE_TIMEOUT);
		lockMgr.unLock(JS_API_CACHE_LOCKED);
		return ticket;
	}
	
	/**
	 * 通过appid跟secret换取token
	 * @return
	 */
	private static String getJsApiTicketFromWxWithValidation() {
		//获取配置项
		String accessToken = WxAccessTokenUtil.getAccessToken();
		String jsApiUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
		String realUrl = String.format(jsApiUrl, accessToken);
		HttpRespons resp = null;
		try {
			resp = HttpRequester.sendGet(realUrl);
		} catch (Exception e) {
			Logger.error("请求获取微信jsapi_ticket发生错误");
			e.printStackTrace();
		}
		if(null == resp) 
			return null;
		
		JSONObject json = JSONObject.fromObject(resp.getContent());
		if(StringUtils.isBlank(json.optString("ticket"))) {
			Logger.error("微信返回的jsapi_ticket错误，错误码：%s，错误信息：%s", 
					json.getString("errcode"), json.getString("errmsg"));
			return null;
		}
		Logger.info("get jsapi_ticket from weixin: %s", json.getString("ticket"));
		return json.getString("ticket");
	}
	
}
