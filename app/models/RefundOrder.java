package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="refund_order")
public class RefundOrder extends Model {

    @Column(name="order_id")
    private Long orderId;
    
    @Column(name="refund_type")
    private Integer refundTtype;

    @Column(name="transaction_id")
    private String transactionId;
    
    @Column(name="out_trade_no")
    private String outTradeNo;
   
    @Column(name="out_refund_no")
    private String outRefundNo;
    
    @Column(name="refund_id")
    private String refundId;
    
    @Column(name="refund_state")
    private Integer refundState;
    
    @Column(name="refund_recv_accout")
    private String refundRecvAccout;
    
    @Column(name="user_memo")
    private String userMemo;

    @Column(name="seller_memo")
    private String sellerMemo;

    @Column(name="state_history")
    private String stateHistory;
    
    @Column(name="op_user_id")
    private Long opUserId;

    @Column(name="create_time")
    private Long createTime;

    @Column(name="update_time")
    private Long updateTime;

	@Column(name="weixin")
	private String weixin;

	@Column(name="goods_title")
	private String goodsTitle;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getRefundTtype() {
		return refundTtype;
	}

	public void setRefundTtype(Integer refundTtype) {
		this.refundTtype = refundTtype;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public Integer getRefundState() {
		return refundState;
	}

	public void setRefundState(Integer refundState) {
		this.refundState = refundState;
	}

	public String getUserMemo() {
		return userMemo;
	}

	public void setUserMemo(String userMemo) {
		this.userMemo = userMemo;
	}

	public String getSellerMemo() {
		return sellerMemo;
	}

	public void setSellerMemo(String sellerMemo) {
		this.sellerMemo = sellerMemo;
	}

	public String getStateHistory() {
		return stateHistory;
	}

	public void setStateHistory(String stateHistory) {
		this.stateHistory = stateHistory;
	}

	public Long getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(Long opUserId) {
		this.opUserId = opUserId;
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

	public String getRefundRecvAccout() {
		return refundRecvAccout;
	}

	public void setRefundRecvAccout(String refundRecvAccout) {
		this.refundRecvAccout = refundRecvAccout;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}
}
