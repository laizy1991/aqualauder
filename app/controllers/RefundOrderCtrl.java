package controllers;

import common.core.WebController;
import models.Administrator;

import java.util.List;

public class RefundOrderCtrl extends WebController {

    public static void list() {
        List<Administrator> admins = Administrator.all().fetch();
        render("/RefundOrder/list.html", admins);
    }
    
}
