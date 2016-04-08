package service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Order;
import models.OrderGoods;
import models.RefundOrder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;

import common.constants.RefundStatus;

import dao.OrderDao;
import dao.OrderGoodsDao;
import dao.RefundOrderDao;
import dto.OrderDetail;
import exception.BusinessException;

public class OrderService {

    public static Order get(long id) {
        return OrderDao.get(id);
    }

    public static void add(Order order) {
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        OrderDao.insert(order);
    }

    public static void delete(Order order) {
        OrderDao.delete(order);
    }

    public static void update(Order order) {
        order.setUpdateTime(System.currentTimeMillis());
        OrderDao.update(order);
    }
    
    /**
	 * 产生一个orderId
	 */
	public static String genOrderId() {
		String formatStr = "yyyyMMddHHmmssSSS";
		DateFormat df = new SimpleDateFormat(formatStr);
		String time = df.format(new Date());
		int no = RandomUtils.nextInt(10000);
		String suffix = String.format("%04d",no);//左补0
		return time + suffix;
	}
	
	/**
	 * 生成一张订单，并用微信支付
	 */
	public static void genOrderPayWithWx() {
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
                detail.setRefundState(RefundStatus.NOREFUND.getCode());
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
            detail.setRefundState(RefundStatus.NOREFUND.getCode());
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
    
}
