package controllers.admin;

import java.util.List;

import models.Administrator;
import common.core.WebController;

public class AdministratorCtrl extends WebController {

    public static void list() {
        List<Administrator> admins = Administrator.all().fetch();
        render("/admin/Administrator/list.html", admins);
    }
    
}
