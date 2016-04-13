package common.jobs;

import java.util.List;

import models.Order;
import models.RefundOrder;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;
import play.jobs.Job;
import play.jobs.On;
import service.CommonDictService;
import service.DistributorService;
import service.OrderService;
import service.RefundOrderService;

import common.constants.CommonDictKey;
import common.constants.CommonDictType;
import common.constants.OrderStatus;
import common.constants.RefundStatus;

import dao.OrderDao;

/**
 * 定时任务，每天凌晨
 * @author laizy1991@gmail.com
 * @createDate 2016年4月13日
 *
 */
@On("0 0 * * *")
public class OrderCheckJob extends Job {

    @Override
    public void doJob() throws Exception {
        Logger.info("start order check job.");
        autoRece();
        compele();
    }

    //首先查询所有已发货状态的订单，判断是否已经达到自动确认收货时间
    private void autoRece() {
        List<Order> deliveredOrders = OrderDao.getByStatus(OrderStatus.DELIVERED.getState());
        if(CollectionUtils.isEmpty(deliveredOrders)) {
            return;
        }
        String dayLimitStr = CommonDictService.getValue(CommonDictType.CONFIG, CommonDictKey.AUTO_CONFIRM_ORDER);
        int dayLimit = 7;
        try {
            dayLimit = Integer.parseInt(dayLimitStr);
        } catch(Exception e) {
            Logger.error("invalid config, key:%s", CommonDictKey.AUTO_CONFIRM_ORDER);
            dayLimit = 7;
        }
        
        long currTs = System.currentTimeMillis();
        long limitTs = currTs - (dayLimit * 24 * 60 * 60 * 1000);
        for(Order order : deliveredOrders) {
            if(order.getDeliverTime() > limitTs) {
                continue;
            }
            
            //走到这里说明到了自动确认收货的时间。需要判断是否处于中，或退款成功的状态，如果是不能确认收货
            RefundOrder refund = RefundOrderService.getByOrder(order.getId());
            if (refund != null
                    && (refund.getRefundState().intValue() == RefundStatus.APPLY.getCode()
                            || refund.getRefundState().intValue() == RefundStatus.ING.getCode() || refund
                            .getRefundState().intValue() == RefundStatus.SUCCESS.getCode())) {
                continue;
            }
            
            //不处于退款中，设置为确认收货
            order.setRecevTime(currTs);
            OrderService.setStatusAndUpdate(order, OrderStatus.RECE);
        }
    }
    
    private void compele() {
        List<Order> receOrders = OrderDao.getByStatus(OrderStatus.RECE.getState());
        if(CollectionUtils.isEmpty(receOrders)) {
            return;
        }
        String dayLimitStr = CommonDictService.getValue(CommonDictType.CONFIG, CommonDictKey.AUTO_FINISH_ORDER);
        int dayLimit = 7;
        try {
            dayLimit = Integer.parseInt(dayLimitStr);
        } catch(Exception e) {
            Logger.error("invalid config, key:%s", CommonDictKey.AUTO_FINISH_ORDER);
            dayLimit = 7;
        }
        
        long currTs = System.currentTimeMillis();
        long limitTs = currTs - (dayLimit * 24 * 60 * 60 * 1000);
        for(Order order : receOrders) {
            if(order.getRecevTime() > limitTs) {
                continue;
            }
            
            //走到这里说明到了自动完成订单的时间。需要判断是否处于中，或退款成功的状态，如果是不能确认收货
            RefundOrder refund = RefundOrderService.getByOrder(order.getId());
            if (refund != null
                    && (refund.getRefundState().intValue() == RefundStatus.APPLY.getCode()
                            || refund.getRefundState().intValue() == RefundStatus.ING.getCode() || refund
                            .getRefundState().intValue() == RefundStatus.SUCCESS.getCode())) {
                continue;
            }
            
            //不处于退款中，设置为完成订单
            order.setFinishTime(currTs);
            boolean isSucc = OrderService.setStatusAndUpdate(order, OrderStatus.COMPLETE);
            if(isSucc) {
                DistributorService.blotterProduce(order.getUserId(), order.getTotalFee() , order.getOutTradeNo());
            } else {
                Logger.error("compele order fail. id:%s", order.getId());
            }
        }
    }
}
