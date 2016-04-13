package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Order;

public class OrderCtrl extends AjaxController {

    public static void add(Order order) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(Order order) throws Exception {

        renderSuccessJson();
    }

    public static void update(Order order) {

        renderSuccessJson();
    }

    
}
