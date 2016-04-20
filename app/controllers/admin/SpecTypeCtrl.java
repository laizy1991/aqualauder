package controllers.admin;

import common.core.WebController;
import models.SpecType;

import java.util.List;

public class SpecTypeCtrl extends WebController {

    public static void list() {
        List<SpecType> specTypes = SpecType.all().fetch();
        render("/admin/SpecType/list.html", specTypes);
    }
}
