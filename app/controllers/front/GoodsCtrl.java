package controllers.front;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Goods;
import models.GoodsColor;
import models.GoodsSize;
import models.GoodsStock;
import models.GoodsType;
import models.Notice;
import models.ShippingAddress;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import service.GoodsColorService;
import service.GoodsService;
import service.GoodsSizeService;
import service.wx.common.Configure;
import service.wx.dto.jspai.JsapiConfig;
import service.wx.service.jsapi.JsApiService;
import service.wx.service.user.WxUserService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import common.annotation.GuestAuthorization;
import common.core.FrontController;
import dao.GoodsStockDao;
import dao.GoodsTypeDao;
import dao.NoticeDao;
import dao.ShippingAddressDao;
import dto.NoticePicInfo;


public class GoodsCtrl extends FrontController {
	public static Gson gson = new Gson();
	
	/**
	 * 微信一级菜单 女神新衣
	 * 0-新品 1-裙装 2-整体搭配 3-上装 4-下装 -1-全部
	 */
    @GuestAuthorization
    public static void list(Integer tag, boolean fromPage) {
        if(tag == null || tag < 0) {
            tag = -1;
        }
		List<Goods> goods = GoodsService.list(-1, -1, tag);
		Notice notice = NoticeDao.get();
		List<NoticePicInfo> imgs = new ArrayList<NoticePicInfo>();
		String desc = "";
		if(notice != null) {
		    desc = notice.getContent();
		    imgs = new Gson().fromJson(notice.getPicInfo(), new TypeToken<List<NoticePicInfo>>(){}.getType());
		}
		
		
		List<GoodsType> types = GoodsTypeDao.all();
		
		GoodsType hotType = null;
		for(GoodsType type : types) {
			if(type.getId().intValue() == 1) {
				hotType = type;
				types.remove(type);
				break;
			}
		}
		
		/*String querystring = request.querystring;
    	String protocol = request.secure?"https://":"http://";
    	String action = request.path;
    	String url =  protocol + request.domain + action + "?" + querystring;
    	
    	Logger.info("生成的分享链接为: %s", url);
    	JsapiConfig config = JsApiService.getSign(url);
    	Logger.info("config参数为: %s", gson.toJson(config));*/
    	
		render("/Front/goods/list.html", imgs, goods, desc, types, hotType);
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
        
        //分享
        String querystring = request.querystring;
    	String protocol = request.secure?"https://":"http://";
    	String action = request.path;
    	String url =  protocol + request.domain + action + "?" + querystring;
    	
    	Logger.info("商品详情页生成的分享链接为: %s", url);
    	JsapiConfig config = JsApiService.getSign(url);
    	Logger.info("商品详情页config参数为: %s", gson.toJson(config));
    	
    	BigDecimal b = new BigDecimal(goods.getPrice()/100D);  
		double totalFee = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		String goodsIcon = Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn") +
				"/public/images/getheadimg.jpg"; 
		if(null != goods.getGoodsIcons() && goods.getGoodsIcons().size() > 0) {
			goodsIcon = goods.getGoodsIcons().get(0).getIconUrl();
		}
		
		render("/Front/goods/details.html", goods, stockMapJson, address, sizeList, 
				config, totalFee, goodsIcon);
	}
	
}
