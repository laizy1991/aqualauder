package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.RefundOrder;
import service.RefundOrderService;

public class RefundOrderCtrl extends AjaxController {

    public static void add(RefundOrder refundOrder) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(RefundOrder refundOrder) throws Exception {

        renderSuccessJson();
    }

    public static void update(RefundOrder refundOrder) {
        RefundOrderService.update(refundOrder);
        renderSuccessJson();
    }

    
}
