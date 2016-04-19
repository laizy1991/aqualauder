package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="cash_info")
public class CashInfo extends Model {
    @Column(name="user_id")
    private Integer userId;
    @Column(name="cash_type")
    private Integer cashType;
    @Column(name="amount")
    private Integer amount;
    @Column(name="cash_status")
    private Integer cashStatus;
    @Column(name="slip_no")
    private String slipNo;
    @Column(name="mch_billno")
    private String mchBillno;
    @Column(name = "create_time")
    private Long createTime;
    
    @Column(name = "update_time")
    private Long updateTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCashType() {
        return cashType;
    }

    public void setCashType(Integer cashType) {
        this.cashType = cashType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCashStatus() {
        return cashStatus;
    }

    public void setCashStatus(Integer cashStatus) {
        this.cashStatus = cashStatus;
    }

    public String getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(String slipNo) {
        this.slipNo = slipNo;
    }

    public String getMchBillno() {
		return mchBillno;
	}

	public void setMchBillno(String mchBillno) {
		this.mchBillno = mchBillno;
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
    
}
