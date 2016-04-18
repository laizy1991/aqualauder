package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Goods;

public class GoodsCtrl extends AjaxController {

    public static void add(Goods goods) throws BusinessException {

        renderSuccessJson();
    }

    public static void delete(Goods goods) throws Exception {

        renderSuccessJson();
    }

    public static void update(Goods goods) {

        renderSuccessJson();
    }

    
}
