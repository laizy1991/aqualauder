package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Order;

import org.junit.Test;

import play.test.UnitTest;

import com.google.gson.Gson;

import dto.OrderDetail;
import exception.BusinessException;

public class OrderTest extends UnitTest {
    private static final Gson gson = new Gson();
    //下订单
    @Test
    public void test() {
//        addOrder();
//        pay();
//       list();
//        delivered();
//        receiving();
//        compele();
//        refundApply();
        refundCancel();
//        refundAudit();
    }
    
    public void addOrder() {
        Order order= new Order();
        order.setMobilePhone("15989104721");
        order.setOpenId("olVhYv2ogEVJYgaRhEpIh83NZh5c");
        order.setOrderMemo("快点发货");
        order.setReceiver("赖泽原");
        order.setShippingAddress("广东省广州市天河区");
        Map<Long, Integer> goodsNum = new HashMap<Long, Integer>();
        goodsNum.put(4l, 1);
        goodsNum.put(5l, 1);
//        OrderDetail detail = BuyerService.createOrder(3, order, goodsNum, "M");
//        System.err.println(gson.toJson(detail));
    }
    
    public void pay() {
        try {
            boolean isSucc = PayService.balancePay(1461429647439000000l);
            System.err.println(isSucc);
            isSucc = PayService.balancePay(1461465779439000000l);
            System.err.println(isSucc);
            isSucc = PayService.balancePay(1461467793439000000l);
            System.err.println(isSucc);
            isSucc = PayService.balancePay(1461467883439000000l);
            System.err.println(isSucc);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }
    
    public void list() {
        List<OrderDetail> details = OrderService.listOrder(3, 1, 10);
        System.err.println(gson.toJson(details));
    }
    
    public void delivered() {
        boolean isSucc = SellerService.delivered(1461429647439000000l, 3, "22222222222222222222");
        System.err.println(isSucc);
        isSucc = SellerService.delivered(1461465779439000000l, 3, "22222222222222222222");
        System.err.println(isSucc);
        isSucc = SellerService.delivered(1461467793439000000l, 3, "22222222222222222222");
        System.err.println(isSucc);
        isSucc = SellerService.delivered(1461467883439000000l, 3, "22222222222222222222");
        System.err.println(isSucc);
    }
    
    public void receiving() {
        boolean isSucc = BuyerService.receiving(3, 1461429647439000000l);
        System.err.println(isSucc);
        isSucc = BuyerService.receiving(3, 1461465779439000000l);
        System.err.println(isSucc);
        isSucc = BuyerService.receiving(3, 1461467883439000000l);
        System.err.println(isSucc);
    }
    
    public void compele() {
        OrderService.compele(3, 1461429647439000000l);
        OrderService.compele(3, 1461465779439000000l);
    }
    
    public void refundApply() {
        boolean isSucc = BuyerService.refundApply(3, 1461429647439000000l, "大小不合适");
        System.err.println(isSucc);
        isSucc = BuyerService.refundApply(3, 1461465779439000000l, "大小不合适");
        System.err.println(isSucc);
        isSucc = BuyerService.refundApply(3, 1461467793439000000l, "大小不合适");
        System.err.println(isSucc);
        isSucc = BuyerService.refundApply(3, 1461467883439000000l, "大小不合适");
        System.err.println(isSucc);
    }
    
    public void refundCancel() {
        BuyerService.refundCancel(3, 6l);
    }
    
    public void refundAudit() {
        try {
			SellerService.refundAudit(7l, 0, "促销商品不能退款");
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
