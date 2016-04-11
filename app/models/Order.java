package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="`order`")
public class Order extends GenericModel {

	@Id
    @Column(name="id")
    private Long id;
    
    @Column(name="user_id")
    private Integer userId;

    @Column(name="out_trade_no")
    private String outTradeNo;

    @Column(name="slip_no")
    private String slipNo;

    @Column(name="pay_type")
    private Integer payType;

    @Column(name="express_id")
    private Integer expressId;

    @Column(name="express_num")
    private String expressNum;

    @Column(name="state")
    private Integer state;

    @Column(name="forbid_refund")
    private Integer forbidRefund;

    @Column(name="order_memo")
    private String orderMemo;

    @Column(name="shipping_address")
    private String shippingAddress;

    @Column(name="state_history")
    private String stateHistory;

    @Column(name="pay_time")
    private Long payTime;

    @Column(name="deliver_time")
    private Long deliverTime;

    @Column(name="recev_time")
    private Long recevTime;

    @Column(name="finish_time")
    private Long finishTime;

    @Column(name="create_time")
    private Long createTime;

    @Column(name="update_time")
    private Long updateTime;

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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
