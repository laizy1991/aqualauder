package controllers;

import java.util.List;

import models.Administrator;
import common.core.WebController;

public class Admins extends WebController {

    public static void list() {
        List<Administrator> admins = Administrator.all().fetch();
        render("/Admin/list.html", admins);
    }
    
}
