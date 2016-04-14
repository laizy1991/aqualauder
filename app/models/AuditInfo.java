package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="audit_info")
public class AuditInfo extends Model {

    @Column(name="user_id")
    private Integer userId;
    @Column(name="audit_type")
    private Integer auditType;
    @Column(name="audit_status")
    private Integer auditStatus;
    @Column(name="content")
    private String content;
    @Column(name="img_urls")
    private String imgUrls;
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
    public Integer getAuditType() {
        return auditType;
    }
    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }
    public Integer getAuditStatus() {
        return auditStatus;
    }
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImgUrls() {
        return imgUrls;
    }
    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
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
