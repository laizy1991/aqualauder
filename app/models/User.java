package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import sun.util.calendar.BaseCalendar.Date;

@Entity
@Table(name="user")
public class User extends GenericModel{

	/**
	 * 用户ID
	 */
	@Id
	@Column(name="user_id")
	private int userId;
	
	/**
	 * 用户手机号码
	 */
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="reg_type")
	private int regType;

	/**
	/**
	 * 昵称 
	 */
	@Column(name="nickname")
	private String nickname;

	@Column(name="sex")
	private Integer sex;
	
	@Column(name="birthday")
	private Date birthday;
	
    @Column(name = "create_time")
    private Long createTime;
    
    @Column(name = "update_time")
    private Long updateTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getRegType() {
        return regType;
    }

    public void setRegType(int regType) {
        this.regType = regType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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