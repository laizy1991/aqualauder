package common.core;

import models.User;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Http.Request;
import service.UserService;
import utils.WxUtil;

import common.constants.RegType;

import exception.BusinessException;

/**
 * 
 * @author laimt
 *
 */
public class FrontController extends BaseController {
	@Before
	protected static void beforeAction() throws SecurityException, NoSuchMethodException {
		Logger.info("Action - %s", Request.current().path);
	}
	
	@Catch(Exception.class)
	protected static void onException(Throwable throwable) {
		
		if (throwable instanceof BusinessException){
			BusinessException be = (BusinessException)throwable;
			Logger.warn(throwable, "@Catch Exception: " + be.getMessage());
			String msg = be.getMessage();
			renderTemplate("errors/error.html", msg);
		} else {
			Logger.error(throwable, "Unknown Exception: " + throwable.getMessage());
			error(Messages.get("server.error"));
		}
	}
	
	protected static void renderError(String msg) {
		renderTemplate("errors/error.html", msg);
	}
	
	/**
	 * 取得用户OpenId
	 * @return
	 */
	protected static String getOpenId(String code) {
		if(StringUtils.isEmpty(code)) {
			Logger.error("传入的code为空");
			return null;
		}
		String openId = session.get("openId");
    	if(StringUtils.isEmpty(openId)) {
    		openId = WxUtil.getUserOpenId(code);
    		if(StringUtils.isEmpty(openId)) {
    			return null;
    		}
    		User user = UserService.getByOpenId(openId);
    		if(null == user) {
    			//向微信获取用户的信息，新增入库
    			JSONObject userJson = WxUtil.getUserInfoByOpenId(openId);
    			if(null == userJson) {
    				return null;
    			}
    			user = new User();
    			user.setOpenId(openId);
    			user.setRegType(RegType.WeiXin.getValue());
				user.setNickname(userJson.optString("nickname"));
				user.setSex(userJson.optInt("sex", 0));
				user.setHeadImgUrl(userJson.optString("headimgurl"));
				user.setSubscribeTime(userJson.optLong("subscribe_time", System.currentTimeMillis()));
				user.setCreateTime(System.currentTimeMillis());
				user.setUpdateTime(System.currentTimeMillis());
    			if(!UserService.add(user)) {
    				//这里不影响后续操作，但在必须取得用户信息时必须做校验
    				Logger.error("新增用户失败"); 
    			}
    			session.put("openId", openId);
    		}
    	}
    	return openId;
	}
	
}
