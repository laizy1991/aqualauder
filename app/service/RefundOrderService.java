package service;

import models.Order;
import models.RefundOrder;
import play.Logger;
import utils.DateUtil;

import common.constants.RefundStatus;

import dao.RefundOrderDao;

public class RefundOrderService {

    public static RefundOrder get(long id) {
        return RefundOrderDao.get(id);
    }

    public static boolean add(RefundOrder refundOrder) {
        refundOrder.setCreateTime(System.currentTimeMillis());
        refundOrder.setUpdateTime(System.currentTimeMillis());
        return RefundOrderDao.insert(refundOrder);
    }

    public static void delete(RefundOrder refundOrder) {
        RefundOrderDao.delete(refundOrder);
    }

    public static void update(RefundOrder refundOrder) {
        refundOrder.setUpdateTime(System.currentTimeMillis());
        RefundOrderDao.update(refundOrder);
    }
    
    public static RefundOrder getByOrder(long orderId) {
        return RefundOrderDao.getByOrder(orderId);
    }
    
    public static boolean updateRefundState(long id, int status) {
        RefundStatus rs = RefundStatus.resolveType(status);
        return updateRefundState(id, rs);
    }
    /**
     * 更新订单退款状态
     * @param orderId
     * @param status
     * @return
     */
    public static boolean updateRefundState(long id, RefundStatus status) {
        if(status == null) {
            return false;
        }
        RefundOrder refundOrder = RefundOrderDao.get(id);
        if(refundOrder == null) {
            Logger.error("refund order not found, id:%s", id);
            return false;
        }
        
        if (refundOrder.getRefundState().intValue() == status.getCode()) {
            return true;
        }

        refundOrder.setStateHistory(refundOrder.getStateHistory() + RefundStatus.APPLY.getCode()
                + "_" + DateUtil.getDateString(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        refundOrder.setUpdateTime(System.currentTimeMillis());
        return RefundOrderDao.update(refundOrder);
    }
}
