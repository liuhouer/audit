package com.auditing.work.jpa.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "审批 评论 新增 接口参数")
public class ReviewPointCommentAddVo {
		
	 	public Long getReviewPointId() {
		return reviewPointId;
	}

	public void setReviewPointId(Long reviewPointId) {
		this.reviewPointId = reviewPointId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	 	
		@ApiModelProperty(value = "review_point_id",required=true)
	    private Long reviewPointId;
	 	 
		@ApiModelProperty(name = "content",required=true)
	    private String content;
		
		@ApiModelProperty(name = "userId",required=true)
	    private Long userId;

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}
}
