package controllers.admin;

import common.core.WebController;
import models.User;
import models.UserWallet;

import java.util.List;

public class WalletCtrl extends WebController {

    public static void list() {
        List<User> wallets = UserWallet.all().fetch();
        render("/admin/Wallet/list.html", wallets);
    }
    
}
