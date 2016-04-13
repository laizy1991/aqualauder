package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.User;

public class UserCtrl extends AjaxController {

    public static void add(User user) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(User user) throws Exception {

        renderSuccessJson();
    }

    public static void update(User user) {

        renderSuccessJson();
    }

    
}
