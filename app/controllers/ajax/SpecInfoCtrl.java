package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.SpecInfo;
import service.SpecInfoService;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class SpecInfoCtrl extends AjaxController {
    
    public static void add(SpecInfo info) throws BusinessException {
        SpecInfoService.add(info);
        renderSuccessJson();
    }

    public static void delete(SpecInfo info) throws Exception {

        renderSuccessJson();
    }

    public static void update(SpecInfo info) {
        SpecInfoService.update(info);
        renderSuccessJson();
    }


}

