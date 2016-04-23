package service;

import java.util.HashMap;
import java.util.Map;

import models.Order;

import org.junit.Test;

import play.test.UnitTest;

import com.google.gson.Gson;

import dto.OrderDetail;

public class OrderTest extends UnitTest {
    private static final Gson gson = new Gson();
    //下订单
    @Test
    public void addOrder() {
        Order order= new Order();
        order.setMobilePhone("15989104721");
        order.setOpenId("olVhYv2ogEVJYgaRhEpIh83NZh5c");
        order.setOrderMemo("快点发货");
        order.setReceiver("赖泽原");
        order.setShippingAddress("广东省广州市天河区");
        Map<Long, Integer> goodsNum = new HashMap<Long, Integer>();
        goodsNum.put(4l, 2);
        goodsNum.put(5l, 3);
        OrderDetail detail = BuyerService.createOrder(3, order, goodsNum);
        System.err.println(gson.toJson(detail));
    }
    
}
