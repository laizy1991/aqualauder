package controllers.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Order;
import models.User;

import org.apache.commons.lang.StringUtils;

import service.BuyerService;
import service.OrderService;
import service.ShippingAddressService;
import service.wx.service.user.WxUserService;

import common.core.FrontController;

import dto.OrderDetail;


public class Orders extends FrontController {

	public static void createOrder(Order order, long goodsId, int goodsNum, String goodsSize) {
	    String openId = session.get("openId");
        User user = null; 
        if(!StringUtils.isBlank(openId)) {
            user = WxUserService.getUserInfo(openId);
        }
        if(null == user) {
            renderJSON("{\"msg\":\"创建订单失败\"}");
        }
        
        Map<Long, Integer> goodsNumMap = new HashMap<Long, Integer>();
        goodsNumMap.put(goodsId, goodsNum);
        OrderDetail detail = BuyerService.createOrder(user.getUserId(), order, goodsNumMap, goodsSize);
        if(detail == null) {
            renderJSON("{\"msg\":\"创建订单失败\"}");
        }
	    try {
	        ShippingAddressService.create(user.getRegType(), order.getShippingAddress(), order.getReceiver(), order.getMobilePhone(), 0);
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    redirect("front.Pay.wxPay", detail.getOrderId());
	}
}
