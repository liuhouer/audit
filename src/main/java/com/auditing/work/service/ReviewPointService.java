package com.auditing.work.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auditing.work.constants.Constants;
import com.auditing.work.dal.daointerface.DepartmentMapper;
import com.auditing.work.dal.daointerface.FirstCategoryMapper;
import com.auditing.work.dal.daointerface.FourthCategoryMapper;
import com.auditing.work.dal.daointerface.ReviewPointMapper;
import com.auditing.work.dal.daointerface.SecondCategoryMapper;
import com.auditing.work.dal.daointerface.ThirdCategoryMapper;
import com.auditing.work.dal.daointerface.UsersMapper;
import com.auditing.work.dal.dataobject.Department;
import com.auditing.work.dal.dataobject.FirstCategory;
import com.auditing.work.dal.dataobject.FourthCategory;
import com.auditing.work.dal.dataobject.ReviewPoint;
import com.auditing.work.dal.dataobject.ReviewPointsReturn;
import com.auditing.work.dal.dataobject.SecondCategory;
import com.auditing.work.dal.dataobject.ThirdCategory;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.jpa.dao.DepRevierPointStautsRepository;
import com.auditing.work.jpa.dao.ReviewPointDocRepository;
import com.auditing.work.jpa.po.DepRevierPointStauts;
import com.auditing.work.modle.jf.FourthcatDepRelation;
import com.auditing.work.modle.vo.FirstCategoryStatisticVo;
import com.auditing.work.modle.vo.FourthcategoryReviewDep;
import com.auditing.work.modle.vo.FourthcategoryReviewDepVo;
import com.auditing.work.modle.vo.ReviewPointDepStatisticVo;
import com.auditing.work.modle.vo.ScoreStatisticVo;
import com.auditing.work.modle.vo.XlsCellVo;
import com.auditing.work.util.CacheHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@Service
public class ReviewPointService {
    @Autowired
    protected ReviewPointMapper reviewPointMapper;

    @Autowired
    protected FirstCategoryMapper firstCategoryMapper;

    @Autowired
    protected SecondCategoryMapper secondCategoryMapper;

    @Autowired
    protected ThirdCategoryMapper thirdCategoryMapper;

    @Autowired
    protected FourthCategoryMapper fourthCategoryMapper;
    
    @Autowired
    protected UsersMapper usersMapper;
    
    @Autowired
    protected DepartmentMapper departmentMapper;
	@Autowired
	   ReviewPointDocRepository reviewPointDocRepository;
	@Autowired
	DepRevierPointStautsRepository depRevierPointStautsRepository;
	@Autowired
	CacheHelper cacheHelper;
	
	public  ReviewPoint setStatus(ReviewPoint reviewPoint){
		
		 
		if (reviewPoint.getDepRevierPointStautsList().isEmpty()) {
			reviewPoint.setStatus(Constants.UNREVIEWED);
			return reviewPoint;
		}
		
		if (reviewPoint.getDepRevierPointStautsList().stream().allMatch(tmp->{
				return tmp.getStatus().equals(Constants.UNREVIEWED);
			})) {
			reviewPoint.setStatus(Constants.UNREVIEWED);
			return reviewPoint;
		} 
		
		if (reviewPoint.getDepRevierPointStautsList().stream().allMatch(tmp->{
			return tmp.getStatus().equals(Constants.REVIEWED);
		})) {
			reviewPoint.setStatus(Constants.REVIEWED);
			return reviewPoint;
		} 
		reviewPoint.setStatus(Constants.REJECT);
		return reviewPoint;
	} 
	
