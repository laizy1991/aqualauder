package dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import models.Order;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.db.jpa.Model;

public class OrderDao {

    public static boolean insert(Order order) {
        if(order == null) {
            return false;
        }
        return order.create();
    }
    
    public static Order get(long id) {
        return Order.findById(id);
    }
    
    public static boolean update(Order order) {
        if(order == null || order.getId() == 0) {
            return false;
        }
        
        order.save();
        return true;
    }

    public static List<Order> list(int userId, int page, int size) {
        return Order.find("userId", userId).fetch(page, size);
    }
    
    public static List<Order> getByStatus(int state) {
        return Order.find("state", state).fetch();
    }
    
    public static void delete(Order order) {
        if (order != null) {
            order.delete();
        }
    }
    
    /**
     * 通过outTradeNo获取订单
     * @param outTradeNo
     * @return
     */
    public static Order getOrderByOutTradeNo(String outTradeNo) {
    	if(StringUtils.isEmpty(outTradeNo)) {
    		return null;
    	}
    	List<Order> list = Order.find("outTradeNo", outTradeNo).fetch();
    	if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        
        if(list.size() > 1) {
            Logger.error("too many order find. outTradeNo:" + outTradeNo);
        }
    	
    	return list.get(0);
    }
    
    public static List<Order> getByUserIds(List<Integer> userIds) {
        if(userIds == null || userIds.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        String ids = "";
        String split = "";
        for(Integer id : userIds) {
            ids = ids + split + id;
            split = ",";
        }
        String sql = "SELECT * FROM `order` where user_id in (%s);";  
        Query query = Model.em().createNativeQuery(String.format(sql, ids),Order.class);  
        return query.getResultList();  
    }
    
}
