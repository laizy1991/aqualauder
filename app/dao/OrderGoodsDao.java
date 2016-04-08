package dao;

import java.util.List;

import models.Order;
import models.OrderGoods;

public class OrderGoodsDao {

    public static boolean insert(OrderGoods orderGoods) {
        if(orderGoods == null) {
            return false;
        }
        return orderGoods.create();
    }

    public static List<OrderGoods> getByOrder(long orderId) {
        return Order.find("orderId", orderId).fetch();
    }
}
