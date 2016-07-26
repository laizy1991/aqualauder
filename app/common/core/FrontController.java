package common.core;

import models.QrShare;
import models.User;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Http.Request;
import service.QrShareService;
import service.UserService;
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
	protected static void beforeAction() throws SecurityException, NoSuchMethodException {
		Logger.info("Action: %s, QueryString: %s", Request.current().path, Request.current().querystring);
		String code = request.params.get("code");
		if(!StringUtils.isBlank(code)) {
	    	String openId = session.get("openId");
	    	if(StringUtils.isBlank(openId)) {
	    		//用户通过公众号自定义菜单进入，表明其信息已经保存在本地，直接把用户openId放入session中
	    		JSONObject accessCodeJson = WxUserService.getUserOpenIdAndAccessTokenByCode(code); 
	    		if(null != accessCodeJson) {
	    			openId = accessCodeJson.optString("openid");
	    			session.put("openId", openId);
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
