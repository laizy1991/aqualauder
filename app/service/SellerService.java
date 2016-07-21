package service;

import common.constants.OrderStatus;
import common.constants.RefundStatus;
import common.constants.wx.WxRefundStatus;
import dao.ExpressDao;
import exception.BusinessException;
import models.Express;
import models.Order;
import models.RefundOrder;
import play.Logger;
import service.wx.WXPay;
import service.wx.dto.refund.SendRefundReqDto;
import service.wx.dto.refund.SendRefundRspDto;

public class SellerService {

    /**
     * 
     * @param refundId
     * @param isAgree
     * @param reason
     * @return
     * @throws BusinessException
     */
    public static boolean refundAudit(long refundId, int isAgree, String reason) throws BusinessException {
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
        if(isSucc && isAgree == 1) {
        	refund(refundOrder);
        }
        return isSucc;
    }
    
    
    private static void refund(RefundOrder ro) throws BusinessException {
    	if(ro.getRefundState() == RefundStatus.SUCCESS.getCode() ||
    			ro.getRefundState() == RefundStatus.REFUSE.getCode() ||
    			ro.getRefundState() == RefundStatus.CANCEL.getCode() ||
				ro.getRefundState() == RefundStatus.NOTREFUND.getCode()) {
    		Logger.info("该笔订单状态为%s, 禁止退款，refundId: %d", RefundStatus.resolveType(ro.getRefundState()).getDesc(),
    				ro.getId());
    	}
    	Order order = OrderService.get(ro.getOrderId());
    	if(null == order) {
    		Logger.error("查询订单记录失败，orderId: %d", ro.getOrderId());
    	}
    	SendRefundReqDto req = new SendRefundReqDto(order.getPlatformTransationId(), ro.getOutRefundNo(), 
    				order.getTotalFee(), order.getTotalFee(), String.valueOf(order.getUserId()));
    	SendRefundRspDto rsp = WXPay.sendRefundServcie(req);
    	if(null != rsp && rsp.getReturn_code().equals("SUCCESS") && !rsp.getResult_code().equals(WxRefundStatus.FAIL.getType())) {
    		RefundOrderService.updateRefundState(ro.getId(), RefundStatus.SUCCESS);
    		WxMsgService.refundMoneyResultMsg(ro.getId());
    	} else {
    		Logger.error("退款失败");
    	}
    }
    
    /**
     * 
     * @param orderId
     * @param expressId
     * @param expressNum
     * @return
     */
    public static boolean delivered(long orderId, long expressId, String expressNum) {
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
