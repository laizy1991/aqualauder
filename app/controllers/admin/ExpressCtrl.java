package controllers.admin;

import common.core.WebController;
import models.Express;

import java.util.List;

public class ExpressCtrl extends WebController {

    public static void list() {
        List<Express> expresses = Express.all().fetch();
        render("/admin/Express/list.html", expresses);
    }
    
}
