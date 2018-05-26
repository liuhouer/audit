package com.auditing.work.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.auditing.work.jpa.po.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.alibaba.fastjson.JSONObject;
import com.auditing.work.constants.Constants;
import com.auditing.work.dal.daointerface.DepartmentMapper;
import com.auditing.work.dal.dataobject.Department;
import com.auditing.work.dal.dataobject.FourthCategory;
import com.auditing.work.dal.dataobject.ReviewPoint;
import com.auditing.work.dal.dataobject.ReviewPointsReturn;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.exception.WorkException;
import com.auditing.work.jpa.dao.DepRevierPointStautsRepository;
import com.auditing.work.jpa.dao.ReviewPointDocRepository;
import com.auditing.work.jpa.po.DepRevierPointStauts;
import com.auditing.work.modle.jf.DepReviewPointStatusModel;
import com.auditing.work.modle.jf.DepartmentModel;
import com.auditing.work.modle.jf.FourthcatDepRelation;
import com.auditing.work.modle.jf.ReviewPointModel;
import com.auditing.work.modle.jf.UserModel;
import com.auditing.work.modle.vo.ReviewedPointsPageVo;
import com.auditing.work.modle.vo.UpdateReviewPointStatusVo;
import com.auditing.work.result.BaseResult;
import com.auditing.work.service.ReviewPointMessageService;
import com.auditing.work.service.ReviewPointService;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * Created by innolab on 16-12-25.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@RequestMapping("/api")
@Api(description="评审 详情 API")
@Controller
public class ReviewPointController extends BaseController{
		@Autowired
	    ReviewPointDocRepository reviewPointDocRepository;
		@Autowired
	    ReviewPointService reviewPointService;
		@Autowired
	    DepartmentMapper departmentMapper;
		
//		  @RequestMapping(value = "/reviewpoints/editToFalse", method = RequestMethod.GET)
//		   @ApiOperation(value="所有ReviewPoint中的isEdit 设置为 false",notes = "所有ReviewPoint中的isEdit 设置为 false", httpMethod = "GET")
//		   @ResponseBody
//		   public BaseResult setNotEdit(){
//			     BaseResult baseResult = new BaseResult();
//			     reviewPointMapper.setNotEdit();
//			     baseResult.setData("设置成功");
//		         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
//		         baseResult.setSuccess(true);
//		         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
//		         return baseResult;
//		   }
//		  
//		  @RequestMapping(value = "/reviewpoints/editToTrue", method = RequestMethod.GET)
//		   @ApiOperation(value="所有ReviewPoint中的isEdit 设置为 True",notes = "所有ReviewPoint中的isEdit 设置为 True", httpMethod = "GET")
//		   @ResponseBody
//		   public BaseResult setEdit(				  
//				   @ApiParam(name = "isReset", value = "true : ReviewPoint中的passed ,status 设置为 0 ,false: 不变") @RequestParam("isReset") Boolean isReset){
//			     BaseResult baseResult = new BaseResult();
//			     reviewPointMapper.setEdit();
//			     if (isReset) {
//			    	 reviewPointMapper.stautsReset();
//				}
//			     baseResult.setData("设置成功");
//		         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
//		         baseResult.setSuccess(true);
//		         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
//		         return baseResult;
//		   }
		
	   @RequestMapping(value = "/reviewpoints/{id}", method = RequestMethod.GET)
	   @ApiOperation(value="获取评审详情信息",notes = "获取评审详情信息", httpMethod = "GET")
	   @ResponseBody
	   public BaseResult getView(@PathVariable("id") Integer id,@RequestParam(value="dep",required=false)String dep){
		     BaseResult baseResult = new BaseResult();
		     ReviewPoint  reviewPoint = reviewPointMapper.queryReviewPointById(id);
		     
		     if (StringUtils.isBlank(dep)) {
		    	 for (String depName : reviewPoint.getDepartmentIdList()) {
		    		 Department department = departmentMapper.selectDepartmentByName(depName);
			    	 DepRevierPointStauts  depRevierPointStaut = depRevierPointStautsRepository.findByReviewPointIdAndDepName(reviewPoint.getId(), depName );
			    	 if(depRevierPointStaut == null ){
			    		 depRevierPointStaut = new  DepRevierPointStauts();
			    		 depRevierPointStaut.setDepName(depName);
			    		 depRevierPointStaut.setStatus(Constants.UNREVIEWED);
			    		 depRevierPointStaut.setReviewPointId(id);
			    		 depRevierPointStaut.setDeptId(department.getId());
			    	 } else {
			    		 depRevierPointStaut.setDeptId(department.getId());
			    	 }
			    	 reviewPoint.getDepRevierPointStautsList().add(depRevierPointStaut);
				}
//		    	reviewPoint = this.reviewPointService.setStatus(reviewPoint);
			}else{
				DepRevierPointStauts  depRevierPointStaut = depRevierPointStautsRepository.findByReviewPointIdAndDepName(reviewPoint.getId(), dep );
				if(depRevierPointStaut == null ){
					depRevierPointStaut = new  DepRevierPointStauts();
					depRevierPointStaut.setDepName(dep);
					depRevierPointStaut.setStatus(Constants.UNREVIEWED);
				}
				reviewPoint.setStatus(depRevierPointStaut.getStatus());
			}
		     baseResult.setData(reviewPoint);
		     baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
		     baseResult.setSuccess(true);
		     baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
		     return baseResult;
	   }
	   
