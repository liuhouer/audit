package com.auditing.work.jpa.po;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the review_point_message database table.
 * 
 */
@Entity
@Table(name="review_point_message")
@NamedQuery(name="ReviewPointMessage.findAll", query="SELECT r FROM ReviewPointMessage r")
public class ReviewPointMessage implements Serializable {
	@Override
	public String toString() {
		return "ReviewPointMessage [id=" + id + ", isReaded=" + isReaded + ", reviewPointActionLogId="
				+ reviewPointActionLogId + ", userId=" + userId + "]";
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="is_readed")
	private int isReaded;



	@Column(name="review_point_action_log_id")
	private int reviewPointActionLogId;

	@Column(name="user_id")
	private int userId;

	public ReviewPointMessage() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsReaded() {
		return this.isReaded;
	}

	public void setIsReaded(int isReaded) {
		this.isReaded = isReaded;
	}


	public int getReviewPointActionLogId() {
		return this.reviewPointActionLogId;
	}

	public void setReviewPointActionLogId(int reviewPointActionLogId) {
		this.reviewPointActionLogId = reviewPointActionLogId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}