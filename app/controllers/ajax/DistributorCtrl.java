package controllers.ajax;

import models.Distributor;
import service.DistributorService;

import common.core.AjaxController;

import dto.DistributorDetail;
import exception.BusinessException;

public class DistributorCtrl extends AjaxController {

    public static void add(Distributor distributor) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(Distributor distributor) throws Exception {

        renderSuccessJson();
    }

    public static void update(Distributor distributor) {
        DistributorService.update(distributor);
        renderSuccessJson();
    }

    public static void distributorDetail(int userId) {
        DistributorDetail detail = DistributorService.distributorDetail(userId);
        renderJSON(detail);
    }
}
