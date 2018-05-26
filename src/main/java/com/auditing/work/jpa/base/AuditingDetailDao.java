package com.auditing.work.jpa.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auditing.work.jpa.po.AuditingDetail;

public interface AuditingDetailDao extends JpaRepository<AuditingDetail,Integer> {

}
