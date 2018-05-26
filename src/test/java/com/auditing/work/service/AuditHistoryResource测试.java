package com.auditing.work.service;

import java.net.URISyntaxException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.auditing.work.controller.AuditHistoryResource;
import com.auditing.work.jpa.dao.AuditHistoryDetailDao;
import com.auditing.work.jpa.po.AuditHistory;

@DirtiesContext
@ContextConfiguration(locations = { "/META-INF/spring/root-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public class AuditHistoryResource测试  extends AbstractTransactionalJUnit4SpringContextTests{
	@Autowired AuditHistoryResource auditHistoryResource;
	  @Autowired
	    private AuditHistoryDetailDao auditHistoryDetailDao;
	
	@Test
	public void testStartAuditTrue() throws URISyntaxException{
		ResponseEntity<AuditHistory> res = auditHistoryResource.startAudit(true);
		System.out.println(res.getBody());
	}
	
	@Test
	public void testStartAudit() throws URISyntaxException{
		ResponseEntity<AuditHistory> res = auditHistoryResource.startAudit(false);
		System.out.println(res.getBody());
	}
	
	@Test
	public void testStop() throws URISyntaxException{
		ResponseEntity<AuditHistory> res = auditHistoryResource.startAudit(false);
		System.out.println(res.getBody());
		 res = auditHistoryResource.stopAudit();
			System.out.println(res.getBody());
			
			auditHistoryDetailDao.findAll().forEach(System.out::println);	
	}
	
	@Test
	public void testStartAuditAndNoStop() throws URISyntaxException{
		ResponseEntity<AuditHistory> res = auditHistoryResource.startAudit(false);
		System.out.println(res);
		res = auditHistoryResource.startAudit(false);
		System.out.println(res);
	}
}
