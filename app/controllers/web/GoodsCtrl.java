package controllers.web;

import models.User;

import org.apache.commons.lang.StringUtils;

import service.UserService;
import utils.WxUtil;
import common.constants.RegType;
import common.core.WebController;


public class GoodsCtrl extends WebController {

	
	/**
	 * 微信一级菜单 女神新衣
	 * 0-新品 1-整体搭配 2-单品 3-鞋帽 4-饰品
	 * @param tag
	 * @param code
	 */
    public static void list(int tag, String code) {
    	String openId = getOpenId(code);
    	if(StringUtils.isEmpty(openId)) {
    		// TODO 获取用户OpenId失败
    	}
    	
    }
}
