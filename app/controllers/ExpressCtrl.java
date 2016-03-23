package controllers;

import common.core.WebController;
import models.Express;

import java.util.List;

public class ExpressCtrl extends WebController {

    public static void list() {
        List<Express> expresses = Express.all().fetch();
        render("/Express/list.html", expresses);
    }
    
}
