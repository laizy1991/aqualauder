package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.CommonDict;

public class CommonDictCtrl extends AjaxController {

    public static void add(CommonDict commonDict) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(CommonDict commonDict) throws Exception {

        renderSuccessJson();
    }

    public static void update(CommonDict commonDict) {

        renderSuccessJson();
    }

    
}
