package controllers.admin;

import java.util.List;

import models.GoodsType;

import common.core.WebController;

import dao.GoodsTypeDao;

public class GoodsTypeCtrl extends WebController {

    public static void list() {
    	List<GoodsType> infos = GoodsTypeDao.all();
        render("/admin/GoodsType/list.html", infos);
    }
}
