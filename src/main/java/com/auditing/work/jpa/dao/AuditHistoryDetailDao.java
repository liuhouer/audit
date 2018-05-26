package com.auditing.work.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auditing.work.jpa.po.AuditHistoryDetail;

public interface AuditHistoryDetailDao extends JpaRepository<AuditHistoryDetail,Long>{
	AuditHistoryDetail findByFourthCategoryIdAndAuditHistoryId(Integer fourthCategoryId, Long auditHistoryId);
}
