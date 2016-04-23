package dao;

import java.util.List;

import models.RefundOrder;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;

public class RefundOrderDao {

    public static boolean insert(RefundOrder refundOrder) {
        if(refundOrder == null) {
            return false;
        }
        return refundOrder.create();
    }
    
    public static RefundOrder get(long id) {
        return RefundOrder.findById(id);
    }
    
    public static boolean update(RefundOrder refundOrder) {
        if(refundOrder == null || refundOrder.getId() == 0) {
            return false;
        }
        
        refundOrder.save();
        return true;
    }

    public static RefundOrder getByOrder(long orderId) {
        return RefundOrder.find("orderId", orderId).first();
    }
    
    public static void delete(RefundOrder refundOrder) {
        if (refundOrder != null) {
            refundOrder.delete();
        }
    }
}
