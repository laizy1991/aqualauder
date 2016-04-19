package controllers.admin;

import common.core.WebController;
import models.Goods;
import models.GoodsColor;
import models.GoodsSize;

import java.util.List;

public class GoodsSizeCtrl extends WebController {

    public static void list() {
        List<GoodsSize> sizes = GoodsSize.all().fetch();
        render("/admin/GoodsSize/list.html", sizes);
    }
}
