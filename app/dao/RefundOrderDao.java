package dao;

import models.RefundOrder;
import org.apache.commons.collections.CollectionUtils;
import play.Logger;

import java.util.List;

public class RefundOrderDao {

    public static boolean insert(RefundOrder refundOrder) {
        if(refundOrder == null) {
            return false;
        }
        return refundOrder.create();
    }
    
    public static RefundOrder get(int id) {
        List<RefundOrder> list = RefundOrder.find("refundOrder_Id", id).fetch();
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        
        if(list.size() > 1) {
            Logger.error("too many refundOrder find. refundOrder_Id:" + id);
            return null;
        }
        
        return list.get(0);
    }
    
    public static void update(RefundOrder refundOrder) {
        if(refundOrder == null || refundOrder.getId() == 0) {
            return;
        }
        
        refundOrder.save();
    }

    public static void delete(RefundOrder refundOrder) {
        if (refundOrder != null) {
            refundOrder.delete();
        }
    }
}
