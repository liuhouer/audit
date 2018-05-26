package com.auditing.work.auditing;

import com.auditing.work.modle.AuditingRequest;
import com.auditing.work.result.BaseResult;

public interface Rule {

    public boolean auditing(AuditingRequest request, BaseResult result);
}
