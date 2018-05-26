package com.auditing.work.modle.vo;

import java.util.List;

import com.auditing.work.jpa.po.AuditHistory;

public class TwoAuditHistory {
	public List<TwoAuditHistoryDetail> auditHistoryDetails;
	public AuditHistory auditHistoryA;
	public AuditHistory auditHistoryB;
}
