package controllers.front;

import common.annotation.GuestAuthorization;
import common.constants.GoodsTag;
import common.core.FrontController;
import models.Goods;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import service.GoodsService;
import service.wx.service.user.WxUserService;

import java.util.List;


public class GoodsCtrl extends FrontController {

	
	/**
	 * 微信一级菜单 女神新衣
	 * 0-新品 1-整体搭配 2-单品 3-鞋帽 4-饰品
	 * @param tag
	 * @param code
	 */
    @GuestAuthorization
    public static void list(int tag, String code) {
    	// TODO 从入口处拿取session中的openId，没有的话用code去换，换回来后放入session中
    	Logger.info("code: %s", code);
    	String openId = session.get("openId");
    	if(StringUtils.isBlank(openId)) {
    		openId = WxUserService.getUserOpenIdByCode(code);
    	}
    	switch(tag) {
			case GoodsTag.NEW:
				Logger.info("name: 新品, tag: %d", tag);
				break;
			case GoodsTag.DRESS:
				Logger.info("name: 裙装, tag: %d", tag);
				break;
			case GoodsTag.SUIT:
				Logger.info("name: 整体搭配, tag: %d", tag);
				break;
			case GoodsTag.TOPS:
				Logger.info("name: 上装, tag: %d", tag);
				break;
			case GoodsTag.BOTTOMS:
				Logger.info("name: 下装, tag: %d", tag);
				break;
			default:

    	}

		List<Goods> goods = GoodsService.list(-1, -1, 0);
		render("/Front/goods/list.html", goods);
    }
}
