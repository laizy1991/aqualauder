package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="user_wallet_bill")
public class UserWalletBill extends Model {
    @Column(name="user_id")
    private Integer userId;
    
    @Column(name="bill_type")
    private Integer billType;
    
    @Column(name="oper_type")
    private Integer operType;
    
    @Column(name="`trigger`")
    private Integer trigger;
    
    @Column(name="amount")
    private Integer amount;
    
    @Column(name="balance")
    private Integer balance;
    
    @Column(name="bill_desc")
    private String billDesc;
    
    @Column(name="bill_time")
    private Long billTime;
    
    @Column(name="obj_id")
    private String objId;
    
    /**
     * 消费月份
     */
    @Column(name="bill_month")
    private Integer billMonth;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public Integer getTrigger() {
        return trigger;
    }

    public void setTrigger(Integer trigger) {
        this.trigger = trigger;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }

    public Long getBillTime() {
        return billTime;
    }

    public void setBillTime(Long billTime) {
        this.billTime = billTime;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public Integer getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(Integer billMonth) {
        this.billMonth = billMonth;
    }
}