	   @RequestMapping(value = "/reviewpoints/{id}/info", method = RequestMethod.GET)
	   @ApiOperation(value="获取评审详情信息",notes = "获取评审详情信息", httpMethod = "GET")
	   @ResponseBody
	   public BaseResult getViewInfo(@PathVariable("id") Integer id) throws Exception{
		     BaseResult baseResult = new BaseResult();
		     Map<String, Object> data = Maps.newHashMap();
		     ReviewPoint reviewPoint = reviewPointMapper.queryReviewPointById(id);
		     
	    	 for (String depName : reviewPoint.getDepartmentIdList()) {
	    		 Department department = departmentMapper.selectDepartmentByName(depName);
		    	 DepRevierPointStauts  depRevierPointStaut =   
    						depRevierPointStautsRepository.findByReviewPointIdAndDepName(reviewPoint.getId(), depName );
					if(depRevierPointStaut == null ){
						depRevierPointStaut = new  DepRevierPointStauts();
						depRevierPointStaut.setDepName(depName);
						depRevierPointStaut.setStatus(Constants.UNREVIEWED);
						depRevierPointStaut.setReviewPointId(id);
						depRevierPointStaut.setDeptId(department.getId());
					} else {
						depRevierPointStaut.setDeptId(department.getId());
					}
					reviewPoint.getDepRevierPointStautsList().add(depRevierPointStaut);
			}
	    	
//	    	 reviewPoint = this.reviewPointService.setStatus(reviewPoint);
		    	
		    	 
		     
		     FourthCategory fourthCategory = this.fourthCategoryMapper.selectById(reviewPoint.getFourth_id());
		     data.put("reviewPoint", reviewPoint);
		     data.put("fourthCategory", fourthCategory);
		     baseResult.setData(data);
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return baseResult;
	   }
	   
	   
	   @RequestMapping(value = "/reviewpoints/dep/statistics", method = RequestMethod.GET)
	   @ApiOperation(value="获取部门统计信息",notes = "获取部门统计信息", httpMethod = "GET")
	   @ResponseBody
	   public BaseResult getStatisticsView(@RequestParam(value ="depName",required=false) String  depName){
		     BaseResult baseResult = new BaseResult();

		     if(StringUtils.isNotBlank(depName)) {
				 Map map = new HashMap();
				 map.put("unreviewedNumber", 0L);//未审核
				 map.put("reviewedNumber", 0L);//已审核
				 map.put("rejectNumber", 0L);//驳回
				 map.put("rejectToComfirmedNumber", 0L);//待确认重审
		    	 /**
		    	  * SELECT
						b.`status`,count(a.id)
					FROM
						`ReviewPoint` a
					LEFT JOIN dep_reviewpoint_status b ON a.id = b.review_point_id
					WHERE
						a.department_id like '%药学%' and (b.dep_name = '药学' or b.dep_name is null)
					group by b.`status`
		    	  */
		    	 
		    	 String sql = "select b.status, 0,count(a.id) as num from ReviewPoint a left join dep_reviewpoint_status b on a.id = b.review_point_id ";
		    	 sql += "where a.department_id like ? and (b.dep_name = ? or b.dep_name is null) group by b.status";
				 List<Record> list = Db.find(sql, "%" + depName + "%", depName);
		    	 for(Record record : list) {
		    		 Integer status = record.getInt("status");
		    		 long num = record.getLong("num");
		    		 if(status==null) map.put("unreviewedNumber", num);
		    		 else if(status==1) map.put("reviewedNumber", num);
		    		 else if(status==2) map.put("rejectNumber", num);
		    		 else if(status==3) map.put("rejectToComfirmedNumber", num);
		    	 }
		    	 map.put("depName", depName);
				 baseResult.setData(map);
		     } else {
		     	List<Department> departments = departmentMapper.selectAllDepartment();
				List<Map> mapList = new ArrayList<Map>();
				 String sql = "select b.status, 0,count(a.id) as num from ReviewPoint a left join dep_reviewpoint_status b on a.id = b.review_point_id ";
				 sql += "where a.department_id like ? and (b.dep_name = ? or b.dep_name is null) group by b.status";
				 List<Record> list = new ArrayList<Record>();

				for (Department department :departments){
					Map map = new HashMap();
					map.put("unreviewedNumber", 0L);//未审核
					map.put("reviewedNumber", 0L);//已审核
					map.put("rejectNumber", 0L);//驳回
					map.put("rejectToComfirmedNumber", 0L);//待确认重审
					map.put("depName", department.getName());
					List<Record> records = Db.find(sql, "%" + department.getName() + "%", department.getName());
					long totalnum = 0;
					long reviewedNumber = 0;
					for(Record record : records) {
						Integer status = record.getInt("status");
						Long num = record.getLong("num");
						num = (num==null?0:num);
						totalnum+=num;
						if(status==null) {
							map.put("unreviewedNumber", num);
						}else if(status==1){
							reviewedNumber =(num==null?0:num);
							map.put("reviewedNumber", num);
						}else if(status==2) {
							map.put("rejectNumber", num);
						}else if(status==3) {
							map.put("rejectToComfirmedNumber", num);
						}
					}
					float percentageComplete = 0;
					if (totalnum!=0){
						percentageComplete = (float)reviewedNumber/(float)totalnum;
					}
					percentageComplete = percentageComplete*100;
					System.out.println("--------->reviewedNumber:"+reviewedNumber+",totalnum="+totalnum);
					System.out.println("----------depname:"+department.getName()+",percentageComplete="+percentageComplete);
					DecimalFormat df = new DecimalFormat("######0");
					map.put("percentageComplete",df.format(percentageComplete));
					mapList.add(map);
				}
				 baseResult.setData(mapList);
//		    	 String sql = "select `status`, count(*) as num from ( ";
//		    	 sql += " select a.id, b.`status` from ReviewPoint a left join dep_reviewpoint_status b ";
//		    	 sql += " on a.id = b.review_point_id group by a.id, b.`status`) c group by `status`";
//		    	 List<Record> list = Db.find(sql);
//				 for(Record record : list) {
//		    		 Integer status = record.getInt("status");
//		    		 long num = record.getLong("num");
//		    		 if(status==null) map.put("unreviewedNumber", num);
//		    		 else if(status==1) map.put("reviewedNumber", num);
//		    		 else if(status==2) map.put("rejectNumber", num);
//		    		 else if(status==3) map.put("rejectToComfirmedNumber", num);
//				 }
			 }
		     baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return baseResult;
	   }
	   
