package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.Goods;
import models.GoodsColor;
import models.GoodsSize;

import java.util.List;

public class GoodsSizeCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
        Long count = GoodsSize.count();
        List<GoodsSize> sizes = GoodsSize.all().fetch(page, pageSize);

        Pager<GoodsSize> pageData = new Pager<GoodsSize>(count.intValue(), page, pageSize);
        pageData.setList(sizes);
        
        render("/admin/GoodsSize/list.html", pageData);
    }
}
