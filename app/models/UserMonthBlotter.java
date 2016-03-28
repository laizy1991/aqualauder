package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * 
 * @author laizy1991@gmail.com
 * @createDate 2016年3月27日
 *
 */
@Entity
@Table(name="user_month_blotters")
public class UserMonthBlotter extends Model{

    @Column(name="user_id")
    private Integer userId;

    @Column(name="month_blotters")
    private Long monthBlotters;
    
    @Column(name="blotter_month")
    private Integer blotterMonth;
    
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

    public Long getMonthBlotters() {
        return monthBlotters;
    }

    public void setMonthBlotters(Long monthBlotters) {
        this.monthBlotters = monthBlotters;
    }

    public Integer getBlotterMonth() {
        return blotterMonth;
    }

    public void setBlotterMonth(Integer blotterMonth) {
        this.blotterMonth = blotterMonth;
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
