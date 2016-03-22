package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="distributor_superior")
public class DistributorSuperior extends GenericModel {
    /**
     * CREATE TABLE `distributor_superior` ( `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id', 
     * `superior` int(11) DEFAULT '0' COMMENT
     * '上线用户id', `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间', PRIMARY KEY (`user_id`), KEY `idx1` (`superior`) USING BTREE
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分销商上线表';
     */
    @Id
    @Column(name = "user_id")
    private Integer userId;
 
    @Column(name="superior")
    private Integer superior;
    
    @Column(name="create_time")
    private Long createTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSuperior() {
        return superior;
    }

    public void setSuperior(Integer superior) {
        this.superior = superior;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
