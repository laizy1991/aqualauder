package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.RefundOrder;

import java.util.List;

public class RefundOrderCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = RefundOrder.count();
        List<RefundOrder> refundOrders = RefundOrder.find("ORDER BY id DESc").fetch(page, pageSize);
        
        Pager<RefundOrder> pageData = new Pager<RefundOrder>(count.intValue(), page, pageSize);
        pageData.setList(refundOrders);
        
        render("/admin/RefundOrder/list.html", pageData);
    }
    
}
