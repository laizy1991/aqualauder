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
        return OrderGoods.find("orderId", orderId).fetch();
    }

    public static boolean update(OrderGoods orderGoods) {
        if(orderGoods == null || orderGoods.getId() == 0) {
            return false;
        }

        orderGoods.save();
        return true;
    }
}
