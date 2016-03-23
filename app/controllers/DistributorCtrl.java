package controllers;

import common.core.WebController;
import models.Distributor;

import java.util.List;

public class DistributorCtrl extends WebController {

    public static void list() {
        List<Distributor> distributors = Distributor.all().fetch();
        render("/Distributor/list.html", distributors);
    }
    
}
