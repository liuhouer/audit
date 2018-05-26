package com.auditing.work.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springside.modules.utils.mapper.JsonMapper;

import com.auditing.work.constants.Constants;
import com.auditing.work.jpa.dao.DepRevierPointStautsRepository;
import com.auditing.work.modle.vo.UpdateReviewPointStatusVo;

@DirtiesContext
@ContextConfiguration(locations = { "/META-INF/spring/root-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public class ReviewPointController测试  extends AbstractTransactionalJUnit4SpringContextTests {
		@Autowired ReviewPointController reviewPointController;
		 @Autowired
		    DepRevierPointStautsRepository depRevierPointStautsRepository;
		 @Test
		 public void 更新要点状态() throws Exception{
			 
			 Integer id= 788;
			UpdateReviewPointStatusVo updateReviewPointStatusVo = new UpdateReviewPointStatusVo();
			updateReviewPointStatusVo.passed = true;
			updateReviewPointStatusVo.userName = "love2";
			updateReviewPointStatusVo.status = Constants.REVIEWED;
			reviewPointController.updateReviewPointStatus(id, updateReviewPointStatusVo);
			 System.out.println(
					 JsonMapper.defaultMapper().toJson(depRevierPointStautsRepository.findAll())
					 );;
		 } 
}
