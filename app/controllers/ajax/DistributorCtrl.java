package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Distributor;

public class DistributorCtrl extends AjaxController {

    public static void add(Distributor distributor) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(Distributor distributor) throws Exception {

        renderSuccessJson();
    }

    public static void update(Distributor distributor) {

        renderSuccessJson();
    }

}
