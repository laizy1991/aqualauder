package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Express;
import models.Express;
import service.AdminService;
import service.ExpressService;
import utils.StringUtil;

public class ExpressCtrl extends AjaxController {

    public static void add(Express express) throws BusinessException {
        if (express == null
                || StringUtil.isNullOrEmpty(express.getName())) {
            throw new BusinessException("Lack of information");
        }

        ExpressService.add(express);
        renderSuccessJson();
    }

    public static void delete(Express express) throws Exception {
        ExpressService.delete(express);
        renderSuccessJson();
    }

    public static void update(Express express) throws BusinessException {
        if (express == null
                || StringUtil.isNullOrEmpty(express.getName())) {
            throw new BusinessException("Lack of information");
        }

        if(express != null) {
            ExpressService.update(express);
        }
        renderSuccessJson();
    }

}
