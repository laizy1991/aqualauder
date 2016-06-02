package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * @author nemo
 * @date 2016.04.19
 */
@Entity
@Table(name="qrcode_share")
public class QrShare extends Model {

    @Column(name="name")
    private String name;
    
    @Column(name="friend_share_title")
    private String friendShareTitle;
    
    @Column(name="friend_share_desc")
    private String friendShareDesc;
    
    @Column(name="moment_share_title")
    private String momentShareTitle;
    
    @Column(name="img_path")
    private String imgPath;
    
    @Column(name="img_url")
    private String imgUrl;
    
    @Column(name="is_enabled")
    private Integer isEnabled;
    
    @Column(name="remark")
    private String remark;
    
    @Column(name="create_time")
    private Long createTime;
    
    @Column(name="update_time")
    private Long updateTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFriendShareTitle() {
		return friendShareTitle;
	}

	public void setFriendShareTitle(String friendShareTitle) {
		this.friendShareTitle = friendShareTitle;
	}

	public String getFriendShareDesc() {
		return friendShareDesc;
	}

	public void setFriendShareDesc(String friendShareDesc) {
		this.friendShareDesc = friendShareDesc;
	}

	public String getMomentShareTitle() {
		return momentShareTitle;
	}

	public void setMomentShareTitle(String momentShareTitle) {
		this.momentShareTitle = momentShareTitle;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