	   @RequestMapping(value = "/reviewpoints/firstCategory/statistics", method = RequestMethod.GET)
	   @ApiOperation(value="获取一级分类统计信息",notes = "获取一级分类统计信息", httpMethod = "GET")
	   @ResponseBody
	   public BaseResult getFirstCategoryStatisticVoList() throws Exception{
		     BaseResult baseResult = new BaseResult();
		     baseResult.setData(reviewPointService.getFirstCategoryStatisticVoList());
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return baseResult;
	   }
	   
	   @RequestMapping(value = "/reviewpoints/score/statistics", method = RequestMethod.GET)
	   @ApiOperation(value="获取四级类目分数统计信息",notes = "获取四级类目分数统计信息", httpMethod = "GET")
	   @ResponseBody
	   public BaseResult getScoreStatisticVo(){
		     BaseResult baseResult = new BaseResult();
		     baseResult.setData(reviewPointService.getFourthCategoriesScoreStatisticVo());
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return baseResult;
	   }
	   
	   @RequestMapping(value = "/reviewpoints/score/keystatistics", method = RequestMethod.GET)
	   @ApiOperation(value="获取关键四级类目分数统计信息",notes = "获取关键分数统计信息", httpMethod = "GET")
	   @ResponseBody
	   public BaseResult geKeytScoreStatisticVo(){
		     BaseResult baseResult = new BaseResult();
		     baseResult.setData(reviewPointService.getKeyFourthCategoriesScoreStatisticVo());
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return baseResult;
	   }
	   
	   @RequestMapping(value = "/reviewpoints/list", method = RequestMethod.GET)
	   @ApiOperation(value="四级条目列表",notes = "四级条目列表", httpMethod = "GET")
	    public List<ReviewPointsReturn> reviewedPoints(
	    		@RequestParam(value ="fourthCategoryUserName",required=false) String  fourthCategoryUserName,
	    		@RequestParam("userName") String  userName,
	    		@RequestParam(value ="key",required=false) String  key,
	    		@RequestParam(value ="dep",required=false) String  dep,
	    		@RequestParam(value ="firstCategoryId",required=false) Integer  firstCategoryId,
	    		@RequestParam(value = "page", defaultValue = "0") Integer page,
	            @RequestParam(value = "size", defaultValue = "10") Integer size,
	            @RequestParam(value = "status", required = false)Integer status) throws Exception {
		   
		   
		   return reviewPointService.query(fourthCategoryUserName,userName,key,dep,firstCategoryId,null,page,size, status);
	   }
	   
