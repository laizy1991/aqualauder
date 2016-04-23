package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import models.Goods;
import models.Order;
import models.OrderGoods;
import models.RefundOrder;
import play.Logger;
import utils.DateUtil;
import utils.IdGenerator;
import common.constants.GoodsType;
import common.constants.OrderStatus;
import common.constants.RefundStatus;
import common.constants.Separator;
import dao.GoodsDao;
import dao.OrderGoodsDao;
import dao.RefundOrderDao;
import dto.OrderDetail;


public class BuyerService {


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
        if(order == null) {
            Logger.error("order not found, id:%s", orderId);
        }
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
        
        order.setRecevTime(System.currentTimeMillis());
        boolean isSucc = OrderService.setStatusAndUpdate(order, OrderStatus.RECE);
        return isSucc;
    }
    
    public static boolean paySuccess(long orderId) {
        Order order = OrderService.get(orderId);
        if(order == null) {
            Logger.error("order not found, id:%s", orderId);
            return false;
        }
        order.setPayTime(System.currentTimeMillis());
        boolean isSucc = OrderService.setStatusAndUpdate(order, OrderStatus.PAYED);
        return isSucc;
    }
}
