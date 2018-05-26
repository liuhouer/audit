package com.auditing.work.jpa.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the review_point_action_log database table.
 * 
 */
@Entity
@Table(name="review_point_action_log")
@NamedQuery(name="ReviewPointActionLog.findAll", query="SELECT r FROM ReviewPointActionLog r")
public class ReviewPointActionLog implements Serializable {
	@Override
	public String toString() {
		return "ReviewPointActionLog [id=" + id + ", actionDate=" + actionDate + ", reviewPointId=" + reviewPointId
				+ ", status=" + status + ", userId=" + userId + "]";
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(name="action_date")
	private Date actionDate;

	@Column(name="review_point_id")
	private Long reviewPointId;
	private int status;
	@Column(name="user_id")
	private Long userId;

	public ReviewPointActionLog() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getActionDate() {
		return this.actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public Long getReviewPointId() {
		return this.reviewPointId;
	}

	public void setReviewPointId(Long reviewPointId) {
		this.reviewPointId = reviewPointId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}