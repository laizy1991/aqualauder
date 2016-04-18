package controllers.admin;

import common.constants.CommonDictKey;
import common.constants.CommonDictType;
import common.core.WebController;
import models.CommonDict;
import models.User;

import javax.persistence.Column;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class CommonDictCtrl extends WebController {

    public static void list() {
        List<CommonDict> commonDicts = CommonDict.all().fetch();
        renderArgs.put("config", CommonDictType.CONFIG);
        renderArgs.put("levelPay", CommonDictType.LEVEL_PAY);
        render("/admin/CommonDict/list.html", commonDicts);
    }
    
}
