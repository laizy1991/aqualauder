package controllers.admin;

import common.core.WebController;
import models.RefundOrder;

import java.util.List;

public class RefundOrderCtrl extends WebController {

    public static void list() {
        List<RefundOrder> refundOrders = RefundOrder.all().fetch();
        render("/admin/RefundOrder/list.html", refundOrders);
    }
    
}
