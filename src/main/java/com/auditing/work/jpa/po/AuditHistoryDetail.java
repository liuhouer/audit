package com.auditing.work.jpa.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;

/**
 * 历史审核
 */
@ApiModel(description = "历史审核详情")
@Entity
@Table(name = "audit_history_detail")
public class AuditHistoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
  
	@Column(name = "score")
    private String score;
    
    @Column(name = "fourth_category_id")
    private Integer fourthCategoryId;
    
    @Column(name = "audit_history_id")
    private Long auditHistoryId;
    
    @Column(name = "remarks")
    private String remarks;
    
    public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Long getId() {
		return id;
	}


	public String getScore() {
		return score;
	}



	public Long getAuditHistoryId() {
		return auditHistoryId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setScore(String score) {
		this.score = score;
	}



	@Override
	public String toString() {
		return "AuditHistoryDetail [id=" + id + ", score=" + score + ", fourthCategoryId=" + fourthCategoryId
				+ ", auditHistoryId=" + auditHistoryId + "]";
	}


	public Integer getFourthCategoryId() {
		return fourthCategoryId;
	}


	public void setFourthCategoryId(Integer fourthCategoryId) {
		this.fourthCategoryId = fourthCategoryId;
	}


	public void setAuditHistoryId(Long auditHistoryId) {
		this.auditHistoryId = auditHistoryId;
	}
}
