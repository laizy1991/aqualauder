package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Goods;
import models.Order;
import models.OrderGoods;
import models.RefundOrder;
import models.User;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import play.Logger;
import utils.DateUtil;
import utils.IdGenerator;

import common.constants.CommonDictKey;
import common.constants.CommonDictType;
import common.constants.GoodsType;
import common.constants.OrderStatus;
import common.constants.RefundStatus;
import common.constants.Separator;

import dao.GoodsDao;
import dao.OrderGoodsDao;
import dao.RefundOrderDao;
import dto.GoodsBrief;
import dto.OrderDetail;


public class BuyerService {


    /**
     * 创建一个订单
     * @return
     */
    public static OrderDetail createOrder(int userId, Order order, Map<GoodsBrief, Integer> goodsNum, String openId) {
        if(order == null || goodsNum == null || goodsNum.isEmpty()) {
            return null;
        }
        
        long cussTs = System.currentTimeMillis();
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
        int totalFee = 0;
        List<GoodsBrief> saveList = new ArrayList<GoodsBrief>();
        for(GoodsBrief gb : goodsNum.keySet()) {
            if(goodsNum.get(gb).intValue() <= 0) {
                continue;
            }
            Goods goods = GoodsDao.get(gb.getGoodsId());
            if(goods == null || goods.getState().intValue() == 0) {
                Logger.error("goods not found, id:%s", gb.getGoodsId());
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
            og.setGoodsNumber(goodsNum.get(gb));
            og.setGoodsType(GoodsType.GOODS.getType());
            og.setGoodsSize(gb.getGoodsSize());
            og.setGoodsColor(gb.getGoodsColor());
            og.setGoodsIdentifier(goods.getIdentifier());
            totalFee += (og.getGoodsDiscountPrice() * og.getGoodsNumber());
            orderGoodsList.add(og);
            
            //减库存
            boolean isSuccess = GoodsStockService.reduced(gb.getGoodsId(), gb.getGoodsSize(), gb.getGoodsColor(), goodsNum.get(gb));
            if(isSuccess) {
                saveList.add(gb);
            } else {
                //减库存失败回滚
                for(GoodsBrief tmp : saveList) {
                    GoodsStockService.reduced(tmp.getGoodsId(), tmp.getGoodsSize(), tmp.getGoodsColor(), goodsNum.get(tmp));
                }
                return null;
            }
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
        order.setExpressName("");
        order.setExpressNum("");
        order.setFinishTime(0l);
        order.setPayTime(0l);
        order.setUserId(userId);
        order.setRecevTime(0l);
        order.setOpenId(openId);
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
        
        Order order = OrderService.get(refundOrder.getOrderId());
        if(order == null || order.getUserId().intValue() != userId) {
            Logger.error("order on found, id:%s, userId:%s", refundOrder.getOrderId(), userId);
            return false;
        }
        
        boolean isSucc = RefundOrderService.updateRefundState(refundId, RefundStatus.CANCEL);
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
        if(order.getState().intValue() == OrderStatus.INIT.getState()) {
            return OrderService.setStatusAndUpdate(orderId, OrderStatus.CLOSE);
        }
        if (order.getState().intValue() == OrderStatus.COMPLETE.getState()
                || order.getState().intValue() == OrderStatus.CLOSE.getState()
                || order.getForbidRefund().intValue() == 1
                || order.getUserId().intValue() != userId) {
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
        
        String dbHis = StringUtils.isBlank(refundOrder.getStateHistory()) ? "" : refundOrder.getStateHistory();
        refundOrder.setStateHistory(dbHis + RefundStatus.APPLY.getCode()
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
        if(order == null || order.getUserId().intValue() != userId) {
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
        
        //通知所有上线
        notifySuperior(orderId, order.getUserId());
        
        DistributorService.checkAndBecomeDistributor(order.getUserId());
        order.setPayTime(System.currentTimeMillis());
        boolean isSucc = OrderService.setStatusAndUpdate(order, OrderStatus.DELIVERING);
        return isSucc;
    }

    /**
     * 支付成功通知上线
     * @param orderId 
     * @param userId
     */
    private static void notifySuperior(long orderId, Integer userId) {
        List<Integer> superiors = DistributorService.getSuperiors(userId);
        String msg = CommonDictService.getValue(CommonDictType.CONFIG, CommonDictKey.PAY_SUCCESS_MSG);
        for(Integer uid : superiors) {
            User user = UserService.get(uid);
            if(user == null) {
                continue;
            }
            String msgTmp = msg;
            msgTmp = msgTmp.replace("%s", user.getNickname());
            WxMsgService.notifySuperior(orderId, uid, msgTmp);
        }
    }
}
