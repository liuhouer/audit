package com.auditing.work.modle.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "四级类目视图")
public class FourthcategoryReviewDep {
	
	
	
	@ApiModelProperty(value = "类型。1 ：已评 ；2 ：未评审")
	public Integer status;
	public List<FourthcategoryReviewDepVo> depList;
}
