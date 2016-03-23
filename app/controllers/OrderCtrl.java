package controllers;

import common.core.WebController;
import models.Order;

import java.util.List;

public class OrderCtrl extends WebController {

    public static void list() {
        List<Order> Orders = Order.all().fetch();
        render("/Order/list.html", Orders);
    }
    
}
