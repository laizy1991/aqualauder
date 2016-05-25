package controllers.front;

import models.Distributor;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import service.DistributorService;
import service.UserService;
import service.UserWalletService;
import service.wx.dto.jspai.JsapiConfig;
import service.wx.service.jsapi.JsApiService;
import service.wx.service.user.WxUserService;

import com.google.gson.Gson;

import common.annotation.GuestAuthorization;
import common.constants.CashType;
import common.core.FrontController;
import dto.DistributorDetail;
import dto.MySpaceDto;

public class Users extends FrontController {
	public static Gson gson = new Gson();
    @GuestAuthorization
    public static void orders() {
        render("/Front/user/orders.html");
    }
    public static void qrcodeShare(String userId) {
    	User user = null;
    	int id = -1;
    	try {
    		id = Integer.parseInt(userId);
    	} catch(Exception e) {
    		Logger.error("userId[%s]非整型", userId);
    	}
    	if(id > 0) {
    		user = UserService.get(id);
    	} 
    	if(null == user) {
    		render("/Front/user/companyQrcode.html");
    	}
    	Distributor dist = DistributorService.get(user.getUserId());
    	if(null == dist) {
    		render("/Front/user/companyQrcode.html");
    	}
    	String querystring = request.querystring;
    	String protocol = request.secure?"https://":"http://";
    	String action = request.path;
    	String url =  protocol + request.domain + action + "?" + querystring;
    	
    	Logger.info("生成的分享链接为: %s", url);
    	JsapiConfig config = JsApiService.getSign(url);
    	Logger.info("config参数为: %s", gson.toJson(config));
    	
    	String qrImg = Play.configuration.getProperty("wx.qrcode.prefix", "/qrcode/")+ dist.getQrcodeLimitPath();
    	render("/Front/user/qrcodeShare.html", user, qrImg, config);
    	
    }
    
    @GuestAuthorization
    public static void qrcode() {
    	String openId = session.get("openId");
    	User user = null; 
		if(!StringUtils.isBlank(openId)) {
			user = WxUserService.getUserInfo(openId);
		}
		if(null == user) {
    		render("/Front/user/companyQrcode.html");
    	}
    	Distributor dist = DistributorService.get(user.getUserId());
    	if(null == dist) {
    		render("/Front/user/companyQrcode.html");
    	}
    	redirect("/front/Users/qrcodeShare?userId="+user.getUserId());
    }
    @GuestAuthorization
    public static void getUserInfo(String code) {
        String openId = session.get("openId");
        if(StringUtils.isBlank(openId)) {
            if(StringUtils.isBlank(code)) {
                Logger.error("code为空");
                renderText("非法请求");
            }
            openId = WxUserService.getUserOpenIdByCode(code);
        }
        
        if(StringUtils.isBlank(openId)) {
            Logger.error("openId为空");
            renderText("非法请求");
        }
        User user = WxUserService.getUserInfo(openId);
        if(null == user) {
            Logger.error("获取用户信息失败, openId: %s", openId);
            renderText("非法请求");
        }
        
        DistributorDetail detail = DistributorService.distributorDetail(user.getUserId());
        
        MySpaceDto data = new MySpaceDto();
        data.setUser(user);
        data.setDetail(detail);
        render("/Front/user/myspace.html", data, code);
    }
    @GuestAuthorization
    public static void cash(String amount) {
        String openId = session.get("openId");
        boolean isSucc = false;
        if(StringUtils.isBlank(openId)) {
            renderJSON(isSucc);
        }
        User user = WxUserService.getUserInfo(openId);
        if(null == user) {
            Logger.error("获取用户信息失败, openId: %s", openId);
            renderJSON(isSucc);
        }
        try {
            int cashAmount = (int)(Double.parseDouble(amount) * 100);
            if(cashAmount > 0) {
                isSucc = UserWalletService.cash(user.getUserId(), cashAmount, CashType.REDPACK.getCode(), "");
            }
        } catch(Exception e) {
            Logger.error(e, "");
        }
        renderJSON(isSucc);
    }
}
