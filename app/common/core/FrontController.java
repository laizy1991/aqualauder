package common.core;

import java.io.UnsupportedEncodingException;

import models.QrShare;
import models.User;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Http.Request;
import service.QrShareService;
import service.UserService;
import service.wx.common.Configure;
import service.wx.service.user.WxUserService;
import common.constants.GlobalConstants;
import common.constants.RegType;
import exception.BusinessException;

/**
 * 
 * @author laimt
 *
 */
public class FrontController extends BaseController {
	@Before
	protected static void beforeAction() throws Exception {
		Logger.info("Action: %s, QueryString: %s", Request.current().path, Request.current().querystring);
		String code = request.params.get("code");
		String openId = session.get("openId");
		Logger.info("beforeAction: code[%s], openId[%s]", code, openId);
		if(!StringUtils.isBlank(code)) {
			//code不为空: 1) 从公众号进入 2)点击分享链接跳转后
	    	if(StringUtils.isBlank(openId)) {
	    		//首先获取网页accessToken和用户openId
	    		JSONObject accessCodeJson = WxUserService.getUserOpenIdAndAccessTokenByCode(code); 
	    		if(null == accessCodeJson) {
	    			return;
	    		}
    			openId = accessCodeJson.optString("openid");
	    		String state = request.params.get("state");
	    		if(!StringUtils.isBlank(state) && state.equals("fromshare")) {
	    			Logger.info("通过分享链接进入");
	    			//用户通过分享进入，其信息可能不在本地，先查库，不存在则向微信请求
	    			if(null == UserService.getByOpenId(openId)) {
	    				//向微信请求，并入库
	    				JSONObject userJson = WxUserService.getUserInfoByOpenId(accessCodeJson.optString("access_token"), openId);
	    				if(null == userJson) {
	    					return;	//向微信获取信息失败
	    				}
    					if(null == WxUserService.addUser(userJson)) {
    						return;	//添加用户信息失败
    					}
	    			}
	    		} 
	    		session.put("openId", openId);
	    	}
		} else {
			if(StringUtils.isBlank(openId)) {
				//从分享链接进来，准备跳转
	    		if(-1 != Request.current().querystring.indexOf("from") && -1 != 
	    				Request.current().querystring.indexOf("isappinstalled")) {
	    			Logger.info("分享链接准备跳转");
	    			//跳转到授权页
	    			String callbackUrl = Play.configuration.getProperty("local.host.domain") + Request.current().path +
	    					"?"+Request.current().querystring;
					callbackUrl = java.net.URLEncoder.encode(callbackUrl, "utf-8");
	    			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=fromshare#wechat_redirect";
	    			redirect(String.format(url, Configure.getAppid(), callbackUrl));
	    		}
			}
		}
	}
	
	@Catch(Exception.class)
	protected static void onException(Throwable throwable) {
		
		if (throwable instanceof BusinessException){
			BusinessException be = (BusinessException)throwable;
			Logger.warn(throwable, "@Catch Exception: " + be.getMessage());
			String msg = be.getMessage();
			renderTemplate("errors/500.html", msg);
		} else {
			Logger.error(throwable, "Unknown Exception: " + throwable.getMessage());
			error(Messages.get("server.error"));
		}
	}
	
	protected static void renderError(String msg) {
		renderTemplate("errors/500.html", msg);
	}
	
	protected static String getQrCodeBg() {
    	String qrcodeBg = "/public/images/qrcode_bg.jpg";
    	QrShare qrShare = QrShareService.getLastIsEnabledRec(GlobalConstants.IS_ENABLED);
    	
    	if(null != qrShare && !StringUtils.isBlank(qrShare.getImgUrl())) {
    		qrcodeBg = qrShare.getImgUrl();
    	}
    	return qrcodeBg;
    }
}
