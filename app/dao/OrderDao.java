package dao;

import java.util.List;

import models.Order;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;

public class OrderDao {

    public static boolean insert(Order order) {
        if(order == null) {
            return false;
        }
        return order.create();
    }
    
    public static Order get(long id) {
        List<Order> list = Order.find("id", id).fetch();
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        
        if(list.size() > 1) {
            Logger.error("too many order find. order_Id:" + id);
            return null;
        }
        
        return list.get(0);
    }
    
    public static void update(Order order) {
        if(order == null || order.getId() == 0) {
            return;
        }
        
        order.save();
    }

    public static List<Order> list(int userId, int page, int size) {
        return Order.find("userId", userId).fetch(page, size);
    }
    
    public static void delete(Order order) {
        if (order != null) {
            order.delete();
        }
    }
}
