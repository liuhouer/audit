package com.auditing.work.controller;

import com.auditing.work.dal.dataobject.AuditingDetail;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.modle.RowItem;
import com.auditing.work.result.BaseResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/api/query")
@Controller
public class QueryController extends BaseController{

    @RequestMapping(value = "/auditDetail", method = RequestMethod.GET)
    public DeferredResult<String> auditing(final HttpServletRequest request) {
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        BaseResult baseResult = new BaseResult();
        try {
            RowItem rowItem = new RowItem();
            Object ob = getClassFromRequest(request, RowItem.class, deferredResult);
            if (ob != null) {
                rowItem = (RowItem) ob;
            }

            AuditingDetail auditingDetail = buildAudtingDetail(rowItem);
            if (StringUtils.isNotBlank(auditingDetail.getDetail())) {
                auditingDetail.setDetail("%" + auditingDetail.getDetail() + "%");
            }
            if (StringUtils.isNotBlank(auditingDetail.getStandards())) {
                auditingDetail.setStandards("%" + auditingDetail.getStandards() + "%");
            }
            if (StringUtils.isNotBlank(auditingDetail.getLevel())) {
                auditingDetail.setLevel(auditingDetail.getLevel() + "%");
            }
            List<AuditingDetail> list = auditingDetailMapper.queryDetailLikely(auditingDetail);
            baseResult.setData(list);
            buildSuccessResult(baseResult,deferredResult);
        }catch (Throwable e){
            buildErrorResult(baseResult, ResultCodeEnum.SYSTEM_ERROR.getCode(),ResultCodeEnum.SYSTEM_ERROR.getDesc(),deferredResult);
        }
        return deferredResult;
    }
}
