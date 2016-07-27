package common.jobs;

import java.util.List;

import models.Order;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;
import play.jobs.Job;
import play.jobs.On;
import service.BuyerService;
import service.CommonDictService;
import service.OrderService;

import common.constants.CommonDictKey;
import common.constants.CommonDictType;
import common.constants.OrderStatus;

import dao.OrderDao;

/**
 * 定时任务，每天凌晨
 * @author laizy1991@gmail.com
 * @createDate 2016年4月13日
 *
 */
@On("0 0 0 * * ?")
public class OrderCheckJob extends Job {

    @Override
    public void doJob() throws Exception {
        Logger.info("start order check job.");
        autoRece();
        compele();
    }

    //首先查询所有已发货状态的订单，判断是否已经达到自动确认收货时间
    private void autoRece() {
        Logger.info("开始自动确认收货....");
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
        
        Logger.info("发货中的订单数：%s，自动确认收货时间：%s",deliveredOrders.size(), dayLimit);
        long currTs = System.currentTimeMillis();
        long limitTs = currTs - ((dayLimit-1) * 24 * 60 * 60 * 1000);
        for(Order order : deliveredOrders) {
            if(order.getDeliverTime() > limitTs) {
                continue;
            }
            
            Logger.info("自动确认收货， 订单id:%s", order.getId());
            //走到这里说明到了自动确认收货的时间。
            BuyerService.receiving(order.getUserId(), order.getId());
        }
    }
    
    private void compele() {
        Logger.info("开始自动完成订单....");
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

        Logger.info("已经确认收货的订单数：%s，自动完成订单时间：%s",receOrders.size(), dayLimit);
        long currTs = System.currentTimeMillis();
        //自然天，比如这里限制的是1天，那么昨天的任何时候刻购买的，都在今天这次定时任务计算
        long limitTs = currTs - ((dayLimit-1) * 24 * 60 * 60 * 1000);
        for(Order order : receOrders) {
            if(order.getRecevTime() > limitTs) {
                continue;
            }
            
            //走到这里说明到了自动完成订单的时间。需要判断是否处于中，或退款成功的状态，如果是不能确认收货
            Logger.info("自动完成订单， 订单id:", order.getId());
            OrderService.compele(order.getUserId(), order.getId());
        }
    }
}
