package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="merchant_trade_order")
public class MerchantTradeOrder extends GenericModel {

	@Id
    @Column(name="id")
    private Long id;

    @Column(name="out_trade_no")
    private String outTradeNo;

    @Column(name="out_trade_type")
    private Integer outTradeType;
    
    @Column(name="user_id")
    private Integer userId;

    @Column(name="total_fee")
    private Integer totalFee;

    @Column(name="subject")
    private String subject;

    @Column(name="body")
    private String body;

    @Column(name="price")
    private Integer price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="callback_url")
    private String callbackUrl;

    @Column(name="status")
    private Integer status;

    @Column(name="trade_msg")
    private String tradeMsg;

    @Column(name="trade_no")
    private String tradeNo;

    @Column(name="create_time")
    private Long createTime;

    @Column(name="callback_time")
    private Long callbackTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Integer getOutTradeType() {
		return outTradeType;
	}

	public void setOutTradeType(Integer outTradeType) {
		this.outTradeType = outTradeType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTradeMsg() {
		return tradeMsg;
	}

	public void setTradeMsg(String tradeMsg) {
		this.tradeMsg = tradeMsg;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getCallbackTime() {
		return callbackTime;
	}

	public void setCallbackTime(Long callbackTime) {
		this.callbackTime = callbackTime;
	}
}
