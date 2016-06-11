package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Order;
import models.OrderGoods;
import service.OrderService;

public class OrderCtrl extends AjaxController {

    public static void add(Order order) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(Order order) throws Exception {

        renderSuccessJson();
    }

    public static void update(Order order, OrderGoods orderGoods) {
        OrderService.update(order);
        OrderService.updateOrderGoods(orderGoods);
        renderSuccessJson();
    }

    
}
