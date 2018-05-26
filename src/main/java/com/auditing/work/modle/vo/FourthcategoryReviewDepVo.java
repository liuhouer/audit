package com.auditing.work.modle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "四级类目下的部门")
public class FourthcategoryReviewDepVo {
	@ApiModelProperty(value = "部门名称")
	public String depName;
	@Override
	public String toString() {
		return "FourthcategoryReviewDep [depName=" + depName + ", type=" + type + "]";
	}
	@ApiModelProperty(value = "类型。1 ：已评部门 ；2 ：未评审部门 ")
	public Integer type;
}
