package controllers.ajax;

import common.core.AjaxController;
import models.CashInfo;
import play.Logger;
import service.CashInfoService;
import service.SellerService;

public class CashInfoCtrl extends AjaxController {

    public static void update(CashInfo cashInfo, int isAgree) {
        if(cashInfo.getId() != null && (isAgree == 1 || isAgree == 2)) {
            try{
                if(CashInfoService.cashAudit(cashInfo, isAgree==1)) {
                    renderSuccessJson();
                    return;
                }
            } catch (Exception e) {
                Logger.error("cashInfo audit fail. id:%s", cashInfo.getId(), e.getMessage());
            }
            renderErrorJson("处理提现申请失败");
        }
    }

    
}
