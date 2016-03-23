package controllers;

import common.core.WebController;
import models.Administrator;

import java.util.List;

public class ExpressCtrl extends WebController {

    public static void list() {
        List<Administrator> admins = Administrator.all().fetch();
        render("/Express/list.html", admins);
    }
    
}
