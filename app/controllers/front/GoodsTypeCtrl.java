package controllers.front;

import java.util.List;

import play.mvc.Controller;
import models.GoodsType;
import common.annotation.GuestAuthorization;
import dao.GoodsTypeDao;

public class GoodsTypeCtrl extends Controller {
	
	/**
	 * 索引列表
	 * @param tag
	 * @param fromPage
	 */
	@GuestAuthorization
	public static void list() {
	    List<GoodsType> types = GoodsTypeDao.all();

        render("/Front/goods/types.html", types);
	}
}
