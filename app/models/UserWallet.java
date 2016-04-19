package models;

import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_wallet")
public class UserWallet extends GenericModel {
    @Id
    @Column(name="user_id")
    private Integer userId;

    /**
     * 银行号
     */
    @Column(name="card_no")
    private String cardNo;

    /**
     * 余额
     */
    @Column(name="balances")
    private Integer balances;
    
    /**
     * 累计收入
     */
    @Column(name="income")
    private Integer income;
    
    /**
     * 创建时间
     */
    @Column(name="create_time")
    private Long createTime;
    
    /**
     * 更新时间
     */
    @Column(name="update_time")
    private Long updateTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getBalances() {
        return balances;
    }

    public void setBalances(Integer balances) {
        this.balances = balances;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
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
