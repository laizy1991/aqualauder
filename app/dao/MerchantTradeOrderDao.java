package dao;

import java.util.List;

import models.MerchantTradeOrder;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;

public class MerchantTradeOrderDao {

    public static boolean insert(MerchantTradeOrder order) {
        if(order == null) {
            return false;
        }
        return order.create();
    }
    
    public static MerchantTradeOrder get(long id) {
        List<MerchantTradeOrder> list = MerchantTradeOrder.find("id", id).fetch();
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        
        if(list.size() > 1) {
            Logger.error("too many order find. order_Id:" + id);
            return null;
        }
        
        return list.get(0);
    }
    
    public static boolean update(MerchantTradeOrder order) {
        if(order == null || order.getId() == 0) {
            return false;
        }
        
        order.save();
        return true;
    }

    public static List<MerchantTradeOrder> list(int userId, int page, int size) {
        return MerchantTradeOrder.find("userId", userId).fetch(page, size);
    }
    
    public static void delete(MerchantTradeOrder order) {
        if (order != null) {
            order.delete();
        }
    }
}