	public List<XlsCellVo> getXlsCellVoList() throws Exception{
		List<XlsCellVo> list = Lists.newArrayList();
		
		
		 List<FirstCategory> firsts = firstCategoryMapper.selectAllFirstCategory();
         for (FirstCategory first: firsts) {
        	 	
     			list.add(new XlsCellVo(first.getName()));
     			
             List<SecondCategory> seconds = secondCategoryMapper.selectAllSecondCategorybyFirst(first.getId(),null);
             for (SecondCategory second: seconds) {
      
      			list.add(new XlsCellVo(second.getName()));
      			XlsCellVo ps = new XlsCellVo();
      			ps.setCategoryName("评审标准");
      			ps.setReviewPointName("评审要点");     
//      			ps.setDepName("任务分解");
      			ps.setDepName("任务分解");
      			list.add(ps);
      			List<ThirdCategory> thirds = thirdCategoryMapper.selectAllThirdCategoryBySecond(second.getId(),null);
                for (ThirdCategory third: thirds) { 
             
          			list.add(new XlsCellVo(third.getName()));
						List<FourthCategory> fourths = fourthCategoryMapper.selectAllFourthCategoryByThird(third.getId(),null);                  
                      for (FourthCategory fourth: fourths) {
                    	 
              			list.add(new XlsCellVo(fourth.getName()));                	  
                           List<ReviewPoint> reviewPoints = reviewPointMapper.selectAllReviewPointByFourthId(fourth.getId());
                           
                           
                           for (Integer i = 0; i < reviewPoints.size(); i++) {
                        	    ReviewPoint reviewPoint = reviewPoints.get(i);
                        	    
                        	    
                        	    if(i == 0){
                        	    	list.get(list.size()-1).setReviewPointName(reviewPoint.getTitle());
                        	    }
                        	    if(i !=0 && StringUtils.isNotBlank(reviewPoint.getTitle()) ){
                        	    	XlsCellVo title = new XlsCellVo();
                        	    	title.setReviewPointName(reviewPoint.getTitle());              
                         			list.add(title);
                        	    }
                        	    
                        	    
                        	    XlsCellVo reviewPointe = new XlsCellVo();
	                       	    reviewPointe.setId(reviewPoint.getId());
	                       	    reviewPointe.setReviewPointName(reviewPoint.getDetail()); 
//	                       	    reviewPointe.setDepName(reviewPoint.getDepartment_id());
	                       	 reviewPointe.setDepName("");
                    			list.add(reviewPointe);
                           }
                          
                           
                      }
                  }
      			
             }
         }
		
		return list;
	}
	
	
    public List<FirstCategoryStatisticVo> getFirstCategoryStatisticVoList() throws Exception{
    	Multimap<String, ReviewPoint> multimap = HashMultimap.create();
    	
    	  List<FirstCategory> firsts = firstCategoryMapper.selectAllFirstCategory();
          for (FirstCategory first: firsts) {
        	 
              List<SecondCategory> seconds = secondCategoryMapper.selectAllSecondCategorybyFirst(first.getId(),null);
              for (SecondCategory second: seconds) {
                  List<ThirdCategory> thirds = thirdCategoryMapper.selectAllThirdCategoryBySecond(second.getId(),null);
                  for (ThirdCategory third: thirds) {             
						List<FourthCategory> fourths = fourthCategoryMapper.selectAllFourthCategoryByThird(third.getId(),null);                  
                      for (FourthCategory fourth: fourths) {
                           List<ReviewPoint> reviewPoints = reviewPointMapper.selectAllReviewPointByFourthId(fourth.getId());
                           multimap.putAll(first.getLevel(), reviewPoints);
                      }
                  }
              }
          }
          List<FirstCategoryStatisticVo> result = Lists.newArrayList();
          multimap.asMap().forEach((key,value)->{
        	  FirstCategoryStatisticVo vo = new FirstCategoryStatisticVo();
        	  vo.level = key;
        	  vo.total = value.size();
        	  vo.reviewedNumber = (int) value.stream().filter(rp->{
          		return  rp.getStatus().equals(Constants.REVIEWED);
          	  }).count();
        	  vo.unreviewedNumber =  vo.total-  vo.reviewedNumber;
        	  vo.percentageComplete = 
        			  NumberUtil.getBaiFengBi( vo.reviewedNumber,  vo.total);
        	  result.add(vo);
          });
    	
    	return result;
    }
    
    
    
