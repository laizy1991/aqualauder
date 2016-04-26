package controllers.front;

import org.apache.commons.lang.StringUtils;

import service.wx.service.user.WxUserService;

import common.constants.GoodsTag;
import common.core.FrontController;


public class GoodsCtrl extends FrontController {

	
	/**
	 * 微信一级菜单 女神新衣
	 * 0-新品 1-整体搭配 2-单品 3-鞋帽 4-饰品
	 * @param tag
	 * @param code
	 */
    public static void list(int tag, String code) {
    	String openId = WxUserService.getUserOpenId(code);
    	if(StringUtils.isEmpty(openId)) {
    		// TODO 获取用户OpenId失败
    	}
    	switch(tag) {
			case GoodsTag.NEW: 
				break;
			case GoodsTag.SUIT: 
				break;
			case GoodsTag.SINGLE: 
				break;
			case GoodsTag.SHOE_HAT: 
				break;
			case GoodsTag.ACCESSORY: 
				break;
			default:
				
    	}
    }
}
