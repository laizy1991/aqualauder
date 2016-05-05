package controllers.admin;

import common.core.WebController;
import models.Goods;
import models.GoodsColor;
import models.GoodsSize;

import java.util.List;

public class GoodsCtrl extends WebController {

    public static void list() {
        List<Goods> goodses = Goods.all().fetch();
        render("/admin/Goods/list.html", goodses);
    }

    public static void add() {
        List<GoodsColor> goodsColors = GoodsColor.all().fetch();
        List<GoodsSize> goodsSizes = GoodsSize.all().fetch();
        render("/admin/Goods/add.html", goodsColors, goodsSizes);
    }

    public static void update(Goods goods) {
        render("/admin/Goods/update.html", goods);
    }
}
