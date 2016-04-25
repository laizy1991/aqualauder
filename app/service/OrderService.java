package service;

import java.util.ArrayList;
import java.util.List;

import models.Order;
import models.OrderGoods;
import models.RefundOrder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import play.Logger;
import utils.DateUtil;
import common.constants.OrderStatus;
import common.constants.RefundStatus;
import common.constants.Separator;
import dao.OrderDao;
import dao.OrderGoodsDao;
import dao.RefundOrderDao;
import dto.OrderDetail;
import exception.BusinessException;

public class OrderService {

    public static Order get(long id) {
        return OrderDao.get(id);
    }

    public static boolean add(Order order) {
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        return OrderDao.insert(order);
    }

    public static void delete(Order order) {
        OrderDao.delete(order);
    }

    public static boolean update(Order order) {
        order.setUpdateTime(System.currentTimeMillis());
        return OrderDao.update(order);
    }
    
    public static boolean setStatusAndUpdate(long id, OrderStatus state) {
        Order order = get(id);
        return setStatusAndUpdate(order, state);
    }
    
    public static boolean setStatusAndUpdate(Order order, OrderStatus state) {
        order.setState(state.getState());
        String dbHis = StringUtils.isBlank(order.getStateHistory()) ? "" : order.getStateHistory();
        order.setStateHistory(dbHis + OrderStatus.DELIVERED.getState() + Separator.COMMON_SEPERATOR_BL
                + DateUtil.getDateString(System.currentTimeMillis(), "yyyyMMddHHmmss")
                + Separator.COMMON_SEPERATOR_COMME);
        return update(order);
    }
    
    /**
     * 订单列表
     * 订单完整信息
     * @param userId 用户ID
     * @param page
     * @param pageSize
     * @return
     * @throws BusinessException 
     */
    public static List<OrderDetail> listOrder( int userId , int page, int pageSize) {
        if( page <= 0 ){
            page = 1;
        } 
        List<OrderDetail> result = new ArrayList<OrderDetail>();
        List<Order> orderList = OrderDao.list(userId, page, pageSize);
        if(CollectionUtils.isEmpty(orderList)) {
            return result;
        }
        
        for(Order order : orderList) {
            OrderDetail detail = new OrderDetail(order);
            result.add(detail);
            
            RefundOrder refund = RefundOrderDao.getByOrder(order.getId());
            if(refund == null) {
                detail.setRefundState(RefundStatus.NOTREFUND.getCode());
            } else {
                detail.setRefundState(refund.getRefundState());
            }
            List<OrderGoods> goods = OrderGoodsDao.getByOrder(order.getId());
            if(CollectionUtils.isEmpty(goods)) {
                continue;
            }
            
            for(OrderGoods item : goods) {
                detail.addGoodsInfo(item);
            }
        }
        return result;
    }
    
    /**
     * 获取单个订单详情
     * @param orderId
     * @return
     */
    public static OrderDetail getOrderDetail( long orderId ){
        Order order = OrderDao.get(orderId);
        OrderDetail detail = new OrderDetail(order);
        RefundOrder refund = RefundOrderDao.getByOrder(order.getId());
        if(refund == null) {
            detail.setRefundState(RefundStatus.NOTREFUND.getCode());
        } else {
            detail.setRefundState(refund.getRefundState());
        }
        List<OrderGoods> goods = OrderGoodsDao.getByOrder(order.getId());
        if(CollectionUtils.isNotEmpty(goods)) {
            for(OrderGoods item : goods) {
                detail.addGoodsInfo(item);
            }
        }
        
        return detail;
    } 
    
    /**
     * 通过outTradeNo获取订单
     * @param outTradeNo
     * @return
     */
    public static Order getOrderByOutTradeNo(String outTradeNo) {
    	if(StringUtils.isEmpty(outTradeNo)) {
    		return null;
    	}
    	return OrderDao.getOrderByOutTradeNo(outTradeNo);
    }

    public static void compele(int userId, long orderId) {
        Order order = OrderService.get(orderId);
        if(order == null || order.getUserId().intValue() != userId) {
            Logger.error("order not found, id:%s", orderId);
        }
        
        RefundOrder refund = RefundOrderService.getByOrder(orderId);
        if (refund != null
                && (refund.getRefundState().intValue() == RefundStatus.APPLY.getCode()
                        || refund.getRefundState().intValue() == RefundStatus.ING.getCode() || refund
                        .getRefundState().intValue() == RefundStatus.SUCCESS.getCode())) {
            return;
        }
        
        //不处于退款中，设置为完成订单
        order.setFinishTime(System.currentTimeMillis());
        boolean isSucc = OrderService.setStatusAndUpdate(order, OrderStatus.COMPLETE);
        if(isSucc) {
            DistributorService.blotterProduce(order.getUserId(), order.getTotalFee() , order.getOutTradeNo());
        } else {
            Logger.error("compele order fail. id:%s", order.getId());
        }
    }
}
