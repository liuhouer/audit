package com.auditing.work.modle.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * The persistent class for the review_point_action_log database table.
 * 
 */

public class ReviewPointActionLogView implements Serializable {


	@Override
	public String toString() {
		return "ReviewPointActionLogView [id=" + id + ", actionDate=" + actionDate + ", status=" + status
				+ ", userName=" + userName + ", reviewPointScore=" + reviewPointScore + ", reviewPointContent="
				+ reviewPointContent + ", reviewPointStatus=" + reviewPointStatus + ", level4Name=" + level4Name
				+ ", reviewPointId=" + reviewPointId + "]";
	}

	private static final long serialVersionUID = 1L;


	private int id;


	private String actionDate;
	private int status;
	private String userName;
	private String reviewPointScore;
	private String reviewPointContent;
	private Integer reviewPointStatus;
	private String level4Name;
	private List<Map<String, Object>> reviewPointCommentList;
	private Long reviewPointId;
	public String getReviewPointScore() {
		return reviewPointScore;
	}

	public void setReviewPointScore(String reviewPointScore) {
		this.reviewPointScore = reviewPointScore;
	}

	public Integer getReviewPointStatus() {
		return reviewPointStatus;
	}

	public void setReviewPointStatus(Integer reviewPointStatus) {
		this.reviewPointStatus = reviewPointStatus;
	}

	public String getLevel4Name() {
		return level4Name;
	}

	public void setLevel4Name(String level4Name) {
		this.level4Name = level4Name;
	}



	public ReviewPointActionLogView() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActionDate() {
		return this.actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public Long getReviewPointId() {
		return this.reviewPointId;
	}

	public void setReviewPointId(Long reviewPointId) {
		this.reviewPointId = reviewPointId;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReviewPointContent() {
		return reviewPointContent;
	}

	public void setReviewPointContent(String reviewPointContent) {
		this.reviewPointContent = reviewPointContent;
	}

	public List<Map<String, Object>> getReviewPointCommentList() {
		return reviewPointCommentList;
	}

	public void setReviewPointCommentList(List<Map<String, Object>> reviewPointCommentList) {
		this.reviewPointCommentList = reviewPointCommentList;
	}

}