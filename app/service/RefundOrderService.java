package service;

import models.Order;
import models.RefundOrder;
import play.Logger;
import utils.DateUtil;

import common.constants.RefundStatus;

import dao.RefundOrderDao;

public class RefundOrderService {

    public static RefundOrder get(int id) {
        return RefundOrderDao.get(id);
    }

    public static void add(RefundOrder refundOrder) {
        refundOrder.setCreateTime(System.currentTimeMillis());
        refundOrder.setUpdateTime(System.currentTimeMillis());
        RefundOrderDao.insert(refundOrder);
    }

    public static void delete(RefundOrder refundOrder) {
        RefundOrderDao.delete(refundOrder);
    }

    public static void update(RefundOrder refundOrder) {
        refundOrder.setUpdateTime(System.currentTimeMillis());
        RefundOrderDao.update(refundOrder);
    }
    
    /**
     * 申请退款
     * @param userId
     * @param orderId
     * @param memo
     */
    public static void refundApply(int userId, long orderId, String memo) {
        Order order = OrderService.get(orderId);
        if(order == null) {
            Logger.error("order not found, id:%s", orderId);
            return;
        }
        
        if(order.getForbidRefund().intValue() == 1 || order.getUserId().intValue() != userId) {
            Logger.error("can not refund.");
            return;
        }

        RefundOrder refundOrder = RefundOrderDao.getByOrder(orderId);
        if (refundOrder != null
                && (refundOrder.getRefundState().intValue() == RefundStatus.SUCCESS.getCode()
                        || refundOrder.getRefundState().intValue() == RefundStatus.APPLY
                                .getCode() || refundOrder.getRefundState().intValue() == RefundStatus.ING
                        .getCode())) {
            Logger.error("can not refund.");
            return;
        }
        if(refundOrder == null) {
            refundOrder = new RefundOrder();
        }
        refundOrder.setStateHistory(refundOrder.getStateHistory() + RefundStatus.APPLY.getCode()
                + "_" + DateUtil.getDateString(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        refundOrder.setCreateTime(System.currentTimeMillis());
        refundOrder.setOrderId(orderId);
        refundOrder.setRefundState(RefundStatus.APPLY.getCode());
        refundOrder.setUpdateTime(System.currentTimeMillis());
        refundOrder.setUserMemo(memo);
        refundOrder.setSellerMemo("");
        add(refundOrder);
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
