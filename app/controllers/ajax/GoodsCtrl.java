package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Goods;
import models.GoodsIcon;
import models.GoodsStock;
import play.mvc.Scope;
import service.GoodsService;

import java.util.List;


public class GoodsCtrl extends AjaxController {

    public static void add(Goods goods, List<GoodsStock> goodsStock, List<GoodsIcon> goodsIcon) throws BusinessException {
        goods.setGoodsType(0);
        GoodsService.add(goods, goodsStock, goodsIcon);
        renderSuccessJson();
    }

    public static void delete(Goods goods) throws Exception {

        renderSuccessJson();
    }

    public static void update(Goods goods) {
        GoodsService.update(goods);
        renderSuccessJson();
    }

    public static void uploadIcon() {
    }


}
