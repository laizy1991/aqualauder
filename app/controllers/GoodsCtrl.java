package controllers;

import common.core.WebController;
import models.Administrator;

import java.util.List;

public class GoodsCtrl extends WebController {

    public static void list() {
        List<Administrator> admins = Administrator.all().fetch();
        render("/Goods/list.html", admins);
    }
    
}
