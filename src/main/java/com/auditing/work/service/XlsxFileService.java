package com.auditing.work.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.auditing.work.dal.daointerface.FirstCategoryMapper;
import com.auditing.work.dal.daointerface.FourthCategoryMapper;
import com.auditing.work.dal.daointerface.ReviewPointMapper;
import com.auditing.work.dal.daointerface.SecondCategoryMapper;
import com.auditing.work.dal.daointerface.ThirdCategoryMapper;
import com.auditing.work.dal.dataobject.FirstCategory;
import com.auditing.work.dal.dataobject.FourthCategory;
import com.auditing.work.dal.dataobject.ReviewPoint;
import com.auditing.work.dal.dataobject.SecondCategory;
import com.auditing.work.dal.dataobject.ThirdCategory;
import com.auditing.work.jpa.dao.DepDictRepository;
import com.auditing.work.jpa.dao.DepartmentDao;
import com.auditing.work.jpa.po.DepDict;
import com.auditing.work.jpa.po.Department;
import com.auditing.work.modle.jf.DepartmentModel;
import com.auditing.work.util.CacheHelper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XlsxFileService {
    @Autowired DepartmentDao departmentDao;
    @Autowired
    protected FirstCategoryMapper firstCategoryMapper;

    @Autowired
    protected SecondCategoryMapper secondCategoryMapper;

    @Autowired
    protected ThirdCategoryMapper thirdCategoryMapper;

    @Autowired
    protected FourthCategoryMapper fourthCategoryMapper;
    
    @Autowired
    protected ReviewPointMapper reviewPointMapper;
    
	Stack<FirstCategory> firstCategorieStack = new Stack<>();
	Stack<SecondCategory> secondCategorieStack = new Stack<>();
	Stack<ThirdCategory>  thirdCategorieStack = new Stack<>();
	Stack<FourthCategory> fourthCategorieStack = new Stack<>();
	Stack<ReviewPoint> reviewPointStack = new Stack<>();
	XSSFSheet sheet;
	XSSFWorkbook workbook;
	ExcelSheet excelSheet;
	
	@Autowired
    SysService sysService;
	
//	@Transactional
	public void read(XSSFWorkbook workbook) throws Exception {
		sysService.delAll();
		this.sheet  = workbook.getSheetAt(0);
		this.excelSheet = new ExcelSheet(workbook,  this.sheet);
		for (Integer rowIndex =  this.sheet.getFirstRowNum(); rowIndex <  this.sheet.getLastRowNum(); rowIndex++) {
			String text1 = excelSheet.getCellValue(rowIndex,0);
			String text2 = excelSheet.getCellValue(rowIndex,1);
			String text3 = excelSheet.getCellValue(rowIndex,2);
			String content  = text1 + text2 + text3;
			if(StringUtils.isBlank(content)){
				continue;
			}
			if(StringUtils.isBlank(excelSheet.getCellValue(rowIndex,0)) && excelSheet.getCellValue(rowIndex,1).contains("【")){
				continue;
			}
   
			if(isReviewPoint(rowIndex)){
				creatReviewPoint(rowIndex);
				continue;
			}
   
			if(isFirstCategorie(rowIndex)){		
//				   System.out.println(rowIndex);
				System.out.println("一级分类------" + rowIndex);
				creatFirstCategorie(rowIndex);
				continue;
			}
			if(isSecondCategorie(rowIndex)){
				creatSecondCategorie(rowIndex);
				continue;
			}
			if(isThirdCategorie(rowIndex)){
	
				creatThirdCategorie(rowIndex);
				continue;
		
			}
			if(isFourthCategorie(rowIndex)){
//					System.out.println(rowIndex);
				creatFourthCategorie(rowIndex);
				continue;
			}
	   
		}
		
	}



	private void creatReviewPoint(Integer rowIndex) throws Exception {
		 FourthCategory fourthCategory = fourthCategorieStack.peek();
		 FourthCategory fourth = fourthCategoryMapper.selectByLevel(fourthCategory.getLevel());
		 String title = "";
		 String type = "";
		 String lastCell = excelSheet.getCellValue(rowIndex-1,1);
		 if(lastCell.startsWith("【")){
			 title = lastCell;
			 type = getType(lastCell);
		 }else{
			 Integer index = rowIndex-2;
			 String typeStr ="";
			 while(true){
				 
				 typeStr = excelSheet.getCellValue(index,1);
				 if(typeStr.startsWith("【")){
					 break;
				 }
				 index--;
			 }
			 
			type = getType(typeStr);		 
		 }
		 
		  ReviewPoint reviewPoint = new ReviewPoint();
          reviewPoint.setStatus(0);
          reviewPoint.setFourth_id(fourth.getId());
          reviewPoint.setScore(type);
          reviewPoint.setTitle(title);
          reviewPoint.setDetail("");
          reviewPoint.setRemarks("");
          reviewPoint.setAttachment("");
          reviewPoint.setPassed(null);
//          reviewPoint.setDepartment_id(rowItem.getDepartment());
          reviewPoint.setIsEdit(true);
        
          reviewPoint.setDetail(excelSheet.getCellValue(rowIndex,1));
          
          String[] depNameArray =  StringUtils.split(excelSheet.getCellValue(rowIndex,2), "、");
          for (String depName : depNameArray) {
        	  DepartmentModel department = DepartmentModel.getCacheDepartmentByName(depName);
				if (department == null) {
					department = new DepartmentModel();
					department.set("name", depName);
					department.save();
				}
			}
          reviewPoint.setDepartment_id(StringUtils.join(depNameArray, ","));
          reviewPointMapper.insert(reviewPoint);
	}

	private String getType(String lastCell) {
		  if (lastCell.contains("【A】")) {
              return  "A";
          }
          if (lastCell.contains("【B】")) {
        	  return "B";
          }
          if (lastCell.contains("【C】")) {
        	  return "C";
          }
          return "";
	}



	private void creatFourthCategorie(Integer rowIndex) throws Exception {
		  String name = excelSheet.getCellValue(rowIndex,0);
		  ThirdCategory thirdCategory = this.thirdCategorieStack.peek();
		  ThirdCategory third = this.thirdCategoryMapper.selectByLevel(thirdCategory.getLevel());
	
		  FourthCategory fourthCategory = new FourthCategory();
		  if (null == third) {
			  throw new Exception("它没有三级目录");
                       
          }
		  if(this.fourthCategorieStack.isEmpty()){
			  fourthCategory.levelIndex = 1;
		  }else{
			  
			  FourthCategory last = fourthCategorieStack.peek();
//			  if(last.getThird_id() == third.getId()){
				  fourthCategory.levelIndex = last.levelIndex+1;
//			  }else{
//				  fourthCategory.levelIndex = 1;
//			  }
			
		  }
		  fourthCategory.setLevel(third.getLevel()+"."+fourthCategory.levelIndex);
		  fourthCategory.setName(name);
		  fourthCategory.setThird_id(third.getId());
		  fourthCategory.setRemarks("");
		  fourthCategory.setTotal_score("D");
          fourthCategoryMapper.insert(fourthCategory);
          fourthCategorieStack.push(fourthCategory);
		
	}



	private void creatThirdCategorie(Integer rowIndex) throws Exception {
		  String name = excelSheet.getCellValue(rowIndex,0);
		  SecondCategory secondCategory = this.secondCategorieStack.peek();
		  SecondCategory second = this.secondCategoryMapper.selectByLevel(secondCategory.getLevel());
	
		  ThirdCategory thirdCategory = new ThirdCategory();
		  if (null == second) {
              throw new Exception("它没有二级目录");
                       
          }
	
		  
		  if(this.thirdCategorieStack.isEmpty()){
			  thirdCategory.levelIndex = 1;
		  }else{
			  
			  ThirdCategory last = thirdCategorieStack.peek();
//			  if(last.getSecond_id() == second.getId()){
				  thirdCategory.levelIndex = last.levelIndex+1;  
//			  }else{
//				  thirdCategory.levelIndex = 1;
//			  }
			 
		  }
		  thirdCategory.setLevel(secondCategory.getLevel()+"."+thirdCategory.levelIndex);
		  thirdCategory.setName(name);
          thirdCategory.setSecond_id(second.getId());
          thirdCategory.setRemarks("");
          
//    	  System.out.println("add thirdCategory:"+thirdCategory.getLevel()+" : "+rowIndex+" : "+thirdCategory.getName());
          thirdCategoryMapper.insert(thirdCategory);
        
          thirdCategorieStack.push(thirdCategory);
	}



	private void creatSecondCategorie(Integer row) throws Exception {
		String cell1 = excelSheet.getCellValue(row,0);
		FirstCategory firstCategory = firstCategorieStack.peek();
		FirstCategory first = firstCategoryMapper.selectByLevel(firstCategory.getLevel());
        if (null == first) {
            throw new Exception("它没有一级类目");
        }
        SecondCategory secondCategory = new SecondCategory();
		if (secondCategorieStack.isEmpty()) {
           
           
            secondCategory.levelIndex = 1;
         
                    
		}else{
			 SecondCategory lastSecondCategory = secondCategorieStack.peek();
			 
//			if(lastSecondCategory.getFirst_id() == first.getId()){
				 secondCategory.levelIndex = lastSecondCategory.levelIndex+1;
//			}else{
//				  secondCategory.levelIndex = 1;
//			}
			 
			
	        
	        
		}
		 secondCategory.setLevel(first.getLevel()+"."+secondCategory.levelIndex);
		 secondCategory.setName(cell1);
         secondCategory.setFirst_id(first.getId());
         secondCategory.setRemarks("");
         secondCategoryMapper.insert(secondCategory);
         secondCategorieStack.push(secondCategory);
		
	}


	private void creatFirstCategorie(Integer row) throws Exception {
		String cell1 = excelSheet.getCellValue(row,0);
		System.out.println("一级分类-----" + cell1);
		  FirstCategory firstCategory = new FirstCategory();
		if (firstCategorieStack.isEmpty()) {			
			  firstCategory.levelIndex =1 ;
              firstCategory.setLevel("1");                         
		}else{
			  FirstCategory lastFirstCategory = firstCategorieStack.peek();		
			  firstCategory.levelIndex=lastFirstCategory.levelIndex+1;
	          firstCategory.setLevel(firstCategory.levelIndex.toString());	         	         
		}
		  firstCategory.setName(cell1);
          firstCategory.setRemarks("");
		  firstCategoryMapper.insert(firstCategory); 
          firstCategorieStack.push(firstCategory);
	}


	
	private boolean isReviewPoint(Integer rowIndex) {
		String cell1 = excelSheet.getCellValue(rowIndex,0);
	
		return StringUtils.isBlank(cell1) ;
	}

	
	private boolean isFirstCategorie(Integer row) {
		System.out.println(row + "-----" + excelSheet.getCellValue(row,0).matches("^第.章.+$"));
		return excelSheet.getCellValue(row,0).matches("^第.章.+$");
	}
	
//	public static void main(String[] args) {
//		System.out.println("第五章护理管理与质量持续改进".matches("^第.章.+$"));
//		System.out.println("1.1.2肿瘤临床科室一、二级诊疗科目设置、人员梯队与诊疗技术项目符合省级卫生行政部门规定的标准。".matches("^第.章.+$"));
//		System.out.println("".matches("^第.章.+$"));
//	}
	private boolean isSecondCategorie(Integer row) {
		
		return excelSheet.getCellValue(row+1,0).equals("评审标准");
	}
	
	private boolean isThirdCategorie(Integer row) {	
//		String cell1 = excelSheet.getCellValue(row,0);	
//		if(StringUtils.isBlank(cell1)){
//			return false;
//		}
//		String lastCell = excelSheet.getCellValue(row-1,0);
//		if(StringUtils.isBlank(lastCell)){
//			return true;
//		}
//		
//		if(lastCell.equals("评审标准")){
//			return true;
//		}
//		return false;
		return excelSheet.getCellValue(row,0).matches("^[0-9]+[.][0-9]+[.][0-9]+[^.].+$");
	}
	
	public static void main(String[] args) {
		String pattern = "^[0-9]+[.][0-9]+[.][0-9]+[^.].+$";
		System.out.println("1.1.1医院的功能、任务和定位明确，保持适度规模。".matches(pattern));
		System.out.println("1.1.1.1 医院的功能、任务和定位明确，保持适度规模，符合卫生行政部门规定肿瘤医院设置标准。".matches(pattern));
		System.out.println("评审标准".matches(pattern));
		System.out.println("一、医院设置、功能和任务符合区域卫生规划和医疗机构设置规划的定位和要求".matches(pattern));
		System.out.println("".matches(pattern));
		System.out.println("6.10.13按照《医疗卫生服务单位信息公开管理办法（试行）》规定，医院应向社会及患者公开信息。".matches(pattern));
	}
	private boolean isFourthCategorie(Integer row) {
		
	
		String cell1 = excelSheet.getCellValue(row,0);
		
		return StringUtils.isNotBlank(cell1) && isReviewPoint(row+1) ;
	}



	@Autowired
	DepDictRepository depDictRepository;
	
	
	 Set<String> getDepDict(){
		 
		 Set<String> set = Sets.newHashSet();
		 
		 Optional<DepDict> depDict =  depDictRepository.findAll().stream().findFirst();
		 
		 if (depDict.isPresent()) {
			  String[] depNameArray =  StringUtils.split(depDict.get().getName(), ",");
			 for (String depName : depNameArray) {
				 set.add(depName);
			}
		}
		 
//		 depDictRepository.findAll().forEach(data->{
//			 set.add(data.getName());
//		 });
		return set;
	}
	public void updateDep(HSSFWorkbook w) throws Exception {
		
		HSSFSheet sheet  = w.getSheetAt(0);
	    ExcelSheet excelSheet = new ExcelSheet(w, sheet);
		Map<Integer,ReviewPoint> reviewPointMap  = Maps.newHashMap();
		Set<String> depDict = getDepDict();
//		List<Department> list = departmentDao.findAll();
//		List<String> depDict = Lists.transform(list, new Function<Department, String>() {
//			@Override
//			public String apply(Department department) {
//				return department.getName();
//			}
//		});
//		System.out.println(depDict);
		  
		for (Integer rowIndex = sheet.getFirstRowNum();rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			if (StringUtils.isBlank(excelSheet.getCellValue(rowIndex,0))) {
				continue;
			}
			  
			String idStr = excelSheet.getCellValue(rowIndex,0);
			if (StringUtils.isBlank(idStr)) {
				String message = "第"+rowIndex+"行:空白";
		        throw new Exception(message);				  
			}
			Integer id = Integer.valueOf(idStr);
			ReviewPoint reviewPoint = reviewPointMapper.selectReviewPointById(id);
		    if (reviewPoint == null) {
		    	String message = "第"+rowIndex+"行:错误ID "+id;
		        throw new Exception(message);
			}
		    String depNameStr = excelSheet.getCellValue(rowIndex,3);
		       
		    if(StringUtils.isBlank(depNameStr)){
		    	String message = "第"+rowIndex+"行:空白部门 ";
	        	throw new Exception(message);
		    } 
		    
		    
	    	String[] depNameArray =  getDepNameArray(depNameStr, depDict);
			for (String depName : depNameArray) {	
				if (!depDict.contains(depName)) {
		    		String message = "第"+rowIndex+"行:错误部门 "+depName;
			        throw new Exception(message);
		    	}
				Department department = this.departmentDao.findByName(depName);
				if (department == null) {
					department = new Department();
					department.setName(depName);
					this.departmentDao.save(department);
				}
			}
		    
		      
			String depnamestr = StringUtils.join(depNameArray, ",");
			reviewPoint.setDepartment_id(depnamestr);
			System.out.println(id + "-----" + depnamestr);
			try {
				reviewPointMapper.updateReviewPoint(reviewPoint);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			      
//		    String[] depNameArray =  getDepNameArray(depNameStr);
//		    for (String depName : depNameArray) {
//		    	if (!depDict.contains(depName)) {
//		    		String message = "第"+rowIndex+"行:错误部门 "+depName;
//			        throw new Exception(message);
//		    	}
//        	  
//				Department department = this.departmentDao.findByName(depName);
//				if (department == null) {
//					department = new Department();
//					department.setName(depName);
//					this.departmentDao.save(department);
//				}
//			}
//		    reviewPoint.setDepartment_id(StringUtils.join(depNameArray, ","));
//		    reviewPointMap.put(id, reviewPoint);
		}
		  
//		List<ReviewPoint> dataList = reviewPointMapper.selectAllReviewPoint();
//		for (ReviewPoint reviewPoint : dataList) {
//			if (!reviewPointMap.containsKey(reviewPoint.getId())) {
//				String message = "要点缺失：  "+reviewPoint.getDetail();
//				throw new Exception(message);
//			}
//		}
//		departmentDao.deleteAll();
//		for(Map.Entry<Integer,ReviewPoint> entry : reviewPointMap.entrySet()) {
//			
//			ReviewPoint t = entry.getValue();
//			System.out.println(entry.getKey() + "----------" + t.getDepartment_id());
//			
////			System.out.println(t.getId()+":"+t.getDepartment_id());  
//			String[] depNameArray =  getDepNameArray(t.getDepartment_id());
//			for (String depName : depNameArray) {	        	  
//				Department department = this.departmentDao.findByName(depName);
//				if (department == null) {
//					department = new Department();
//					department.setName(depName);
//					this.departmentDao.save(department);
//				}
//			}  
//			try {
//				reviewPointMapper.updateReviewPoint(t);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	private String[] getDepNameArray(String depNameStr, Set<String> depDict) {
		
		if(depNameStr.equals("所有部门")) {
			String[] strs = new String[depDict.size()];
			int i=0;
			for(String s : depDict) {
				strs[i++] = s;
			}
			return strs;
		}
		
		
		if (depNameStr.contains(",")) {
			return StringUtils.split(depNameStr, ",");
		}
		
		if (depNameStr.contains("，")) {
			return StringUtils.split(depNameStr, "，");
		}
		if (depNameStr.contains("、")) {
			return StringUtils.split(depNameStr, "、");
		}
		
		return StringUtils.split(depNameStr, ",");
	}


	
	
}
