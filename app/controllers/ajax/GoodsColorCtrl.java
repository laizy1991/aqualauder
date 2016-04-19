package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.GoodsColor;
import service.GoodsColorService;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class GoodsColorCtrl  extends AjaxController {
    
    public static void add(GoodsColor color) throws BusinessException {
        GoodsColorService.add(color);
        renderSuccessJson();
    }

    public static void delete(GoodsColor color) throws Exception {

        renderSuccessJson();
    }

    public static void update(GoodsColor color) {
        GoodsColorService.update(color);
        renderSuccessJson();
    }


}

