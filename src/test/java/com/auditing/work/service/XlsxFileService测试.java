package com.auditing.work.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.function.Consumer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.ResourceUtils;

import com.auditing.work.dal.dataobject.FourthCategory;
import com.auditing.work.dal.dataobject.ReviewPoint;
import com.auditing.work.dal.dataobject.SecondCategory;
import com.auditing.work.dal.dataobject.ThirdCategory;

@DirtiesContext
@ContextConfiguration(locations = { "/META-INF/spring/root-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager")
public class XlsxFileService测试 extends AbstractTransactionalJUnit4SpringContextTests {
	 @Autowired
	    XlsxFileService xlsxFileService;
	  @Autowired
	    SysService sysService;
	 Consumer<? super ReviewPoint> showReviewPoint = (reviewPoint)->{
		 System.out.println(reviewPoint);
	 };
	 
	 
	 Consumer<? super FourthCategory> showFourth = (fourth)->{
		 System.out.println(fourth);
		 xlsxFileService.reviewPointMapper.queryByFourthId(fourth.getId()).forEach(showReviewPoint );
	 };
	 
     Consumer<? super ThirdCategory> showThird = (third)->{
    	 System.out.println(third);
    	 try {
		
			xlsxFileService.fourthCategoryMapper.selectAllFourthCategoryByThird(third.getId(), null)
			.forEach(showFourth);;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     };
     
     Consumer<? super SecondCategory> showSecondCategory = (secondCategory)->{
    	 System.out.println(secondCategory);
    	 
    	 try {
			xlsxFileService.thirdCategoryMapper.selectAllThirdCategoryBySecond(secondCategory.getId(), null)
			.forEach(showThird);;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
     };

     @Test
	 public void 解析新文档更新部门() throws Exception{
    	 File file = ResourceUtils.getFile("classpath:审核要点数据1.xls"); 
    	 HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
    	 
    	 xlsxFileService.updateDep(workbook);
     }
     
     
	 @Test
	 public void test解析新文档() throws Exception{
		 sysService.delAll();
		 String path = "G:/work/audit/testFile/import.xlsx";
		
		XSSFWorkbook w = new XSSFWorkbook(path );
         
         xlsxFileService.read(w);
         
     	
         xlsxFileService.departmentDao.findAll().forEach(dep->{
        	 System.out.println(dep);
         });
         
         xlsxFileService.firstCategoryMapper.selectAllFirstCategory()
          .stream().filter(f->{
        	  return  f.getId() > 10;
          })
         .forEach(data->{
        	 System.out.println(data);
        	 
        	 try {
				
				xlsxFileService.secondCategoryMapper.selectAllSecondCategorybyFirst(data.getId(), null)
				.forEach(showSecondCategory);;
			} catch (Exception e) {
			
				e.printStackTrace();
			}
     	 
        	 
         });
         
       
        
	 }
	 
	 
    
}
