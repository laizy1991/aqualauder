package service;

import models.Express;
import models.Order;
import models.RefundOrder;
import play.Logger;

import common.constants.OrderStatus;
import common.constants.RefundStatus;

import dao.ExpressDao;
import exception.BusinessException;

public class SellerService {

    /**
     * 
     * @param refundId
     * @param isAgree
     * @param reason
     * @return
     * @throws BusinessException
     */
    public static boolean refundAudit(long refundId, int isAgree, String reason) {
        RefundOrder refundOrder = RefundOrderService.get(refundId);
        if(refundOrder == null) {
            return false;
        }
        
        RefundStatus refundState = RefundStatus.ING;
        if(isAgree != 1) {
            refundState = RefundStatus.REFUSE;
        } else {
            //如果是同意退款需要关闭订单
            boolean isSucc = OrderService.setStatusAndUpdate(refundOrder.getOrderId(), OrderStatus.CLOSE);
            if(!isSucc) {
                Logger.error("close order fail. id:%s", refundOrder.getOrderId());
                return false;
            }
        }

        refundOrder.setSellerMemo(reason);
        boolean isSucc = RefundOrderService.setStatusAndUpdate(refundOrder, refundState);
        return isSucc;
    }
    
    /**
     * 
     * @param orderId
     * @param expressId
     * @param expressNum
     * @return
     */
    public static boolean delivered(long orderId, int expressId, String expressNum) {
        Order order = OrderService.get(orderId);
        if(order == null) {
            Logger.error("order not found, id:%s", orderId);
            return false;
        }

        RefundOrder refundOrder = RefundOrderService.getByOrder(orderId);
        int dbRefundState = refundOrder == null || refundOrder.getRefundState() == null ? RefundStatus.NOTREFUND.getCode() : refundOrder.getRefundState();
        if (dbRefundState == RefundStatus.APPLY.getCode()
                || dbRefundState == RefundStatus.ING.getCode()
                || dbRefundState == RefundStatus.SUCCESS.getCode()) {
            Logger.error("order is refund, can not complete, orderId:{}", orderId);
            return false;
        }
        
        int dbOrderState = order.getState() == null ? OrderStatus.INIT.getState() : order.getState();
        if (dbOrderState != OrderStatus.DELIVERING.getState()) {
            Logger.error("can not delivered, orderId:{}", orderId);
            return false;
        }
        
        Express express = ExpressDao.get(expressId);
        if(express != null) {
            order.setExpressName(express.getName());
        }
        
        order.setExpressNum(expressNum);
        order.setDeliverTime(System.currentTimeMillis());
        boolean isSucc = OrderService.setStatusAndUpdate(order, OrderStatus.DELIVERED);
            
        return isSucc;
    }
}
