package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.GoodsSize;
import service.GoodsSizeService;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class GoodsSizeCtrl extends AjaxController {
    
    public static void add(GoodsSize size) throws BusinessException {
        GoodsSizeService.add(size);
        renderSuccessJson();
    }

    public static void delete(GoodsSize size) throws Exception {

        renderSuccessJson();
    }

    public static void update(GoodsSize size) {
        GoodsSizeService.update(size);
        renderSuccessJson();
    }


}

