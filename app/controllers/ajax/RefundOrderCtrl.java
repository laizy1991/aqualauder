package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.RefundOrder;
import play.Logger;
import service.RefundOrderService;
import service.SellerService;

public class RefundOrderCtrl extends AjaxController {

    public static void add(RefundOrder refundOrder) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(RefundOrder refundOrder) throws Exception {

        renderSuccessJson();
    }

    public static void update(RefundOrder refundOrder, int isAgree) {
        if(refundOrder.getId() != null && (isAgree == 1 || isAgree == 2)) {
            try{
                if(SellerService.refundAudit(refundOrder.getId(), isAgree, refundOrder.getSellerMemo())) {
                    renderSuccessJson();
                    return;
                }
            } catch (Exception e) {
                Logger.error("refund audit fail. id:%s", refundOrder.getOrderId(), e.getMessage());
            }
            renderErrorJson("处理退款单失败");
        }
    }

    
}
