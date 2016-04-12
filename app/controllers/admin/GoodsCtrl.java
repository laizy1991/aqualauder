package controllers.admin;

import common.core.WebController;
import models.Goods;

import java.util.List;

public class GoodsCtrl extends WebController {

    public static void list() {
        List<Goods> goodses = Goods.all().fetch();
        render("/admin/Goods/list.html", goodses);
    }
    
}
