package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.SpecInfo;
import models.SpecType;

import java.util.List;

public class SpecInfoCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = SpecInfo.count();
        List<SpecInfo> specInfos = SpecInfo.all().fetch(page, pageSize);

        Pager<SpecInfo> pageData = new Pager<SpecInfo>(count.intValue(), page, pageSize);
        pageData.setList(specInfos);
        
        List<SpecType> specTypes = SpecType.all().fetch();
        
        render("/admin/SpecInfo/list.html", pageData, specTypes);
    }
}
