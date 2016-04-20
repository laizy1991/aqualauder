package controllers.admin;

import common.core.WebController;
import models.SpecInfo;
import models.SpecType;

import java.util.List;

public class SpecInfoCtrl extends WebController {

    public static void list() {
        List<SpecInfo> specInfos = SpecInfo.all().fetch();
        List<SpecType> specTypes = SpecType.all().fetch();
        render("/admin/SpecInfo/list.html", specInfos, specTypes);
    }
}
