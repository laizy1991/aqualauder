package dao;

import models.Order;
import org.apache.commons.collections.CollectionUtils;
import play.Logger;

import java.util.List;

public class OrderDao {

    public static boolean insert(Order order) {
        if(order == null) {
            return false;
        }
        return order.create();
    }
    
    public static Order get(int id) {
        List<Order> list = Order.find("order_Id", id).fetch();
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

    public static void delete(Order order) {
        if (order != null) {
            order.delete();
        }
    }
}
