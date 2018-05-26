package com.auditing.work.controller;

import com.alibaba.fastjson.JSONObject;
import com.auditing.work.auditing.Rule;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.enums.AuditingStrategyEnum;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.exception.WorkException;
import com.auditing.work.modle.AuditingRequest;
import com.auditing.work.result.BaseResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/audit")
@Controller
public class AuditingController extends BaseController {

    @RequestMapping(value = "/auditing", method = RequestMethod.POST)
    public DeferredResult<String> auditing(final HttpServletRequest request) {
        final DeferredResult<String> deferredResult = new DeferredResult<String>();
        final BaseResult baseResult = new BaseResult();
        final AuditingRequest auditingRequest = (AuditingRequest) getClassFromRequest(request,
            AuditingRequest.class, deferredResult);
        if (auditingRequest == null)
            return deferredResult;
        processWithQuery(auditingRequest, baseResult, deferredResult, new BusinessProcess() {
            public void doProcess() throws Exception {

                if (StringUtils.isBlank(auditingRequest.getToken())
                    || StringUtils.isBlank(auditingRequest.getLevel())
                    || StringUtils.isBlank(auditingRequest.getStandards())
                    || StringUtils.isBlank(auditingRequest.getDetail())
                    || StringUtils.isBlank(auditingRequest.getType())
                    || StringUtils.isBlank(auditingRequest.getResult())) {
                    throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "请求参数有误!");
                }

                Users users = new Users();
                users.setUserName(getUser(auditingRequest.getToken()));
                Users user = usersMapper.queryByUser(users);

                //auditingRequest.setUserDepartment(user.getDepartment());
                //auditingRequest.setUserRole(user.getRole());
                Rule rule = getRule(AuditingStrategyEnum.DEPEDENT);
                if (rule.auditing(auditingRequest, baseResult)) {
                    buildSuccessResult(baseResult, deferredResult);
                }

                deferredResult.setResult(JSONObject.toJSONString(baseResult));
            }
        });
        return deferredResult;
    }
}
