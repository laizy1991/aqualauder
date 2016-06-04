package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.Distributor;

import java.util.List;

public class DistributorCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = Distributor.count();
        List<Distributor> distributors = Distributor.find("ORDER BY create_time DESC").fetch(page, pageSize);
        
        Pager<Distributor> pageData = new Pager<Distributor>(count.intValue(), page, pageSize);
        pageData.setList(distributors);
        
        render("/admin/Distributor/list.html", pageData);
    }
    
}
