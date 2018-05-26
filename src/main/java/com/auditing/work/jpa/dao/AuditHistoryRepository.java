package com.auditing.work.jpa.dao;

import org.springframework.data.jpa.repository.*;

import com.auditing.work.jpa.po.AuditHistory;

import java.util.List;

/**
 * Spring Data JPA repository for the AuditHistory entity.
 */
@SuppressWarnings("unused")
public interface AuditHistoryRepository extends JpaRepository<AuditHistory,Long> {

	AuditHistory findByEndTimeIsNull();
	Integer countByEndTimeIsNull();
}
