package controllers.front;

import common.annotation.GuestAuthorization;
import common.core.WebController;
import models.Goods;
import service.GoodsService;

import java.util.List;

public class Helps extends WebController {
    @GuestAuthorization
    public static void explain() {
        render("/Front/help/explain.html");
    }
    @GuestAuthorization
    public static void introduce() {
        render("/Front/help/introduce.html");
    }
    @GuestAuthorization
    public static void link() {
        render("/Front/help/link.html");
    }

    @GuestAuthorization
    public static void list() {
        List<Goods> goods = GoodsService.list(-1, -1, 0);
        render("/Front/goods/list.html", goods);
    }

}
