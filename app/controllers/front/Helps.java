package controllers.front;

import java.util.List;

import models.Goods;
import service.GoodsService;

import common.annotation.GuestAuthorization;
import common.core.FrontController;

public class Helps extends FrontController {
    public static void explain() {
        render("/Front/help/explain.html");
    }

    public static void introduce() {
        render("/Front/help/introduce.html");
    }

    public static void link() {
        render("/Front/help/link.html");
    }

    public static void list() {
        List<Goods> goods = GoodsService.list(-1, -1, 0);
        render("/Front/goods/list.html", goods);
    }

}
