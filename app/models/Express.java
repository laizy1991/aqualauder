package models;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

import javax.crypto.Mac;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="express")
public class Express extends Model {

    @Column(name="name")
    private String name;

    @Column(name="create_time")
    private Long createTime;

    @Column(name="update_time")
    private Long updateTime;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
