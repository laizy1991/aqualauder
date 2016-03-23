package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "administrator")
public class Administrator extends Model {

    @Column(name="password")
    private String password;
    @Column(name="username")
    private String username;
    @Column(name="deleted")
    private Integer deleted;
    @Column(name="create_time")
    private Long createTime;
    @Column(name="update_time")
    private Long updateTime;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getDeleted() {
        return deleted;
    }
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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
