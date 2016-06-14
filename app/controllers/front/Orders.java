package controllers.front;

import java.util.HashMap;
import java.util.Map;

import models.Order;
import models.User;

import org.apache.commons.lang.StringUtils;

import service.BuyerService;
import service.ShippingAddressService;
import service.wx.service.user.WxUserService;

import common.core.FrontController;

import dto.GoodsBrief;
import dto.OrderDetail;


public class Orders extends FrontController {

	public static void createOrder(Order order, long goodsId, int goodsNum, String goodsSize, String goodsColor) {
	    String openId = session.get("openId");
        User user = null; 
        if(!StringUtils.isBlank(openId)) {
            user = WxUserService.getUserInfo(openId);
        }
        if(null == user) {
            renderJSON("{\"msg\":\"创建订单失败\"}");
        }
        
        Map<GoodsBrief, Integer> goodsNumMap = new HashMap<GoodsBrief, Integer>();
        GoodsBrief gb = new GoodsBrief();
        gb.setGoodsId(goodsId);
        gb.setGoodsColor(goodsColor);
        gb.setGoodsSize(goodsSize);
        goodsNumMap.put(gb, goodsNum);
        OrderDetail detail = BuyerService.createOrder(user.getUserId(), order, goodsNumMap, openId);
        if(detail == null) {
            renderJSON("{\"msg\":\"创建订单失败\"}");
        }
	    try {
	        ShippingAddressService.create(user.getUserId(), order.getShippingAddress(), order.getReceiver(), order.getMobilePhone(), order.getWeixin(), 0);
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    redirect("front.Pay.wxPay", detail.getOrderId());
	}
}
