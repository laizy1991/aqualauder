package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.User;

import java.util.List;

public class UserCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = User.count();
        List<User> users = User.all().fetch(page, pageSize);
        
        Pager<User> pageData = new Pager<User>(count.intValue(), page, pageSize);
        pageData.setList(users);
        
        render("/admin/User/list.html", pageData);
    }
    
}
