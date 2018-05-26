package com.auditing.work.modle.vo;

import java.util.List;

import com.auditing.work.dal.dataobject.ReviewPointsReturn;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="分页信息",description="分页信息")
public class ReviewedPointsPageVo {
	@ApiModelProperty(value = "总页数")
	public Integer totalPage ;
	@ApiModelProperty(value = "数据数组")
	public List<ReviewPointsReturn> list ;
}
