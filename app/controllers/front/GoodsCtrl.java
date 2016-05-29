package controllers.front;

import java.util.List;

import models.Goods;
import models.ShippingAddress;
import models.User;

import org.apache.commons.lang.StringUtils;

import service.GoodsService;
import service.wx.service.user.WxUserService;

import common.annotation.GuestAuthorization;
import common.core.FrontController;

import dao.ShippingAddressDao;


public class GoodsCtrl extends FrontController {

	
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
		ShippingAddress address = null;
		String openId = session.get("openId");
        if(StringUtils.isNotBlank(openId)) {
            User user = WxUserService.getUserInfo(openId);
            if(user != null) {
                address = ShippingAddressDao.getByUserId(user.getUserId());
            }
        }
		render("/Front/goods/details.html", goods, address);
	}
}
