package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.Express;
import models.User;
import models.UserWallet;

import java.util.List;

public class WalletCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = UserWallet.count();
        List<UserWallet> wallets = UserWallet.all().fetch(page, pageSize);
        
        Pager<UserWallet> pageData = new Pager<UserWallet>(count.intValue(), page, pageSize);
        pageData.setList(wallets);
        
        render("/admin/Wallet/list.html", pageData);
    }
    
}
