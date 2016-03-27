package common.core;

import models.Administrator;
import play.mvc.Controller;
import dto.SessionInfo;

/**
 * 接口请求公用逻辑
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public class BaseController extends Controller {
	
	protected static SessionInfo sessionInfo;
	
	protected static Long privilegeFlag = 0L;

	protected static boolean checkRouterPrivilege(SessionInfo sessionInfo) {
		return true;
	}
	
	public static Administrator getAdmin() {
	    return sessionInfo.getAdmin();
	}
}
