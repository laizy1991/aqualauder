package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="distributor")
public class Distributor extends GenericModel {
    @Id
    @Column(name="user_id")
    private Integer userId;
    
    @Column(name="distributor_type")
    private Integer type;
    
    @Column(name="distributor_status")
    private Integer status;
    
    @Column(name="real_name")
    private String realName;
    
    @Column(name="join_time")
    private Long joinTime;
    
    @Column(name="link")
    private String link;
    
    @Column(name="qrcode_type")
    private Integer qrcodeType;
    
    @Column(name="qrcode_url")
    private String qrcodeUrl;
    
    @Column(name="qrcode_path")
    private String qrcodePath;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Long joinTime) {
        this.joinTime = joinTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
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

	public String getQrcodePath() {
		return qrcodePath;
	}

	public void setQrcodePath(String qrcodePath) {
		this.qrcodePath = qrcodePath;
	}

	public Integer getQrcodeType() {
		return qrcodeType;
	}

	public void setQrcodeType(Integer qrcodeType) {
		this.qrcodeType = qrcodeType;
	}
}
