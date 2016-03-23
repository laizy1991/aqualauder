package models;

import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_id_pool")
public class UserIdPool extends GenericModel {
	@Id
	@GeneratedValue
	@Column(name="user_id")
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
