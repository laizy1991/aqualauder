package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.User;
import models.UserWallet;
import service.UserWalletService;

public class WalletCtrl extends AjaxController {

    public static void add(UserWallet wallet) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(UserWallet wallet) throws Exception {

        renderSuccessJson();
    }

    public static void update(UserWallet wallet) {
        UserWalletService.update(wallet);
        renderSuccessJson();
    }

    
}