	   @RequestMapping(value = "/reviewpoints/pageList", method = RequestMethod.GET)
	   @ApiOperation(value="四级条目列表",notes = "四级条目列表", httpMethod = "GET")
	    public ReviewedPointsPageVo reviewedPointsPage(
	    		@RequestParam(value ="fourthCategoryUserName",required=false) String  fourthCategoryUserName,
	    		@RequestParam("userName") String  userName,
	    		@RequestParam(value ="key",required=false) String  key,
	    		@RequestParam(value ="dep",required=false) String  dep,
	    		@RequestParam(value ="firstCategoryId",required=false) Integer  firstCategoryId,
	    		@RequestParam(value ="fourthCategoryId",required=false) Integer  fourthCategoryId,
	    		@RequestParam(value = "page", defaultValue = "0") Integer page,
	            @RequestParam(value = "size", defaultValue = "10") Integer size,
	            @RequestParam(value = "status", required = false) Integer status) throws Exception {
		   
		   ReviewedPointsPageVo reviewedPointsPageVo = new ReviewedPointsPageVo();
		   reviewedPointsPageVo.list = reviewPointService.query(fourthCategoryUserName,userName,key,dep,firstCategoryId,fourthCategoryId,page,size, status);
		   Integer totalRow = reviewPointService.getTotalRow(fourthCategoryUserName,userName, key, dep, firstCategoryId,fourthCategoryId, status);
		   Integer totalPage = totalRow/size;
		   if(totalRow%size != 0){
			   totalPage = totalPage+1;
		   }
		   reviewedPointsPageVo.totalPage = totalPage;
		   return reviewedPointsPageVo;
	   }
	   
	   

	   
	   @RequestMapping(value = "/reviewpoints/{id}/departmentId", method = RequestMethod.POST)
	   @ApiOperation(value="更新审批部门信息",notes = "更新审批部门信息", httpMethod = "POST")
	   @ResponseBody
	   public BaseResult updateReviewPointDep(@PathVariable("id") Integer id,@RequestBody String department_id) throws Exception{
		     BaseResult baseResult = new BaseResult();
		     ReviewPoint reviewPoint = reviewPointMapper.selectReviewPointById(id);
		     if (reviewPoint == null) {
		    	 baseResult.setData("无效ID");
		         baseResult.setResultCode("404");
		         baseResult.setSuccess(false);
		         baseResult.setResultMessage("无效ID");
		         return baseResult;
			 }
		     reviewPoint.setDepartment_id(department_id);
		     reviewPointMapper.updateReviewPoint(reviewPoint);
		     baseResult.setData("更新成功");
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return baseResult;
	   }
	
	   
	   
	   
	   
	   
	   
	   
    @RequestMapping(value = "/reviewpoints/status", method = RequestMethod.GET)
    public DeferredResult<String> getReviewed(@RequestParam(value ="dep",required=false) String  dep,
    		@RequestParam(value ="userName",required=false) String  userName,
    		final HttpServletRequest request) {
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        BaseResult baseResult = new BaseResult();
        try {

//            Map<String, Integer> status = new HashMap<String, Integer>();
//            Integer num = reviewPointMapper.countReviewPointByStatus(Constants.UNREVIEWED,dep,userName);
//            status.put(Constants.STR_UNREVIEWED, num);
//            num = reviewPointMapper.countReviewPointByStatus(Constants.REVIEWED,dep,userName);
//            status.put(Constants.STR_REVIEWED, num);
//            num = reviewPointMapper.countReviewPointByStatus(Constants.REJECT,dep,userName);
//            status.put(Constants.STR_REJECT, num);
//            num = reviewPointMapper.countReviewPointByStatus(Constants.REJECT_TO_COMFIRMED,dep,userName);
//            status.put(Constants.STR_REJECT_TO_COMFIRMED, num);
        	 Map map = new HashMap();
	    	 map.put("unreviewed", 0L);//未审核
	    	 map.put("reviewed", 0L);//已审核
	    	 map.put("reject", 0L);//驳回
	    	 map.put("reject_to_comfirmed", 0L);//待确认重审
	    	 if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(dep)) {
	    		 String sql = "select b.status, 0,count(a.id) as num from ReviewPoint a left join dep_reviewpoint_status b on a.id = b.review_point_id ";
		    	 sql += "where a.department_id like ? and (b.dep_name = ? or b.dep_name is null) and a.fourth_id in ";
		    	 sql += "(select fourthid from fourthcat_dep_relation where username like ?) group by b.status";
		    	 List<Record> list = Db.find(sql, "%" + dep + "%", dep, "%" + userName + "%");
		    	 for(Record record : list) {
		    		 Integer status = record.getInt("status");
		    		 long num = record.getLong("num");
		    		 if(status==null) map.put("unreviewed", num);
		    		 else if(status==1) map.put("reviewed", num);
		    		 else if(status==2) map.put("reject", num);
		    		 else if(status==3) map.put("reject_to_comfirmed", num);
		    	 }
	    	 } else if (StringUtils.isBlank(userName) &&StringUtils.isNotBlank(dep)) {
		    	 String sql = "select b.status, 0,count(a.id) as num from ReviewPoint a left join dep_reviewpoint_status b on a.id = b.review_point_id ";
		    	 sql += "where a.department_id like ? and (b.dep_name = ? or b.dep_name is null) group by b.status";
		    	 
		    	 List<Record> list = Db.find(sql, "%" + dep + "%", dep);
		    	 for(Record record : list) {
		    		 Integer status = record.getInt("status");
		    		 long num = record.getLong("num");
		    		 if(status==null) map.put("unreviewed", num);
		    		 else if(status==1) map.put("reviewed", num);
		    		 else if(status==2) map.put("reject", num);
		    		 else if(status==3) map.put("reject_to_comfirmed", num);
		    	 }
		     } else {
		    	 String sql = "select `status`, count(*) as num from ( ";
		    	 sql += " select a.id, b.`status` from ReviewPoint a left join dep_reviewpoint_status b ";
		    	 sql += " on a.id = b.review_point_id group by a.id, b.`status`) c group by `status`";
		    	 List<Record> list = Db.find(sql);
		    	 for(Record record : list) {
		    		 Integer status = record.getInt("status");
		    		 long num = record.getLong("num");
		    		 if(status==null) map.put("unreviewed", num);
		    		 else if(status==1) map.put("reviewed", num);
		    		 else if(status==2) map.put("reject", num);
		    		 else if(status==3) map.put("reject_to_comfirmed", num);
		    	 }
		     }
		     baseResult.setData(map);
             buildSuccessResult(baseResult, deferredResult);

        } catch (Throwable e) {
        	e.printStackTrace();
            buildErrorResult(baseResult, ResultCodeEnum.SYSTEM_ERROR.getCode(), ResultCodeEnum.SYSTEM_ERROR.getDesc(), deferredResult);
        }
        return deferredResult;
    }
  
    @Autowired
    ReviewPointMessageService reviewPointMessageService;
    @Autowired
    DepRevierPointStautsRepository depRevierPointStautsRepository;
    
    /**
     * 详情页审核
     * @param id
     * @param dep
     * @param status
     * @param userName
     * @return
     */
    @RequestMapping(value = "/reviewpoints/{id}/updateDepStatus", method = RequestMethod.POST)
    @ApiOperation(value="修改部门审核操作",notes = "修改部门审核操作", httpMethod = "POST")
    @ResponseBody
    public BaseResult updateReviewPointDepStatus(@PathVariable("id") Integer id,
    		@RequestParam("dep") String dep,
    		@RequestParam("status") Integer status,
    		@RequestParam("passed") Boolean passed,
    		@RequestParam("userName") String userName){
    	
    	//准备修改的代码
    	ReviewPointModel rpm = ReviewPointModel.dao.findById(id);
    	String departmentNameStr = rpm.getStr("department_id");
    	String[] departmentNames = departmentNameStr.split(",");
    	boolean reviewPointPassed = passed;
    	int reviewPointStatus = status;
    	
    	Users user = usersMapper.selectUserByName(userName);
    	int role = user.getRole();
    	if(role==0) {
    		if(StringUtils.isBlank(dep)) {
    			dep = departmentNames[0];
    		}
    	} else {
    		dep = user.getDepartmentName();
    	}
    	
    	Integer currDepStatus = 0;
    	Record r = Db.findFirst("select * from dep_reviewpoint_status where review_point_id = ? and dep_name = ?", id, dep);
    	if(r!=null) {
    		currDepStatus = r.getInt("status");
    	}
    	if(currDepStatus==2 && role!=0) {
			//非超管 将驳回的条目 置为合格
			status = 3;
			reviewPointPassed = false;//确认待重审 要点状态为不合格
			reviewPointStatus = 2;//外层为红色不通过
		}
    	
    	for(String departmentName : departmentNames) {
    		if(departmentName.equals(dep)) continue;
    		Record record = Db.findFirst("select * from dep_reviewpoint_status where review_point_id = ? and dep_name = ?", id, departmentName);
    		if(record==null) {
    			reviewPointPassed = false;
    			break;
    		}
    		boolean isPassed = record.getBoolean("passed");
    		int depStatus = record.getInt("status");
    		
    		if((depStatus==1 && !isPassed)) {
    			reviewPointPassed = false;
    			break;
    		} else if(depStatus==2) {
    			reviewPointPassed = false;
    			reviewPointStatus = 2;
    			break;
    		} 
    	}
    	rpm.set("status", reviewPointStatus);
    	rpm.set("passed", reviewPointPassed);
    	rpm.update();
    	
    	Db.update("delete from dep_reviewpoint_status where review_point_id = ? and dep_name = ?", id, dep);
    	DepReviewPointStatusModel model = new DepReviewPointStatusModel();
    	model.set("dep_name", dep).set("status", status).set("passed", passed).set("review_point_id", id).save();
    	
    	ReviewPoint reviewPoint = reviewPointMapper.selectReviewPointById(id);
//    	Users user = usersMapper.selectUserByName(userName);
    	reviewPointMessageService.addActionLog(reviewPoint, user);
    	
    	BaseResult baseResult = new BaseResult();
//    	Users users = new Users();
//        users.setUserName(userName);
//        Users user = usersMapper.queryByUser(users);
//         
//		DepRevierPointStauts depRevierPointStauts = depRevierPointStautsRepository.findByReviewPointIdAndDepName(id, dep);
//		if (depRevierPointStauts==null) {
//			depRevierPointStauts = new DepRevierPointStauts();
//			depRevierPointStauts.setReviewPointId(id);
//			depRevierPointStauts.setDepName(dep);
//		}
// 	
//		depRevierPointStauts.setStatus(status);
//		depRevierPointStautsRepository.save(depRevierPointStauts);
//		ReviewPoint reviewPoint = reviewPointMapper.selectReviewPointById(id);
//		reviewPointMessageService.addActionLog(reviewPoint,user);
         
        baseResult.setData("更新成功");
        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
        baseResult.setSuccess(true);
        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());

        return baseResult;
    }
    
    /**
     * 列表页审核
     * @param id
     * @param updateReviewPointStatusVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/reviewpoints/{id}/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value="审核操作",notes = "审核操作", httpMethod = "POST")
    @ResponseBody
    public BaseResult updateReviewPointStatus(@PathVariable("id") Integer id,@RequestBody UpdateReviewPointStatusVo updateReviewPointStatusVo) throws Exception {
     
    	return updateReviewPointDepStatus(id, null, updateReviewPointStatusVo.status, 
    			updateReviewPointStatusVo.passed, updateReviewPointStatusVo.userName);
    	
//        BaseResult baseResult = new BaseResult();
//        //1. 参数校验
//        if (StringUtils.isBlank(updateReviewPointStatusVo.userName)) {
//			 baseResult.setData("请求参数缺失!:userName");
//	         baseResult.setResultCode(ResultCodeEnum.ILLEGAL_ARGUMENT.getCode());
//	         baseResult.setSuccess(false);
//	         baseResult.setResultMessage("请求参数缺失!:userName");
//	         return baseResult;
//        }
//
//        Users users = new Users();
//        users.setUserName(updateReviewPointStatusVo.userName);
//        Users user = usersMapper.queryByUser(users);
//        ReviewPoint reviewPoint = reviewPointMapper.selectReviewPointById(id);
//        
//        for (String depName : reviewPoint.getDepartmentIdList()) {
//        	Department department = departmentMapper.selectDepartmentByName(depName);
//	    	DepRevierPointStauts  depRevierPointStaut = depRevierPointStautsRepository.findByReviewPointIdAndDepName(reviewPoint.getId(), depName );
//	    	if(depRevierPointStaut == null ){
//	    		depRevierPointStaut = new  DepRevierPointStauts();
//	    		depRevierPointStaut.setDepName(depName);
//	    		depRevierPointStaut.setStatus(Constants.UNREVIEWED);
//	    		depRevierPointStaut.setReviewPointId(id);
//	    		depRevierPointStaut.setDeptId(department.getId());
//	    	} else {
//	    		depRevierPointStaut.setDeptId(department.getId());
//	    	}
//	    	reviewPoint.getDepRevierPointStautsList().add(depRevierPointStaut);
//		}
//        reviewPoint = reviewPointService.setStatus(reviewPoint);
//        
//        ReviewPoint newReviewPoint = new ReviewPoint();
//        newReviewPoint.setId(id);
//        newReviewPoint.setDepartment_id(reviewPoint.getDepartment_id());
//        if (updateReviewPointStatusVo.status!=null) {
//            newReviewPoint.setStatus(updateReviewPointStatusVo.status);
//        }
//
//        if (updateReviewPointStatusVo.passed  != null){
//            newReviewPoint.setPassed(updateReviewPointStatusVo.passed);
//        }
//
//        Department department = departmentMapper.selectDepartmentById(user.getDepartment_id());
//
//        if (user.getRole() != Constants.SUPER_ADMIN) {
//        	Integer reviewPointId = id;
//			String depName = department.getName();
//			DepRevierPointStauts depRevierPointStauts = 
//        			this.depRevierPointStautsRepository.findByReviewPointIdAndDepName(reviewPointId, depName);
//			if (depRevierPointStauts==null) {
//				depRevierPointStauts = new DepRevierPointStauts();
//				depRevierPointStauts.setReviewPointId(reviewPointId);
//				depRevierPointStauts.setDepName(depName);
//			}
//        	
//			depRevierPointStauts.setStatus(updateReviewPointStatusVo.status);
//			depRevierPointStautsRepository.save(depRevierPointStauts);
//		}
//
//        if (user.getRole() == Constants.USER) {
//			FourthCategory fourthCategory = fourthCategoryMapper.selectById(reviewPoint.getFourth_id());
//			if (!Objects.equal(fourthCategory.getUserName(), user.getUserName())) {
//				 baseResult.setData("权限不够!");
//		         baseResult.setResultCode(ResultCodeEnum.ILLEGAL_ARGUMENT.getCode());
//		         baseResult.setSuccess(false);
//		         baseResult.setResultMessage("权限不够!");
//		         return baseResult;
//			}
//		}
//        
//        
//        if ((user.getRole() == Constants.SUPER_ADMIN) || (null != department &&
//                (reviewPoint.getDepartment_id().indexOf(department.getName())!=-1))) {
//            reviewPointMapper.updateReviewPoint(newReviewPoint);
//        } else {
//			 baseResult.setData("权限不够!");
//	         baseResult.setResultCode(ResultCodeEnum.ILLEGAL_ARGUMENT.getCode());
//	         baseResult.setSuccess(false);
//	         baseResult.setResultMessage("权限不够!");
//	         return baseResult;
//        }
//        
//        reviewPointMessageService.addActionLog(newReviewPoint,user);
//           
//
//        baseResult.setData("更新成功");
//        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
//        baseResult.setSuccess(true);
//        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
//        return baseResult;
    }
    
    @RequestMapping(value = "/reviewpoints/update", method = RequestMethod.POST)
    public DeferredResult<String> updateReviewPoint(final HttpServletRequest request) {
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        BaseResult baseResult = new BaseResult();
        final JSONObject obj = getClassFromRequestByJson(request, deferredResult);
        if (obj == null)
            return deferredResult;

        ReviewPoint reviewPoint = null;
        Users user = null;
        try {

            //1. 参数校验
            if (StringUtils.isBlank(obj.get("userName").toString())
                    || StringUtils.isBlank(obj.get("id").toString())) {
                throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "请求参数缺失!");
            }

            Users users = new Users();
            users.setUserName(obj.get("userName").toString());
            user = usersMapper.queryByUser(users);
            reviewPoint = reviewPointMapper.selectReviewPointById((Integer) obj.get("id"));
            ReviewPoint newReviewPoint = new ReviewPoint();
            newReviewPoint.setId((Integer)obj.get("id"));
            if (obj.get("status")!=null) {
                newReviewPoint.setStatus((Integer) obj.get("status")); 				
            }
            if (obj.get("remarks")!=null){
                newReviewPoint.setRemarks(obj.get("remarks").toString());
            }
            if (obj.get("score")!= null){
                newReviewPoint.setScore(obj.get("score").toString());
            }
            if (obj.get("passed")!=null){
                newReviewPoint.setPassed((Boolean)obj.get("passed"));
            }
            if (obj.get("attachment")!=null){
                newReviewPoint.setAttachment(obj.get("attachment").toString());
            }
          

            Department department = departmentMapper.selectDepartmentById(user.getDepartment_id());


            if (user.getRole() == Constants.USER) {
				FourthCategory fourthCategory = fourthCategoryMapper.selectById(reviewPoint.getFourth_id());
				if (!Objects.equal(fourthCategory.getUserName(), user.getUserName())) {
					 throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "权限不够!");
				}
			}
            
            
            if ((user.getRole() == Constants.SUPER_ADMIN) || (null != department &&
                    (reviewPoint.getDepartment_id().indexOf(department.getName())!=-1))) {
                reviewPointMapper.updateReviewPoint(newReviewPoint);
            } else {
                throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "权限不够!");
            }

          


            buildSuccessResult(baseResult, deferredResult);

        } catch (Throwable e) {
            buildErrorResult(baseResult, ResultCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage(), deferredResult);
        }

        
        return deferredResult;
    }
    
    /**
     * 审核员任务状态统计
     * @param userName
     * @return
     */
	@ResponseBody
    @RequestMapping(value="/reviewpoints/auditorStatus", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    @ApiOperation(value="审核员任务状态统计", notes="审核员任务状态统计", httpMethod="GET")
    public String auditorStatus(@RequestParam(required=true, value="userName") String userName) {
		UserModel um = UserModel.dao.findFirst("select * from users where user_name = ?", userName);
    	Integer depid = um.getInt("department_id");
    	Integer role = um.getInt("role");
    	//update by sunliang
    	String depname  = "";
    	if (depid!=null){
			DepartmentModel dm = DepartmentModel.dao.findById(depid);
			depname = dm.getStr("name");
		}
		List<UserModel> userList = null;
		List<Map> resultList = new ArrayList<Map>();
		List fourthids = new ArrayList();
    	//管理员相关操作
		if (role == Constants.SUPER_ADMIN && depid==null){
//			List<Users> usersList = usersMapper.selectAllUsers();
//			String sql = "select B.username,A.status,count(A.num) num,A.fourth_id,A.department_id  from (\n" +
//					"select count(status) as num, status,fourth_id,department_id from ReviewPoint \n" +
//					"group by department_id,fourth_id ) A join \n" +
//					"fourthcat_dep_relation B \n" +
//					"on A.fourth_id = B.fourthid\n" +
//					"GROUP BY username,status";
//			List<FourthcatDepRelation> relationList = FourthcatDepRelation.dao.find(sql);
//
//			for (FourthcatDepRelation depRelation :relationList){
//			  String username =  depRelation.get("username");
//			  int status = depRelation.getInt("status");
//			  int num = depRelation.getLong("num").intValue();
//			  String departId = depRelation.getStr("department_id");
//			  System.out.println("【username】："+username+"【status】:"+status+",【num】:"+num+",【departId】:"+departId);
//			  Map map = new HashMap();
//			  Map<Integer, Integer> userMap = null;
//			  boolean flag =false;
//			  for (Map tempMap :resultList){
//				  if (tempMap.get("name").equals(username)){
//					  map = tempMap;
//					  flag=true;
//				  }
//			  }
//				if (map.get("name")!=null){
//					userMap = (Map<Integer, Integer>) map.get("data");
//					num+=userMap.get(status);
//				}else {
//					map.put("name", username);
//					userMap = new LinkedHashMap<>();
//				}
//				//0 未审核，1 已审核，2 被驳回，3 已重审待确认。
//				userMap.put(0, 0);userMap.put(1, 0);userMap.put(2, 0);userMap.put(3, 0);
//				userMap.put(status, num);
//				if (!flag){
//					map.put("data", userMap);
//					resultList.add(map);
//				}
//			}

		}else{
			userList = UserModel.dao.find("select user_name from users where department_id = ? and role = 2", depid);
			for(UserModel user : userList) {
				Map map = new HashMap();
				String name = user.getStr("user_name");
				map.put("name", name);
				String sql = "select fourthid from fourthcat_dep_relation where depid = ? and username = ?";
				List<FourthcatDepRelation> relationList = FourthcatDepRelation.dao.find(sql, depid, name);
				List<Integer> fourthidList = Lists.transform(relationList, new Function<FourthcatDepRelation, Integer>() {
					@Override
					public Integer apply(FourthcatDepRelation relation) {
						return relation.getInt("fourthid");
					}
				});
				LinkedHashMap<Integer, Integer> userMap = new LinkedHashMap<>();
				//0 未审核，1 已审核，2 被驳回，3 已重审待确认。
				userMap.put(0, 0);userMap.put(1, 0);userMap.put(2, 0);userMap.put(3, 0);
				if(CollectionUtils.isNotEmpty(fourthidList)) {
					fourthids.addAll(fourthidList);
					String fourthidStr = StringUtils.join(fourthidList, ",");
					sql = "select count(status) as num, status from ReviewPoint where fourth_id in (" + fourthidStr
							+ ") and department_id like ? group by status";
					List<Record> list = Db.find(sql, "%" + depname + "%");
					for(Record record : list) {
						int status = record.getInt("status");
						int num = record.getLong("num").intValue();
						userMap.put(status, num);
					}
				}
				map.put("data", userMap);
				resultList.add(map);
			}
		}
    	//处理未分配的情况
    	String otherSql = "select count(status) as num, status from ReviewPoint where 1=1 ";
    	if(CollectionUtils.isNotEmpty(fourthids)) {
    		otherSql += " and fourth_id not in (" + StringUtils.join(fourthids, ",") + ") ";
    	}
    	otherSql += " and department_id like ? group by status";
    	List<Record> list = Db.find(otherSql, "%" + depname + "%");
    	Map otherMap = new HashMap();
    	otherMap.put("name", "未分配");
    	Map dataMap = new HashMap();
    	dataMap.put(0, 0);dataMap.put(1, 0);dataMap.put(2, 0);dataMap.put(3, 0);//初始化
		for(Record record : list) {
			int status = record.getInt("status");
			int num = record.getLong("num").intValue();
			dataMap.put(status, num);
		}
		otherMap.put("data", dataMap);
		resultList.add(otherMap);
    	return success(resultList);
    }


}
