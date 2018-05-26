package com.auditing.work.jpa.po;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.annotations.ApiModelProperty;

/**
 * A DepDict.
 */
@Entity
@Table(name = "dep_reviewpoint_status")
public class DepRevierPointStauts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dep_name")
    private String depName;

	@Column(name="review_point_id")
	private Integer reviewPointId;
	
	@Column(name="status")
	@ApiModelProperty(value = "部门评审状态-> 0 :未审核, 1:已经审核 ,2:驳回 ,3 :驳回后待确认 ")
	private Integer status;
	
	@Column(name="passed")
	private Boolean passed;
	
	@Transient
	private Integer deptId;

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Long getId() {
		return id;
	}

	public String getDepName() {
		return depName;
	}

	public Integer getReviewPointId() {
		return reviewPointId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public void setReviewPointId(Integer reviewPointId) {
		this.reviewPointId = reviewPointId;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}
	 
}
