package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.Express;
import models.GoodsColor;
import models.GoodsSize;
import models.Order;
import net.sf.json.JSON;
import net.sf.json.util.JSONUtils;

import java.util.List;

public class OrderCtrl extends WebController {

    public static void list(int page, String orderBy, String key, int state) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = Order.count();
        List<Order> orders = Order.all().fetch(page, pageSize);
        
        Pager<Order> pageData = new Pager<Order>(count.intValue(), page, pageSize);
        pageData.setList(orders);
        
        List<Express> expresses = Express.all().fetch();
        List<GoodsColor> colors = GoodsColor.all().fetch();
        List<GoodsSize> sizes = GoodsSize.all().fetch();
        renderArgs.put("expresses", expresses);
        renderArgs.put("colors", colors);
        renderArgs.put("sizes", sizes);

        render("/admin/Order/list.html", pageData);
    }
    
}
