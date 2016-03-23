package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="refund_order")
public class RefundOrder extends Model {

    @Column(name="order_id")
    private Integer orderId;

    @Column(name="refund_state")
    private Integer refundState;

    @Column(name="user_memo")
    private String userMemo;

    @Column(name="seller_memo")
    private String user_memo;

    @Column(name="state_history")
    private String state_history;

    @Column(name="create_time")
    private Long createTime;

    @Column(name="update_time")
    private Long updateTime;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public String getUser_memo() {
        return user_memo;
    }

    public void setUser_memo(String user_memo) {
        this.user_memo = user_memo;
    }

    public String getState_history() {
        return state_history;
    }

    public void setState_history(String state_history) {
        this.state_history = state_history;
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
