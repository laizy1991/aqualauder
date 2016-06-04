package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.SpecType;

import java.util.List;

public class SpecTypeCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = SpecType.count();
        List<SpecType> specTypes = SpecType.all().fetch(page, pageSize);

        Pager<SpecType> pageData = new Pager<SpecType>(count.intValue(), page, pageSize);
        pageData.setList(specTypes);
        
        render("/admin/SpecType/list.html", pageData);
    }
}
