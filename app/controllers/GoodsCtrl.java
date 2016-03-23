package controllers;

import common.core.WebController;
import models.Goods;

import java.util.List;

public class GoodsCtrl extends WebController {

    public static void list() {
        List<Goods> goodses = Goods.all().fetch();
        render("/Goods/list.html", goodses);
    }
    
}
