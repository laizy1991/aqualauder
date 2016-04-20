package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.SpecType;
import service.SpecTypeService;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class SpecTypeCtrl extends AjaxController {
    
    public static void add(SpecType type) throws BusinessException {
        SpecTypeService.add(type);
        renderSuccessJson();
    }

    public static void delete(SpecType type) throws Exception {

        renderSuccessJson();
    }

    public static void update(SpecType type) {
        SpecTypeService.update(type);
        renderSuccessJson();
    }


}

