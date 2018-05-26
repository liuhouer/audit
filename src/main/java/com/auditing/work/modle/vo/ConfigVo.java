package com.auditing.work.modle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="系统配置信息",description="系统配置信息")
public class ConfigVo {
	@ApiModelProperty(value = "部门管理员  是否可以进行审核操作")
	public String reviewPointisManagerEditConfig ;
	@ApiModelProperty(value = "超级管理员  是否可以进行审核操作")
	public String reviewPointisEditConfig ;
}
