package com.auditing.work.modle.vo;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "更新历史审核备注信息")
public class TwoAuditHistoryUpdateVo {
  
    public Integer fourthCategoryId;
    public String fourthCategoryRemarks;
    
    public Long auditHistoryDetailAId;
    public String auditHistoryDetailAremarks;
    
    public Long auditHistoryDetailBId;
    public String auditHistoryDetailBremarks;
}
