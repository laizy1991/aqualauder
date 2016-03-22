package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.sf.oval.constraint.MaxLength;
import play.db.jpa.Model;

/**
 * 通用字典表
 *
 */
@Entity
@Table(name = "common_dict")
public class CommonDict extends Model {
    @Column(name = "dict_type")
    private Integer type;
    
    @Column(name = "dict_value")
    @MaxLength(value = 50)
    private String value;
    
    @Column(name = "dict_key")
    @MaxLength(value = 50)
    private String key;
    
    @Column(name = "dict_desc")
    @MaxLength(value = 50)
    private String desc;
    
    @Column(name = "create_time")
    private Long createTime;
    
    @Column(name = "update_time")
    private Long updateTime;
    
    @Column(name = "deleted")
    private Integer deleted;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
