package dto;

import java.util.ArrayList;
import java.util.List;

import models.Order;
import models.OrderGoods;
import play.db.jpa.Model;


/**
 * 订单详情
 * @author laizy1991@gmail.com
 * @createDate 2016年4月8日
 *
 */
public class OrderDetail {
    private Integer userId;
    private String outTradeNo;
    private String slipNo;
    private Integer payType;
    private Integer expressId;
    private String expressNum;
    private Integer state;
    private Integer forbidRefund;
    //-1=无退款，0=申请退款,2=退款中,3=退款成功,4=拒绝退款，5=取消退款',
    private Integer refundState;
    private String orderMemo;
    private String shippingAddress;
    private String stateHistory;
    private Long payTime;
    private Long deliverTime;
    private Long recevTime;
    private Long finishTime;
    private List<OrderGoodsInfo> goodsInfo = new ArrayList<OrderGoodsInfo>();

    
    public OrderDetail(Order order) {
        this.deliverTime = order.getDeliverTime();
        this.expressId = order.getExpressId();
        this.expressNum = order.getExpressNum();
        this.finishTime = order.getFinishTime();
        this.forbidRefund = order.getForbidRefund();
        this.orderMemo = order.getOrderMemo();
        this.outTradeNo = order.getOutTradeNo();
        this.payTime = order.getPayTime();
        this.payType = order.getPayType();
        this.recevTime = order.getRecevTime();
        this.shippingAddress = order.getShippingAddress();
        this.slipNo = order.getSlipNo();
        this.state = order.getState();
        this.stateHistory = order.getStateHistory();
        this.userId = order.getUserId();
    }
    
    public Integer getRefundState() {
        return refundState;
    }

    public void setRefundState(Integer refundState) {
        this.refundState = refundState;
    }

    public void addGoodsInfo(OrderGoods goods) {
        if(goods == null) {
            return;
        }
        OrderGoodsInfo info = new OrderGoodsInfo();
        info.setCreateTime(goods.getCreateTime());
        info.setGoodsDesc(goods.getGoodsDesc());
        info.setGoodsDiscountPrice(goods.getGoodsDiscountPrice());
        info.setGoodsIcon(goods.getGoodsIcon());
        info.setGoodsId(goods.getGoodsId());
        info.setGoodsName(goods.getGoodsName());
        info.setGoodsNumber(goods.getGoodsDiscountPrice());
        info.setGoodsOriginPrice(goods.getGoodsOriginPrice());
        info.setGoodsType(goods.getGoodsType());
        info.setOrderId(goods.getOrderId());
        goodsInfo.add(info);
        
        
    }
    
    public List<OrderGoodsInfo> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<OrderGoodsInfo> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(String slipNo) {
        this.slipNo = slipNo;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getExpressId() {
        return expressId;
    }

    public void setExpressId(Integer expressId) {
        this.expressId = expressId;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getForbidRefund() {
        return forbidRefund;
    }

    public void setForbidRefund(Integer forbidRefund) {
        this.forbidRefund = forbidRefund;
    }

    public String getOrderMemo() {
        return orderMemo;
    }

    public void setOrderMemo(String orderMemo) {
        this.orderMemo = orderMemo;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getStateHistory() {
        return stateHistory;
    }

    public void setStateHistory(String stateHistory) {
        this.stateHistory = stateHistory;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Long getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Long deliverTime) {
        this.deliverTime = deliverTime;
    }

    public Long getRecevTime() {
        return recevTime;
    }

    public void setRecevTime(Long recevTime) {
        this.recevTime = recevTime;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public class OrderGoodsInfo extends Model {
        private Long orderId;
        private Long goodsId;
        private String goodsName;
        private Integer goodsType;
        private Integer goodsNumber;
        private String goodsIcon;
        private String goodsDesc;
        private Integer goodsOriginPrice;
        private Integer goodsDiscountPrice;
        private Long createTime;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public Long getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Long goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public Integer getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(Integer goodsType) {
            this.goodsType = goodsType;
        }

        public Integer getGoodsNumber() {
            return goodsNumber;
        }

        public void setGoodsNumber(Integer goodsNumber) {
            this.goodsNumber = goodsNumber;
        }

        public String getGoodsIcon() {
            return goodsIcon;
        }

        public void setGoodsIcon(String goodsIcon) {
            this.goodsIcon = goodsIcon;
        }

        public String getGoodsDesc() {
            return goodsDesc;
        }

        public void setGoodsDesc(String goodsDesc) {
            this.goodsDesc = goodsDesc;
        }

        public Integer getGoodsOriginPrice() {
            return goodsOriginPrice;
        }

        public void setGoodsOriginPrice(Integer goodsOriginPrice) {
            this.goodsOriginPrice = goodsOriginPrice;
        }

        public Integer getGoodsDiscountPrice() {
            return goodsDiscountPrice;
        }
        public void setGoodsDiscountPrice(Integer goodsDiscountPrice) {
            this.goodsDiscountPrice = goodsDiscountPrice;
        }
        public Long getCreateTime() {
            return createTime;
        }
        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

    }
}
