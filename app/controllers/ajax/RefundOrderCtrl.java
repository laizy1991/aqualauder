package controllers.ajax;

import play.db.jpa.JPA;
import models.RefundOrder;
import service.SellerService;
import common.core.AjaxController;
import exception.BusinessException;

public class RefundOrderCtrl extends AjaxController {

    public static void add(RefundOrder refundOrder) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(RefundOrder refundOrder) throws Exception {

        renderSuccessJson();
    }

    public static void update(RefundOrder refundOrder, int isAgree) {
    	try {
    		if(refundOrder.getId() != null && (isAgree == 1 || isAgree == 2)) {
    			if(SellerService.refundAudit(refundOrder.getId(), isAgree, refundOrder.getSellerMemo())) {
    				renderSuccessJson();
    				return;
    			}
    		}
    	} catch(Exception e) {
    		JPA.setRollbackOnly();
    		e.printStackTrace();
    		if(e instanceof BusinessException) {
    			renderErrorJson(e.getMessage());
    		}
    	}
        renderErrorJson("退款失败，服务器异常");
    }

    
}
