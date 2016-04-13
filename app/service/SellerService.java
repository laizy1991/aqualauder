package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Goods;
import models.Order;
import models.OrderGoods;
import models.RefundOrder;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;
import utils.DateUtil;
import utils.IdGenerator;

import common.constants.GoodsType;
import common.constants.OrderStatus;
import common.constants.RefundStatus;
import common.constants.Separator;

import dao.GoodsDao;
import dao.OrderGoodsDao;
import dto.OrderDetail;
import exception.BusinessException;

public class SellerService {

    /**
     * 创建一个订单
     * @return
     */
    public static OrderDetail createOrder(int userId, Order order, Map<Long, Integer> goodsNum) {
        if(order == null || goodsNum == null || goodsNum.isEmpty()) {
            return null;
        }
        
        long cussTs = System.currentTimeMillis();
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
        int totalFee = 0;
        for(Long id : goodsNum.keySet()) {
            if(goodsNum.get(id).intValue() <= 0) {
                continue;
            }
            Goods goods = GoodsDao.get(id);
            if(goods == null || goods.getState().intValue() == 0) {
                Logger.error("goods not found, id:%s", id);
            }
            OrderGoods og = new OrderGoods();
            og.setCreateTime(cussTs);
            og.setGoodsDesc(goods.getGoodsDesc());
            //暂时不加上折扣信息
            og.setGoodsDiscountPrice(goods.getPrice());
            og.setGoodsOriginPrice(goods.getPrice());
            og.setGoodsIcon(GoodsService.getIcon(goods.getId()));
            og.setGoodsId(goods.getId());
            og.setGoodsTitle(goods.getTitle());
            og.setGoodsNumber(goodsNum.get(id));
            og.setGoodsType(GoodsType.GOODS.getType());
            totalFee += (og.getGoodsDiscountPrice() + og.getGoodsNumber());
            orderGoodsList.add(og);
        }
        
        if(CollectionUtils.isEmpty(orderGoodsList)) {
            return null;
        }
        
        order.setTotalFee(totalFee);
        order.setId(IdGenerator.getId());
        order.setForbidRefund(0);
        order.setOutTradeNo(OutTradeNo.getOutTradeNo());
        order.setState(OrderStatus.INIT.getState());
        order.setStateHistory(OrderStatus.INIT.getState() + Separator.COMMON_SEPERATOR_BL
                + DateUtil.getDateString(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        
        order.setDeliverTime(0l);
        order.setExpressId(null);
        order.setExpressNum("");
        order.setFinishTime(0l);
        order.setPayTime(0l);
        order.setSlipNo("");
        order.setUserId(userId);
        order.setRecevTime(0l);
        boolean isSucc = OrderService.add(order);
        
        if(!isSucc) {
            return null;
        }
        
        OrderDetail detail = new OrderDetail(order);
        for(OrderGoods og : orderGoodsList) {
            og.setOrderId(order.getId());
            OrderGoodsDao.insert(og);
            detail.addGoodsInfo(og);
        }
        
        return detail;
    }

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
        
        boolean isSucc = RefundOrderService.updateRefundState(refundId, refundState);
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
        int dbRefundState = refundOrder.getRefundState() == null ? RefundStatus.NOTREFUND.getCode() : refundOrder.getRefundState();
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
        order.setExpressId(expressId);
        order.setExpressNum(expressNum);
        order.setDeliverTime(System.currentTimeMillis());
        boolean isSucc = OrderService.setStatusAndUpdate(order, OrderStatus.DELIVERED);
            
        return isSucc;
    }
}
