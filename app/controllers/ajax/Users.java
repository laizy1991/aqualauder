package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.User;
import models.User;
import utils.StringUtil;

import java.util.List;

public class Users extends AjaxController {

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
