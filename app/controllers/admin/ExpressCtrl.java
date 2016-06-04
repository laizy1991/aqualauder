package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.Express;

import java.util.List;

public class ExpressCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = Express.count();
        List<Express> expresses = Express.all().fetch(page, pageSize);

        Pager<Express> pageData = new Pager<Express>(count.intValue(), page, pageSize);
        pageData.setList(expresses);
        
        render("/admin/Express/list.html", pageData);
    }
    
}
