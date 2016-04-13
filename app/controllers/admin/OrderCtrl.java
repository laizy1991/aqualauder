package controllers.admin;

import common.core.WebController;
import models.Order;

import java.util.List;

public class OrderCtrl extends WebController {

    public static void list() {
        List<Order> orders = Order.all().fetch();
        render("/admin/Order/list.html", orders);
    }
    
}
