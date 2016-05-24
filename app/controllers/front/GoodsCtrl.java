package controllers.front;

import common.annotation.GuestAuthorization;
import common.constants.GoodsTag;
import common.core.WebController;
import models.Goods;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import service.GoodsService;
import service.wx.service.user.WxUserService;

import java.util.List;


public class GoodsCtrl extends WebController {

	
	/**
	 * 微信一级菜单 女神新衣
	 * 0-新品 1-整体搭配 2-单品 3-鞋帽 4-饰品
	 */
    @GuestAuthorization
    public static void list() {
		List<Goods> goods = GoodsService.list(-1, -1, 0);
		render("/Front/goods/list.html", goods);
    }

	@GuestAuthorization
	public static void details(int id) {
		Goods goods = GoodsService.get(id);
		render("/Front/goods/details.html", goods);
	}
}
