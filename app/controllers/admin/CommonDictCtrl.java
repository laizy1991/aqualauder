package controllers.admin;

import common.constants.CommonDictType;
import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.CommonDict;

import java.util.List;

public class CommonDictCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = CommonDict.count();
        List<CommonDict> commonDicts = CommonDict.all().fetch(page, pageSize);
        
        Pager<CommonDict> pageData = new Pager<CommonDict>(count.intValue(), page, pageSize);
        pageData.setList(commonDicts);
        
        renderArgs.put("config", CommonDictType.CONFIG);
        renderArgs.put("levelPay", CommonDictType.LEVEL_PAY);
        render("/admin/CommonDict/list.html", pageData);
    }
    
}
