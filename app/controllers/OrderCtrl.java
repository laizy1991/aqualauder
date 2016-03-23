package controllers;

import common.core.WebController;
import models.Administrator;

import java.util.List;

public class OrderCtrl extends WebController {

    public static void list() {
        List<Administrator> admins = Administrator.all().fetch();
        render("/Order/list.html", admins);
    }
    
}
