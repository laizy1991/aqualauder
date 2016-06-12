package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="shipping_address")
public class ShippingAddress extends GenericModel {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @Column(name="user_id")
    private Integer userId;
    
    @Column(name="name")
    private String name;    
    @Column(name="mobile")
    private String mobile;    
    @Column(name="district_id")
    private Integer districtId;    
    @Column(name="postal")
    private Integer postal;
    @Column(name="address")
    private String address;
    @Column(name="create_time")
    private Long createTime;
    @Column(name="update_time")
    private Long updateTime;

    @Column(name="weixin")
    private String weixin;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
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
	public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Integer getDistrictId() {
        return districtId;
    }
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }
    public Integer getPostal() {
        return postal;
    }
    public void setPostal(Integer postal) {
        this.postal = postal;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
