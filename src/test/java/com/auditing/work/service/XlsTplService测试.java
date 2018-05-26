package com.auditing.work.service;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@DirtiesContext
@ContextConfiguration(locations = { "/META-INF/spring/root-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public class XlsTplService测试 extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired  XlsTplService xlsTplService;
	
	
	@Test
	public void 获取任务概览导出结果(){
		for (Map<String, Object> data : xlsTplService.getXlsFourthcategoryViewList()) {
			System.out.println(data);
		}
	}
} 
