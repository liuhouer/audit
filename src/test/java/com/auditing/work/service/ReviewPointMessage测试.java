package com.auditing.work.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.auditing.work.constants.Constants;
import com.auditing.work.dal.daointerface.ReviewPointMapper;
import com.auditing.work.dal.daointerface.UsersMapper;
import com.auditing.work.dal.dataobject.ReviewPoint;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.jpa.dao.ReviewPointActionLogDao;
import com.auditing.work.jpa.dao.ReviewPointMessageDao;

@DirtiesContext
@ContextConfiguration(locations = { "/META-INF/spring/root-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public class ReviewPointMessage测试 extends AbstractTransactionalJUnit4SpringContextTests {
		@Autowired 
		ReviewPointMessageService reviewPointMessageService;
		  @Autowired
		    protected UsersMapper usersMapper;
		  @Autowired
		    private ReviewPointActionLogDao reviewPointActionLogDao;
		    @Autowired
		    protected ReviewPointMapper reviewPointMapper;
		    @Autowired
			 private ReviewPointMessageDao reviewPointMessageDao;
		@Test
		public void testAddActionLong(){
				
			   Integer id = 34;
			   ReviewPoint reviewPoint = reviewPointMapper.selectReviewPointById(id);
	            ReviewPoint newReviewPoint = new ReviewPoint();
	            newReviewPoint.setId(id);
	            newReviewPoint.setDepartment_id(reviewPoint.getDepartment_id());	         
	            newReviewPoint.setStatus(Constants.REJECT);
	            newReviewPoint.setPassed(false);
	                Users queryUsers = new Users();
	                queryUsers.setUserName("superadmin");
	                Users user = usersMapper.queryByUser(queryUsers);
	          
			  reviewPointMessageService.addActionLog(newReviewPoint, user);
			  
			  
			  reviewPointActionLogDao.findAll().forEach(data->{
				  System.out.println(data);
			  });
			  
			  reviewPointMessageDao.findAll().forEach(data->{
				  System.out.println(data);
			  });
			  
			
		}

		@Test
		public void testQueryUser(){
			usersMapper.selectLisByRoleAndDepName(Constants.USER, "QA").forEach(data->{
				System.out.println(data.getUserName());
			});
		}
		
	
}	
