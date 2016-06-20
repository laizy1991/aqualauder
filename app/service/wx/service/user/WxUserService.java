package service.wx.service.user;

import models.Distributor;
import models.User;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import play.cache.Cache;
import service.DistributorService;
import service.UserService;
import service.wx.dto.user.SubscribeReqDto;
import utils.EmojiFilter;
import utils.WxAccessTokenUtil;
import utils.http.HttpRequester;
import utils.http.HttpRespons;

import com.google.gson.Gson;
import common.constants.RegType;

import dao.DistributorDao;
import dao.DistributorSuperiorDao;

public class WxUserService {
	public static Gson gson = new Gson();
	public static final String USERINFO_CACHE_TIME = "12h";
	/**
	 * 获取用户OpenID
	 * @return
	 */
	public static String getUserOpenIdByCode(String code) {
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
	private static JSONObject getUserInfoByOpenId(String openId) {
		if(StringUtils.isEmpty(openId)) {
			return null;
		}
		String accessToken = WxAccessTokenUtil.getAccessToken();
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
	 * 将用户信息入库，所有需要将用户信息入库的代码，统一调这个接口
	 * 目前的触发条件为：用户关注公众号和进入公众号(用户关注公众号后将用户信息入库失败的情况下)
	 * 1. 先从缓存拿
	 * 2. 缓存若没有，则从库拿并放入缓存，缓存key为用户openId, value为用户对象
	 * 3. 库也没有，向微信拿，入库并放缓存
	 * @param openId
	 * @return
	 */
	private static User addUser(JSONObject userJson) {
		if(null == userJson) {
			Logger.error("将用户信息入库时，入参为空");
			return null;
		}
		String openId = userJson.getString("openid");
		if(StringUtils.isBlank(openId)) {
			Logger.error("即将入库的用户对象openId为空, 参数: %s", userJson);
			return null;
		}
		User user = new User();
		user.setRegType(RegType.WeiXin.getValue());
		user.setOpenId(openId);
		user.setNickname(EmojiFilter.filterEmoji(userJson.optString("nickname", "用户")));
		user.setSex(userJson.optInt("sex", 0));
		user.setHeadImgUrl(userJson.optString("headimgurl"));
		user.setSubscribeTime(userJson.optLong("subscribe_time") * 1000);
		user.setCreateTime(System.currentTimeMillis());
		user.setUpdateTime(System.currentTimeMillis());
		
		if(!UserService.add(user)) {
			Logger.error("新增用户失败, 参数为: %s", gson.toJson(user));
			return null;
		}
		//将用户信息从库中拿来，并放入缓存中，userId可能有用
		User u = UserService.getByOpenId(openId);
		if(null == u) {
			Logger.error("新增用户成功，但查库时获取到的用户对象为空, openId: %s", openId);
			return null;
		}
		
		Logger.info("新增用户成功，并将用户信息放入缓存中，用户信息: %s", gson.toJson(user));
		Cache.add(openId, u, USERINFO_CACHE_TIME);
		return u;
	}
	
	/**
	 * 获取用户信息
	 * 1. 先从缓存拿
	 * 2. 缓存若没有，则从库拿并放入缓存，缓存key为用户openId, value为用户对象
	 * 3. 库也没有，向微信拿，入库并放缓存
	 * @param openId
	 * @return
	 */
	public static User getUserInfo(String openId) {
		if(StringUtils.isBlank(openId)) {
			Logger.error("获取用户信息时，入库的openId[%s]为空", openId);
		}
		User user = Cache.get(openId, User.class);
		if(null != user) {
			return user;
		}
		
		Logger.info("缓存中没有用户信息，从库里获取, openId: %s", openId);
		user = UserService.getByOpenId(openId);
		if(null != user) {
			//将用户信息放入缓存中
			Cache.add(openId, user, USERINFO_CACHE_TIME);
			return user;
		}
		
		Logger.info("库里没有用户信息, 向微信获取, openId: %s", openId);
		JSONObject userJson = WxUserService.getUserInfoByOpenId(openId);
		if(null == userJson) {
			Logger.error("向微信获取用户信息失败, openId: %s", openId);
			return null;
		}
		
		Logger.info("向微信获取用户信息成功, 准备入库, 参数为: %s", userJson);
		return WxUserService.addUser(userJson);
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
		
		//先通过用户openId查询是否有该用户的信息
		User user = UserService.getByOpenId(openId);
		if(null != user) {
			Logger.info("用户关注之前，其用户信息已在库中，可能是之前关注或关注后用户信息入库失败，参数为: %s", gson.toJson(user));
			user.setSex(userJson.optInt("sex", 0));
			user.setNickname(EmojiFilter.filterEmoji(userJson.optString("nickname", "用户")));
			user.setSubscribeTime(userJson.optLong("subscribe_time") * 1000 );
			user.setHeadImgUrl(userJson.optString("headimgurl"));
			user.setUpdateTime(System.currentTimeMillis());
			Logger.info("用户已关注过该公众号，进行更新操作, 参数为: %s", gson.toJson(user));
			UserService.update(user);
			user = UserService.getByOpenId(openId);
			if(null != user) {
				Cache.set(openId, user, USERINFO_CACHE_TIME);
			} else {
				Cache.delete(openId);
			}
			return true;
		}
		
		Logger.info("用户未关注过该公众号，进行入库操作, openId[%s]", openId);
		User u = WxUserService.addUser(userJson);
		if(null == u) {
			return false;
		}

		try {
			//判断是否有用户分享带来其它用户
			if(!StringUtils.isBlank(subscribeReqDto.getEventKey()) && subscribeReqDto.getEventKey().toLowerCase().startsWith("qrscene_")) {
				//qrscene_后为二维码参数，若为Integer则为临时二维码(用户ID)，若为String，则为永久二维码(用户openId)
				String[] args = subscribeReqDto.getEventKey().split("_");
				if(null != args && args.length >=2) {
					String param = args[1];
					Integer superiorId = null;
					if(StringUtils.isNumeric(param)) {
						//临时二维码(用户ID)
						superiorId = Integer.parseInt(param);
					} else {
						//永久二维码(用户openId)
						int firstTimeDownLine = subscribeReqDto.getEventKey().indexOf("_");
						if(-1 != firstTimeDownLine) {
							String superiorOpenId = subscribeReqDto.getEventKey().substring(firstTimeDownLine+1);
							User superiorUser = UserService.getByOpenId(superiorOpenId);
							if(null == superiorUser) {
								Logger.error("用户分享带来的下线通过openId获取到的用户时空, openId: %s", superiorOpenId);
								superiorId = null;
							} else {
								superiorId = superiorUser.getUserId();
							}
						} else {
							Logger.error("处理微信下线关注切割永久二维码时无法解析下划线位置, eventKey: %s", subscribeReqDto.getEventKey());
						}
					}
					if(null == superiorId) {
						Logger.error("获取用户上线userId为空");
					} else {
						//将信息写入分销商上线表，这里先检验userId, superiorId组合是否存在
						Distributor dis = DistributorDao.get(u.getUserId());
						if(null != dis) {
							//该用户之前已有上线
							Logger.warn("用户[%d]之前已成为用户[%d]的下线", u.getUserId(), superiorId);
						} else {
							//库中没有userId所对应的记录，即用户之前并不存在于库中
							if(superiorId != u.getUserId() && DistributorSuperiorDao.create(u.getUserId(), superiorId)) {
								Logger.info("用户[%d]分享带来的下线[%d]入库成功", superiorId, u.getUserId());
							} else {
								Logger.error("用户[%d]分享带来的下线[%d]相同或入库失败", superiorId, u.getUserId());
							}
						}
					}
				} else {
					Logger.error("用户分享带来的下线解析参数时发生错误, eventKey: %s", subscribeReqDto.getEventKey());
				}
			}
		} catch(Exception e) {
			Logger.error("用户分享带来下线进行逻辑处理时发生错误");
			e.printStackTrace();
		}
		
		//这里主要是用于微信消息重试的排重
		Cache.set(subscribeReqDto.getFromUserName()+"_"+subscribeReqDto.getCreateTime(), "added", "30mn");
		return true;
	}
	
}
