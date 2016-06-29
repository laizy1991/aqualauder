package models;

import javax.persistence.*;

import play.db.jpa.GenericModel;

import java.util.List;

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

    @Column(name="pay_type")
    private Integer payType;

    @Column(name="express_name")
    private String expressName;

    @Column(name="express_num")
    private String expressNum;

    @Column(name="total_fee")
    private Integer totalFee;

    @Column(name="state")
    private Integer state;

    @Column(name="forbid_refund")
    private Integer forbidRefund;

    @Column(name="order_memo")
    private String orderMemo;
    
    @Column(name="receiver")
    private String receiver;
    
    @Column(name="mobile_phone")
    private String mobilePhone;

    @Column(name="weixin")
    private String weixin;

    @Column(name="identifier")
    private String identifier;
    
    @Column(name="shipping_address")
    private String shippingAddress;

    @Column(name="state_history")
    private String stateHistory;

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
    
    @Column(name="open_id")
    private String openId;
    
    @Column(name="client_ip")
    private String clientIp;
    
    @Column(name="pay_status")
    private Integer payStatus;
    
    @Column(name="pay_time")
    private Long payTime;
    
    @Column(name="callback_status")
    private Integer callbackStatus;
    
    @Column(name="callback_time")
    private Long callbackTime;
    
    @Column(name="callback_url")
    private String callbackUrl;

    @Column(name="platform_trade_no")
    private String platformTradeNo;
    
    @Column(name="platform_trade_msg")
    private String platformTradeMsg;
    
    @Column(name="platform_transation_id")
    private String platformTransationId;

    @OneToMany
    @JoinColumn(name="order_id")
    private List<RefundOrder> refundOrder;

    @OneToMany
    @JoinColumn(name="order_id")
    private List<OrderGoods> orderGoods;
    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
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

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
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

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getPlatformTradeNo() {
		return platformTradeNo;
	}

	public void setPlatformTradeNo(String platformTradeNo) {
		this.platformTradeNo = platformTradeNo;
	}

	public String getPlatformTradeMsg() {
		return platformTradeMsg;
	}

	public void setPlatformTradeMsg(String platformTradeMsg) {
		this.platformTradeMsg = platformTradeMsg;
	}

	public String getPlatformTransationId() {
		return platformTransationId;
	}

	public void setPlatformTransationId(String platformTransationId) {
		this.platformTransationId = platformTransationId;
	}

	public Long getCallbackTime() {
		return callbackTime;
	}

	public void setCallbackTime(Long callbackTime) {
		this.callbackTime = callbackTime;
	}

	public Integer getCallbackStatus() {
		return callbackStatus;
	}

	public void setCallbackStatus(Integer callbackStatus) {
		this.callbackStatus = callbackStatus;
	}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
