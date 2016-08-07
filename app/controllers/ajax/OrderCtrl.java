package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Express;
import models.Order;
import models.OrderGoods;
import service.OrderService;
import service.SellerService;
import service.WxMsgService;

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

    public static void delivered(Order order, Express express) {
        if(order != null && express != null && SellerService.delivered(order.getId(), express.getId(), order.getExpressNum())){
        	WxMsgService.sendDeliveredResultMsg(order.getId());
            renderSuccessJson();
        } else {
            renderErrorJson("发货信息修改失败");
        }
    }

    
}
