package controllers.admin;

import common.core.WebController;
import models.Goods;
import models.GoodsColor;
import models.GoodsSize;

import java.util.List;

public class GoodsColorCtrl extends WebController {

    public static void list() {
        List<GoodsColor> colors = GoodsColor.all().fetch();
        render("/admin/GoodsColor/list.html", colors);
    }
}
