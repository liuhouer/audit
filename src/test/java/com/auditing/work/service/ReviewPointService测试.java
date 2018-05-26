package com.auditing.work.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.auditing.work.modle.vo.FourthcategoryReviewDep;
import com.auditing.work.modle.vo.FourthcategoryReviewDepVo;
import com.auditing.work.modle.vo.ReviewPointDepStatisticVo;
import com.auditing.work.modle.vo.XlsCellVo;

@DirtiesContext
@ContextConfiguration(locations = { "/META-INF/spring/root-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public class ReviewPointService测试 extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired ReviewPointService reviewPointService;
	
	
	@Test
	public void 查询四级类目部门() throws Exception{
		FourthcategoryReviewDep fourthcategoryReviewDep = 
				reviewPointService.getFourthcategoryReviewDep(95);
		System.out.println(fourthcategoryReviewDep.status);
		List<FourthcategoryReviewDepVo> list =fourthcategoryReviewDep.depList ;
		for (FourthcategoryReviewDepVo data : list) {
			System.out.println(data);
		}
	}
	
	
	@Test
	public void 查询Xls要点() throws Exception{
		List<XlsCellVo> list = reviewPointService.getXlsCellVoList();
		for (XlsCellVo xlsCellVo : list) {
			System.out.println(xlsCellVo);
		}
	}
	

	@Test
	public void 部门统计() throws Exception{
		List<ReviewPointDepStatisticVo> list = reviewPointService.getDepInfo();
		for (ReviewPointDepStatisticVo data : list) {
			System.out.println(data);
		}
	}
	
	@Test
	public void testQuery() throws Exception{
		String fName = "superadmin";
		String userName = "superadmin";
		String key= "摄图网";
		String dep= "";
		Integer firstCategoryId =null;
		Integer page = 0;
		Integer size = 5;
		Integer status = null;
		reviewPointService.query(fName,userName, key, dep, firstCategoryId, null, page, size, status).forEach(data->{
			System.out.println(data.getId4()+" : "+data.getName4()+" : "+data.getReviewPoints().size());
		});;
	}
	
	@Test
	public void testtotalRow() throws Exception{
		String fName = "superadmin";
		String userName = "superadmin";
		String key= "";
		String dep= "";
		Integer firstCategoryId =null;
		System.out.println(reviewPointService.getTotalRow(fName,userName, key, dep, firstCategoryId, null, null));
	}
}
