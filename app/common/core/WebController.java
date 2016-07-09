package common.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import models.RolePrivilege;
import play.Logger;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Http.Request;
import play.mvc.Scope.Session;
import service.AdminService;
import common.annotation.GuestAuthorization;
import controllers.Application;
import dao.RolePrivilegeDao;
import exception.BusinessException;

/**
 * 
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public class WebController extends BaseController {
	protected static String loginTpl = "login.html";
	
	@Before
	protected static void beforeAction() throws SecurityException, NoSuchMethodException {
		
		Logger.info("Action - %s", Request.current().path);
		
		initReqSource();
		
		if(getActionAnnotation(GuestAuthorization.class) != null) {
			return;
		}
		
		String sessionId = Session.current().get("sid");
		sessionInfo = AdminService.getSessionInfo(sessionId);
		
		if(sessionInfo != null) {
			if(!checkRouterPrivilege(sessionInfo)) {
				Application.index();
			}
			initParams();
			return;
		}
		
		Application.login();
	}
	
    private static void initReqSource() {
        renderArgs.put("pageTitle", "管理平台");
        renderArgs.put("sysType", 0);
    }
	
	/**
	 * 初始化全局参数
	 * 
	 */
	private static void initParams() {
	    List<RolePrivilege> rps = RolePrivilegeDao.getByRole(1);
	    List<Integer> adminPrivileges = new ArrayList<Integer>();
	    if(CollectionUtils.isNotEmpty(rps)) {
	        for(RolePrivilege rp : rps) {
	            adminPrivileges.add(rp.getPrivilegeId());
	        }
	    }
	    
	    renderArgs.put("adminPrivileges", adminPrivileges);
		renderArgs.put("sid", sessionInfo.getSessionId());
		renderArgs.put("admin", sessionInfo.getAdmin());
		renderArgs.put("username", sessionInfo.getAdmin().getUsername());
		renderArgs.put("privilegeFlag", privilegeFlag);
		renderArgs.put("ctrl", request.controller);
		renderArgs.put("action", request.action);

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
	
}
