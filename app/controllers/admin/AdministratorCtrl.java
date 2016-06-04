package controllers.admin;

import java.util.List;

import models.Administrator;
import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;

public class AdministratorCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = Administrator.count();
        List<Administrator> administrators = Administrator.all().fetch(page, pageSize);
        
        Pager<Administrator> pageData = new Pager<Administrator>(count.intValue(), page, pageSize);
        pageData.setList(administrators);
        
        render("/admin/Administrator/list.html", pageData);
    }
    
}
