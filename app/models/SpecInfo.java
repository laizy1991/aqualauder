package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author nemo
 * @date 2016.04.19
 */
@Entity
@Table(name="spec_info  ")
public class SpecInfo extends Model {

    @Column(name="name")
    private String name;

    @Column(name="spec_type")
    private Long specType;

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

    public Long getSpecType() {
        return specType;
    }

    public void setSpecType(Long specType) {
        this.specType = specType;
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
