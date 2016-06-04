package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.Goods;
import models.GoodsColor;
import models.GoodsSize;

import java.util.List;

public class GoodsColorCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = GoodsColor.count();
		List<GoodsColor> colors = GoodsColor.all().fetch(page, pageSize);

        Pager<GoodsColor> pageData = new Pager<GoodsColor>(count.intValue(), page, pageSize);
        pageData.setList(colors);
        
        render("/admin/GoodsColor/list.html", pageData);
    }
}
