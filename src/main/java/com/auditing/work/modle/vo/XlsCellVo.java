package com.auditing.work.modle.vo;

public class XlsCellVo {
	@Override
	public String toString() {
		return "XlsCellVo [id=" + id + ", categoryName=" + categoryName + ", reviewPointName=" + reviewPointName
				+ ", depName=" + depName  + "]";
	}
	private Integer id; 
	private String categoryName; 
	private String reviewPointName; 
	private String depName; 

	
	public XlsCellVo() {
		super();
	}
	public XlsCellVo(String categoryName) {
		super();
		this.categoryName = categoryName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getReviewPointName() {
		return reviewPointName;
	}
	public void setReviewPointName(String reviewPointName) {
		this.reviewPointName = reviewPointName;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	
}
