package controllers.admin;

import common.core.WebController;
import models.CommonDict;
import models.User;

import java.util.List;

public class CommonDictCtrl extends WebController {

    public static void list() {
        List<CommonDict> commonDicts = CommonDict.all().fetch();
        render("/admin/CommonDict/list.html", commonDicts);
    }
    
}
