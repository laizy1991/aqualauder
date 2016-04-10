package service;

import models.Order;
import models.RefundOrder;
import play.Logger;
import utils.DateUtil;

import common.constants.OrderStatus;
import common.constants.RefundStatus;

import dao.RefundOrderDao;


public class BuyerService {

    public static boolean refundCancel(Integer userId, long refundId) {
        RefundOrder refundOrder = RefundOrderService.get(refundId);
        if(refundOrder == null) {
            return false;
        }

        int refundState = refundOrder.getRefundState() == null ? RefundStatus.NOTREFUND.getCode() : refundOrder.getRefundState();
        if (!(refundState == RefundStatus.APPLY.getCode()
                || refundState == RefundStatus.REFUSE.getCode())) {
            Logger.error("order is refund over, refundId:{}", refundId);
            return false;
        }
        
        boolean isSucc = RefundOrderService.updateRefundState(refundId, refundState);
        return isSucc;
    }
    
    /**
     * 申请退款
     * @param userId
     * @param orderId
     * @param memo
     */
    public static boolean refundApply(int userId, long orderId, String memo) {
        Order order = OrderService.get(orderId);
        if(order == null) {
            Logger.error("order not found, id:%s", orderId);
            return false;
        }
        
        if(order.getForbidRefund().intValue() == 1 || order.getUserId().intValue() != userId) {
            Logger.error("can not refund.");
            return false;
        }

        RefundOrder refundOrder = RefundOrderDao.getByOrder(orderId);
        if (refundOrder != null
                && (refundOrder.getRefundState().intValue() == RefundStatus.SUCCESS.getCode()
                        || refundOrder.getRefundState().intValue() == RefundStatus.APPLY
                                .getCode() || refundOrder.getRefundState().intValue() == RefundStatus.ING
                        .getCode())) {
            Logger.error("can not refund.");
            return false;
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
        return RefundOrderService.add(refundOrder);
    }
    
    /**
     * 
     * @param userId
     * @param orderId
     * @return
     */
    public static boolean receiving(int userId, long orderId) {
        Order order = OrderService.get(orderId);
        int dbOrderState = order.getState() == null ? OrderStatus.INIT.getState() : order.getState();
        if (dbOrderState != OrderStatus.DELIVERED.getState()) {
            return false;
        }
        
        RefundOrder refund = RefundOrderService.getByOrder(orderId);
        int refundState = refund == null ? RefundStatus.NOTREFUND.getCode() : refund.getRefundState();
        if (refundState == RefundStatus.APPLY.getCode()
                || refundState == RefundStatus.SUCCESS.getCode()
                || refundState == RefundStatus.ING.getCode()) {
            Logger.error("order is refund, id:%s", orderId);
            return false;
        }
        
        order.setFinishTime(System.currentTimeMillis());
        order.setState(OrderStatus.COMPLETE.getState());
        boolean isSucc = OrderService.update(order);
        return isSucc;
    }
}
