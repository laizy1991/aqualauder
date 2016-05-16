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
    
    @Column(name="qrcode_tmp_wx_url")
    private String qrcodeTmpWxUrl;
    
    @Column(name="qrcode_tmp_path")
    private String qrcodeTmpPath;
    
    @Column(name="qrcode_tmp_ticket")
    private String qrcodeTmpTicket;

    @Column(name="qrcode_tmp_expire_time")
    private Long qrcodeTmpExpireTime;
    
    @Column(name="qrcode_limit_wx_url")
    private String qrcodeLimitWxUrl;
    
    @Column(name="qrcode_limit_path")
    private String qrcodeLimitPath;
    
    @Column(name="qrcode_limit_ticket")
    private String qrcodeLimitTicket;
    
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

	public String getQrcodeTmpPath() {
		return qrcodeTmpPath;
	}

	public void setQrcodeTmpPath(String qrcodeTmpPath) {
		this.qrcodeTmpPath = qrcodeTmpPath;
	}

	public String getQrcodeTmpTicket() {
		return qrcodeTmpTicket;
	}

	public void setQrcodeTmpTicket(String qrcodeTmpTicket) {
		this.qrcodeTmpTicket = qrcodeTmpTicket;
	}

	public Long getQrcodeTmpExpireTime() {
		return qrcodeTmpExpireTime;
	}

	public void setQrcodeTmpExpireTime(Long qrcodeTmpExpireTime) {
		this.qrcodeTmpExpireTime = qrcodeTmpExpireTime;
	}

	public String getQrcodeLimitPath() {
		return qrcodeLimitPath;
	}

	public void setQrcodeLimitPath(String qrcodeLimitPath) {
		this.qrcodeLimitPath = qrcodeLimitPath;
	}

	public String getQrcodeLimitTicket() {
		return qrcodeLimitTicket;
	}

	public void setQrcodeLimitTicket(String qrcodeLimitTicket) {
		this.qrcodeLimitTicket = qrcodeLimitTicket;
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

	public String getQrcodeTmpWxUrl() {
		return qrcodeTmpWxUrl;
	}

	public void setQrcodeTmpWxUrl(String qrcodeTmpWxUrl) {
		this.qrcodeTmpWxUrl = qrcodeTmpWxUrl;
	}

	public String getQrcodeLimitWxUrl() {
		return qrcodeLimitWxUrl;
	}

	public void setQrcodeLimitWxUrl(String qrcodeLimitWxUrl) {
		this.qrcodeLimitWxUrl = qrcodeLimitWxUrl;
	}

}