    public ScoreStatisticVo getFourthCategoriesScoreStatisticVo(){
    	 List<FourthCategory> fourthCategories = null;
//    	List<ReviewPoint> reviewPoints = null;
		try {
			fourthCategories = fourthCategoryMapper.selectAll();
//			reviewPoints = reviewPointMapper.selectAllReviewPoint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return getScoreStatisticVo(fourthCategories);
    }
    
    public ScoreStatisticVo getKeyFourthCategoriesScoreStatisticVo(){
    	 List<FourthCategory> fourthCategories = fourthCategoryMapper.selectKeyList();
    	 return getScoreStatisticVo(fourthCategories);
    }

	private ScoreStatisticVo getScoreStatisticVo(List<FourthCategory> fourthCategories) {
		ScoreStatisticVo scoreStatisticVo = new ScoreStatisticVo();
    	scoreStatisticVo.aNumber = (int) fourthCategories.stream().filter(rp->{
    		return rp.getTotal_score().equals("A");
    	}).count();
    	scoreStatisticVo.bNumber =  (int) fourthCategories.stream().filter(rp->{
    		return rp.getTotal_score().equals("B") ||  rp.getTotal_score().equals("A");
    	}).count();
    	scoreStatisticVo.cNumber = (int) fourthCategories.stream().filter(rp->{
    		return rp.getTotal_score().equals("C") ||  rp.getTotal_score().equals("B") ||  rp.getTotal_score().equals("A");
    	}).count();
    	scoreStatisticVo.dNumber = (int) fourthCategories.stream().filter(rp->{
    		return rp.getTotal_score().equals("D") ||  rp.getTotal_score().equals("C") ||  rp.getTotal_score().equals("B") ||  rp.getTotal_score().equals("A");
    	}).count();
    	

    	scoreStatisticVo.aPercentageComplete = 
    			NumberUtil.getBaiFengBi(scoreStatisticVo.aNumber, fourthCategories.size());
    	scoreStatisticVo.bPercentageComplete = 
    			NumberUtil.getBaiFengBi(scoreStatisticVo.bNumber, fourthCategories.size());
    	scoreStatisticVo.cPercentageComplete = 
    			NumberUtil.getBaiFengBi(scoreStatisticVo.cNumber, fourthCategories.size());
    	scoreStatisticVo.dPercentageComplete = 
    			NumberUtil.getBaiFengBi(scoreStatisticVo.dNumber, fourthCategories.size());
    	scoreStatisticVo.totalNumber =  fourthCategories.size();
    	return scoreStatisticVo;
	}
    
    public List<ReviewPointDepStatisticVo> getDepInfo() {
    	Map<String, ReviewPointDepStatisticVo> map = Maps.newHashMap();
    	
    	List<ReviewPoint> reviewPoints = null;
		try {
			reviewPoints = reviewPointMapper.selectAllReviewPoint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	reviewPoints.forEach(reviewPoint->{
    		List<String> depList = Lists.newArrayList(
    				StringUtils.split( reviewPoint.getDepartment_id(), ",")
    			) ;
    		
    		setDepView(map, reviewPoint, depList);
    	});
    	map.values().forEach(reviewPointDepStatisticVo->{
    		
    		int diliverNum = reviewPointDepStatisticVo.reviewedNumber;
			int queryMailNum = reviewPointDepStatisticVo.reviewedNumber+reviewPointDepStatisticVo.unreviewedNumber
					+reviewPointDepStatisticVo.rejectNumber+reviewPointDepStatisticVo.rejectToComfirmedNumber;
			reviewPointDepStatisticVo.percentageComplete = 
    				 NumberUtil.getBaiFengBi(diliverNum, queryMailNum);
    	});
    	return Lists.newArrayList(map.values());
    }

	private void setDepView(Map<String, ReviewPointDepStatisticVo> map, ReviewPoint reviewPoint, List<String> depList) {
		
		
		depList.forEach(dep->{
			ReviewPointDepStatisticVo depStatisticVo = map.get(dep) ;
			if (  depStatisticVo ==  null ) {
				depStatisticVo = new ReviewPointDepStatisticVo();
				depStatisticVo.depName =dep;   				
			}
//			System.out.println(reviewPoint.getId()+" : "+reviewPoint.getStatus());
			
			switch (reviewPoint.getStatus()) {
				case 0:
					
					depStatisticVo.unreviewedNumber++;
					break;
				case 1:
					depStatisticVo.reviewedNumber++;
					break;
				case 2:
					depStatisticVo.rejectNumber++;
					break;
				case 3:
					depStatisticVo.rejectToComfirmedNumber++;
					break;
				default:
					break;
				}
				
			map.put(depStatisticVo.depName, depStatisticVo);
			
		});
	}
	
	public List<ReviewPointsReturn> query(
			String fourthCategoryUserName, 
			String userName, 
			String key, 
			String dep, 
			Integer firstCategoryId, 
			Integer fourthCategoryId,
			Integer page, 
			Integer size,
			Integer status) throws Exception{
		Integer startRow = page*size;
        Users queryUsers = new Users();
        queryUsers.setUserName(userName);
        Users user = usersMapper.queryByUser(queryUsers);
        Integer depid = null;
        if (user.getRole() != Constants.SUPER_ADMIN) {
        	depid = user.getDepartment_id();
            if (StringUtils.isBlank(dep)) {
            	
                Department department = departmentMapper.selectDepartmentById(user.getDepartment_id());  
                dep = department.getName();
			} 
        }
        
        List<ReviewPointsReturn> list = null;
        if(user.getRole() == Constants.USER){
        	 list = fourthCategoryMapper.query(key,userName,startRow,size,firstCategoryId,fourthCategoryId,dep,depid,status);
        }else{
        	 list = fourthCategoryMapper.query(key,fourthCategoryUserName,startRow,size,firstCategoryId,fourthCategoryId,dep,depid,status);
        }
		
		
        for (ReviewPointsReturn reviewPointsReturn : list) {
//        	String auditUserName = reviewPointsReturn.getUserName();
        	int id4 = reviewPointsReturn.getId4();
        	String auditUserName = null;
        	if(depid!=null) {
	        	FourthcatDepRelation relation = FourthcatDepRelation.dao.findById(id4, depid);
	        	if(relation!=null) {
	        		auditUserName= relation.getStr("username");
		        	reviewPointsReturn.setUserName(auditUserName);
	        	} else {
	        		reviewPointsReturn.setUserName(null);
	        	}
        	}
        	 List<ReviewPoint> reviewPoints = reviewPointMapper.queryByFourthId(reviewPointsReturn.getId4());
        	 List<ReviewPoint> reviewPoints2 = new ArrayList<>();
             for (ReviewPoint reviewPoint : reviewPoints) {
            	 boolean flag = true;
            	 if(StringUtils.isNotBlank(dep)) {
            		 Record record = Db.findFirst("select * from dep_reviewpoint_status where dep_name = ? and review_point_id = ?", dep, reviewPoint.getId());
            		 if(status!=null && status==0 && record==null) flag = true;
            		 else if(status==null) flag = true;
            		 else if(status!=null && record!=null && status==record.getInt("status")) flag = true;
            		 else flag = false;
            	 }
            	 if(!flag) continue;
            	 reviewPoint.setDepartmentIdList(Lists.newArrayList(StringUtils.split(reviewPoint.getDepartment_id(), ",")));
            	 if(StringUtils.isNotBlank(auditUserName)) {
             		String departmentNameStr = reviewPoint.getDepartment_id();
             		String filterName = "";
             		String[] userNames = auditUserName.split(",");
             		for(String uName : userNames) {
             			Users u = cacheHelper.getCacheUserByName(uName);
             			String departmentName = u.getDepartmentName();
        			 		if(StringUtils.isNotBlank(dep) && departmentNameStr.contains(departmentName)) {
        			 			filterName += "," + uName; 
        			 		}
             		}
             		reviewPoint.setUserName(StringUtils.isBlank(filterName)?null:filterName.substring(1, filterName.length()));
//             		reviewPointsReturn.setUserName(StringUtils.isBlank(filterName)?null:filterName.substring(1, filterName.length()));
            	 }
            	 
            	 List<String> idList = reviewPoint.getDepartmentIdList();
            	 List<Department> departmentList = Lists.newArrayList();
            	 for(String name : idList) {
            		 Department department = cacheHelper.getCacheDepartmentByName(name);
            		 departmentList.add(department);
            	 }
            	 reviewPoint.setDepartmentList(departmentList);
            	 Boolean hasDoc =  reviewPointDocRepository.countByReviewPointId(reviewPoint.getId().longValue()) > 0;
       			 reviewPoint.setHasDoc(hasDoc);
       			 if (user.getRole() != Constants.SUPER_ADMIN) {					
       				 DepRevierPointStauts  depRevierPointStaut = depRevierPointStautsRepository.findByReviewPointIdAndDepName(reviewPoint.getId(), dep );
       				 if(depRevierPointStaut == null ){
       					 depRevierPointStaut = new  DepRevierPointStauts();
       					 depRevierPointStaut.setDepName(dep);
       					 depRevierPointStaut.setStatus(Constants.UNREVIEWED);
       				 }
//					reviewPoint.setStatus(depRevierPointStaut.getStatus());
					reviewPoint.getDepRevierPointStautsList().add(depRevierPointStaut);
       			} else {
       				for (String depName : reviewPoint.getDepartmentIdList()) {
				    	DepRevierPointStauts  depRevierPointStaut = depRevierPointStautsRepository.findByReviewPointIdAndDepName(reviewPoint.getId(), depName );
						if(depRevierPointStaut == null ){
							depRevierPointStaut = new  DepRevierPointStauts();
							depRevierPointStaut.setDepName(depName);
							depRevierPointStaut.setStatus(Constants.UNREVIEWED);
						}
						reviewPoint.getDepRevierPointStautsList().add(depRevierPointStaut);
       			 	}
//       				reviewPoint = this.setStatus(reviewPoint);
       			}
       			reviewPoints2.add(reviewPoint);
             }
             
             reviewPointsReturn.setReviewPoints(reviewPoints2);
		}
//        System.err.println(JSON.toJSONString(list));
		return list;
	}
	
	public Integer getTotalRow(
			String fourthCategoryUserName, 
			String userName, 
			String key, 
			String dep, 
			Integer firstCategoryId,
			Integer fourthCategoryId,
			Integer status) throws Exception{

        Users queryUsers = new Users();
        queryUsers.setUserName(userName);
        Users user = usersMapper.queryByUser(queryUsers);
        Integer depid = null;
        if (user.getRole() != Constants.SUPER_ADMIN) {
        	depid = user.getDepartment_id();
            if (StringUtils.isBlank(dep)) {
                 Department department = departmentMapper.selectDepartmentById(user.getDepartment_id());  
                 dep = department.getName();
                 depid = department.getId();
			}
        }
       if(user.getRole() == Constants.USER){
    	   return fourthCategoryMapper.querytotalRow(key,userName, firstCategoryId, fourthCategoryId, dep, depid,status);
       }else{
    	   return fourthCategoryMapper.querytotalRow(key,fourthCategoryUserName, firstCategoryId, fourthCategoryId, dep, depid,status);
       }
      
		
	}

	public FourthcategoryReviewDep getFourthcategoryReviewDep(Integer id) {
		// TODO Auto-generated method stub
		List<ReviewPoint> reviewPointlist = reviewPointMapper.queryByFourthId(id);
		Multimap<String, Integer> multimap = HashMultimap.create();
		List<FourthcategoryReviewDepVo> list = Lists.newArrayList();
		for (ReviewPoint reviewPoint : reviewPointlist) {
			for (String depName : reviewPoint.getDepartmentIdList()) {
				
				DepRevierPointStauts  depRevierPointStaut =   
   						depRevierPointStautsRepository.findByReviewPointIdAndDepName(reviewPoint.getId(), depName );
				if(depRevierPointStaut == null ){
					depRevierPointStaut = new  DepRevierPointStauts();
					depRevierPointStaut.setStatus(Constants.UNREVIEWED);
				}			
				multimap.put(depName, depRevierPointStaut.getStatus());
			}		
		}
		
		
		
		multimap.asMap().forEach((depName,statusList)->{
			FourthcategoryReviewDepVo fourthcategoryReviewDep = new FourthcategoryReviewDepVo();
			fourthcategoryReviewDep.depName = depName;
			
			
			if ( statusList.stream().allMatch(status->{
				return status.equals(Constants.UNREVIEWED);}) ){
				fourthcategoryReviewDep.type = 2;
			}else{
				fourthcategoryReviewDep.type = 1;
			}
			
			list.add(fourthcategoryReviewDep);
		});
		
		FourthcategoryReviewDep fourthcategoryReviewDep =
				new FourthcategoryReviewDep();
		fourthcategoryReviewDep.depList = list;
		if (fourthcategoryReviewDep.depList.isEmpty()) {
			fourthcategoryReviewDep.status = 2;
			return fourthcategoryReviewDep;
		}
		
		if (fourthcategoryReviewDep.depList.stream().allMatch(dep->{
			return dep.type == 1;
		}) ) {
			fourthcategoryReviewDep.status = 1;
			return fourthcategoryReviewDep;
		}else{
			fourthcategoryReviewDep.status = 2;
			return fourthcategoryReviewDep;
		}
		
		
	}









}
