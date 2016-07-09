package controllers.admin;

import java.util.ArrayList;
import java.util.List;

import models.Administrator;
import models.Privilege;
import service.AdminService;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;

import dto.SessionInfo;

public class AdministratorCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;

		SessionInfo sessionInfo = AdminService.getSessionInfo(session.get("sid"));
		if(sessionInfo == null || sessionInfo.getAdmin() == null) {
		    render("/admin/Administrator/list.html", null);
		}
		
		Long count = 0l;
        List<Administrator> administrators = new ArrayList<Administrator>();

        Administrator admin = sessionInfo.getAdmin();
        if(admin.getAdminType().intValue() == 1) {
            count = 1l;
            administrators.add(sessionInfo.getAdmin());
        } else {
            count = Administrator.count();
            administrators = Administrator.all().fetch(page, pageSize);
        }
        
        Pager<Administrator> pageData = new Pager<Administrator>(count.intValue(), page, pageSize);
        pageData.setList(administrators);
        
        render("/admin/Administrator/list.html", pageData, admin);
    }
    
    public static void adminPrivilege() {
        List<Privilege> privileges = Privilege.all().fetch();
        render("/admin/Administrator/adminPrivilege.html", privileges);
    }
}
