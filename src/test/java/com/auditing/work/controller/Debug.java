package com.auditing.work.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.client.RestTemplate;

import com.auditing.work.jpa.dao.ReviewPointActionLogDao;
import com.auditing.work.service.ReviewPointMessageService;
@DirtiesContext
@ContextConfiguration(locations = { "/META-INF/spring/root-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public class Debug extends AbstractTransactionalJUnit4SpringContextTests {
	RestTemplate restTemplate = new RestTemplate();
	@Autowired
	ReviewPointActionLogDao reviewPointActionLogDao;
	@Autowired
	ReviewPointMessageService reviewPointMessageService;
	

	@Test
	public void test(){
		 System.out.println(reviewPointActionLogDao.findAll().size());
	}
	@Test
	public void test2(){
		 
		 
		reviewPointMessageService.queryNoReadActionLog(67).forEach(data->{
			 
			 System.out.println(data+"  list:"+data.getReviewPointCommentList());
			 
		 });
	
	}
	

}
