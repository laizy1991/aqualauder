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
import service.wx.service.user.WxUserService;
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
	    session.put("openId", "olVhYv0N4I24GhgF7dyf9mBm9wgE");
		Logger.info("Action: %s, QueryString: %s", Request.current().path, Request.current().querystring);
		String code = request.params.get("code");
		if(!StringUtils.isBlank(code)) {
			Logger.info("code: %s", code);
	    	String openId = session.get("openId");
	    	if(StringUtils.isBlank(openId)) {
	    		openId = WxUserService.getUserOpenIdByCode(code);
	    		if(!StringUtils.isBlank(openId)) {
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
}
