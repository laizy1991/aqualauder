package controllers.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Goods;
import models.GoodsColor;
import models.GoodsIcon;
import models.GoodsSize;
import models.GoodsStock;
import models.ShippingAddress;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import service.GoodsColorService;
import service.GoodsService;
import service.GoodsSizeService;
import service.wx.service.user.WxUserService;

import com.google.gson.Gson;
import common.annotation.GuestAuthorization;
import common.core.FrontController;

import dao.GoodsStockDao;
import dao.ShippingAddressDao;


public class GoodsCtrl extends FrontController {

	
	/**
	 * 微信一级菜单 女神新衣
	 * 0-新品 1-整体搭配 2-单品 3-鞋帽 4-饰品
	 */
    @GuestAuthorization
    public static void list(Integer type) {
        if(type == null || type < 0) {
            type = -1;
        } else {
            type ++;
        }
		List<Goods> goods = GoodsService.list(-1, -1, type);
		Goods activity = GoodsService.get(1l);
		List<String> imgs = new ArrayList<String>();
		String desc = "";
		if(activity != null) {
		    for(GoodsIcon gi : activity.getGoodsIcons()) {
		        imgs.add(gi.getIconUrl());
		    }
		    desc = activity.getGoodsDesc();
		}
		render("/Front/goods/list.html", imgs, goods, desc);
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
        
        List<GoodsStock> stock = GoodsStockDao.getByGoods(goods.getId());
        Map<String, Map<String, Integer>> stockMap = new HashMap<String, Map<String, Integer>>();
        Map<Long, String> colors = new HashMap<Long, String>();
        Map<Long, String> sizes = new HashMap<Long, String>();
        List<String> sizeList = new ArrayList<String>();
        for(GoodsStock gs : stock) {
            if(gs.getAmount() <= 0) {
                continue;
            }
            String size = "";
            String color = "";
            if(sizes.containsKey(new Long(gs.getGoodsSize()))) {
                size = sizes.get(new Long(gs.getGoodsSize()));
            } else {
                GoodsSize goodsSize = GoodsSizeService.get(gs.getGoodsSize());
                if(goodsSize == null) {
                    Logger.error("size not found, id:%s", gs.getGoodsSize());
                    continue;
                }
                sizes.put(goodsSize.getId(), goodsSize.getName());
                size = goodsSize.getName();
                sizeList.add(size);
            }
            if(colors.containsKey(new Long(gs.getGoodsColor()))) {
                color = colors.get(new Long(gs.getGoodsColor()));
            } else {
                GoodsColor goodsColor = GoodsColorService.get(gs.getGoodsColor());
                if(goodsColor == null) {
                    Logger.error("color not found, id:%s", gs.getGoodsColor());
                    continue;
                }
                colors.put(goodsColor.getId(), goodsColor.getName());
                color = goodsColor.getName();
            }
            
            Map<String, Integer> map = stockMap.get(size);
            if(map == null) {
                map = new HashMap<String, Integer>();
                stockMap.put(size, map);
            }
            map.put(color, gs.getAmount());
        }
        String stockMapJson = new Gson().toJson(stockMap);
		render("/Front/goods/details.html", goods, stockMapJson, address, sizeList);
	}
	
}
