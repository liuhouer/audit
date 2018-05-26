package com.auditing.work.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
import com.auditing.work.dal.dataobject.Department;
import com.auditing.work.dal.dataobject.FourthCategory;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.modle.jf.DepartmentModel;
import com.auditing.work.modle.jf.FourthcatDepRelation;
import com.auditing.work.modle.jf.ReviewPointModel;
import com.auditing.work.modle.jf.UserModel;
import com.auditing.work.modle.vo.FourthcategoryReviewDep;
import com.auditing.work.modle.vo.UpdateFourthCategoryUserNameVo;
import com.auditing.work.result.BaseResult;
import com.auditing.work.service.ReviewPointService;
import com.auditing.work.util.CacheHelper;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by innolab on 17-1-3.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@RequestMapping("/api")
@Controller
@Api(tags="四级类目 API")
public class FourthCategoryController extends BaseController {
    @RequestMapping(value = "/fourthcategory/update", method = RequestMethod.POST)
    public DeferredResult<String> updateFourthCategory(final HttpServletRequest request) {
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        BaseResult baseResult = new BaseResult();

        final JSONObject obj = getClassFromRequestByJson(request, deferredResult);
        if (obj == null)
            return deferredResult;

        try {

            FourthCategory fourthCategory = new FourthCategory();
            fourthCategory.setId((Integer)obj.get("id"));

            if (obj.get("remarks")!=null){
                fourthCategory.setRemarks(obj.get("remarks").toString());
            }
            if (obj.get("total_score")!= null){
                fourthCategory.setTotal_score(obj.get("total_score").toString());
            }

            fourthCategoryMapper.updateFourthCategory(fourthCategory);

            buildSuccessResult(baseResult, deferredResult);
        } catch (Throwable e) {
            buildErrorResult(baseResult, ResultCodeEnum.SYSTEM_ERROR.getCode(), ResultCodeEnum.SYSTEM_ERROR.getDesc(), deferredResult);
        }
        return deferredResult;
    }
    
    @RequestMapping(value = "/fourthcategory/{id}/userName", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ApiOperation(value="更新四级条目审核员",notes = "更新四级条目审核员", httpMethod = "POST")
	@ResponseBody
    public String updateFourthCategoryUserName(@PathVariable("id") Integer id,
    		@RequestParam(required=false, value="username") String username, 
    		@RequestParam(required=false, value="depname") String depname) throws Exception {
//            BaseResult baseResult = new BaseResult();
//
//       
//
//            FourthCategory fourthCategory = new FourthCategory();
//            fourthCategory.setId(id);
//            Users users = new Users();
//            users.setUserName(userName);
//            Users user = usersMapper.queryByUser(users);
//
//            if (user  == null  ) {
//            	 baseResult.setData("无效 userName");
//		         baseResult.setResultCode("404");
//		         baseResult.setSuccess(false);
//		         baseResult.setResultMessage("无效 userName");
//		         return baseResult;
//			}
//            
//            if(user.getRole() != Constants.USER){
//            	 baseResult.setData("该用户不是审核员");
//		         baseResult.setResultCode("404");
//		         baseResult.setSuccess(false);
//		         baseResult.setResultMessage("该用户不是审核员");
//		         return baseResult;
//            }
//            
//            fourthCategory.setUserName(userName);
//            
//             fourthCategoryMapper.updateFourthCategory(fourthCategory);
//             baseResult.setData("更新成功");
//	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
//	         baseResult.setSuccess(true);
//	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());	
    	
    	Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				if(StringUtils.isBlank(username)) {
					Department department = cacheHelper.getCacheDepartmentByName(depname);
					Db.update("delete from fourthcat_dep_relation where fourthid = ? and depid = ?", id, department.getId());
				} else {
					UserModel user = UserModel.dao.findFirst("select * from users where user_name = ?", username);
					int depid = user.getInt("department_id");
					FourthcatDepRelation.dao.deleteById(id, depid);
					FourthcatDepRelation relation = new FourthcatDepRelation();
			    	relation.set("fourthid", id);
			    	relation.set("depid", depid);
			    	relation.set("username", username);
			    	relation.save();
				}
				return true;
			}
		});
    	
        return success();
    }
    
    @RequestMapping(value = "/fourthcategory/userName", method = RequestMethod.POST)
    @ApiOperation(value="批量更新 更新四级条目审核员",notes = "批量更新 更新四级条目审核员", httpMethod = "POST")
	 @ResponseBody
    public BaseResult batchUpdateFourthCategoryUserName(@RequestParam(required=false, value="username") String username, 
    		@RequestParam(required=true, value="ids") String ids, 
    		@RequestParam(required=false, value="depname") String depname) throws Exception {
    	
    		if(StringUtils.isBlank(username)) {
    			Db.tx(new IAtom() {
					@Override
					public boolean run() throws SQLException {
						Department department = cacheHelper.getCacheDepartmentByName(depname);
						for(String fourthid : ids.split(",")) {
							Db.update("delete from fourthcat_dep_relation where fourthid = ? and depid = ?"
									, fourthid, department.getId());
						}
						return true;
					}
				});
				BaseResult baseResult = new BaseResult();
				baseResult.setData("更新成功");
		        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
		        baseResult.setSuccess(true);
		        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());	
		        return baseResult;
    		}
    	
            BaseResult baseResult = new BaseResult();
            Db.tx(new IAtom() {
				
				@Override
				public boolean run() throws SQLException {
					UserModel um = UserModel.dao.findFirst("select * from users where user_name = ?", username);
					Integer depid = um.getInt("department_id");
					
					for(String fourthid : ids.split(",")) {
						FourthcatDepRelation.dao.deleteById(fourthid, depid);
//							Db.update("delete from fourthcat_dep_relation where fourthid = ? and depid = ?", fourthid, depid);
						FourthcatDepRelation relation = new FourthcatDepRelation();
						relation.set("fourthid", fourthid);
						relation.set("depid", depid);
						relation.set("username", username);
						relation.save();
					}
					return true;
				}
			});
            
            
             baseResult.setData("更新成功");
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());	

        return baseResult;
    }
    
    @RequestMapping(value = "/fourthcategory/list", method = RequestMethod.GET)
    @ApiOperation(value="获取四级条目",notes = "获取四级条目", httpMethod = "GET")
	 @ResponseBody
    public List<FourthCategory>  findAll(
    		@RequestParam(value ="userName",required=false) String  userName,
    		@RequestParam(value ="key",required=false) String  key,
    		@RequestParam(value ="dep",required=false) String  dep,
    		@RequestParam(value ="firstCategoryId",required=false) Integer  firstCategoryId) throws Exception{
    	List<FourthCategory> list = fourthCategoryMapper.selectAllView(key,userName,firstCategoryId,dep).stream().map( fc->{    	
    		FourthcategoryReviewDep reviewDep = 
    				reviewPointService.getFourthcategoryReviewDep(fc.getId());
			fc.setReviewDep(reviewDep );
    		return fc;
    	} ).collect(Collectors.toList());
    	System.out.println(list.size());
    	return list;
    }
    
    /**
     * 任务概览（管理员）
     * @param userName
     * @param key
     * @param dep
     * @param firstCategoryId
     * @param pageNumber
     * @param pageSize
     * @return
     */
	@RequestMapping(value = "/fourthcategory/list", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ApiOperation(value="获取四级条目",notes = "获取四级条目", httpMethod = "POST")
	@ResponseBody
    public String findAll(
    		@RequestParam(value ="userName",required=false) String userName,
    		@RequestParam(value ="key",required=false) String key,
    		@RequestParam(value ="dep",required=false) String dep,
    		@RequestParam(value ="firstCategoryId",required=false) Integer firstCategoryId,
    		@RequestParam(value ="pageNumber",required=false, defaultValue="1") Integer pageNumber,
    		@RequestParam(value ="pageSize",required=false, defaultValue="10") Integer pageSize) {
//    	List<FourthCategory> list = fourthCategoryMapper.selectAllView(key,userName,firstCategoryId,dep).stream().map( fc->{    	
//    		FourthcategoryReviewDep reviewDep = 
//    				reviewPointService.getFourthcategoryReviewDep(fc.getId());
//			fc.setReviewDep(reviewDep );
//    		return fc;
//    	} ).collect(Collectors.toList());
//    	System.out.println(list.size());
		
//		UserModel um = UserModel.dao.findFirst("select * from users where user_name = ?", userName);
//		Integer depid = um.getInt("department_id");
//		DepartmentModel dm = null;
//		if(depid!=null) {
//			dm = DepartmentModel.dao.findById(depid);
//		}
		
    	List params = new ArrayList();
    	String sql = "from (select distinct b.id,b.name,b.total_score from ReviewPoint a ";
    	sql += " join FourthCategory b on a.fourth_id = b.id ";
    	sql += " join ThirdCategory c on b.third_id = c.id ";
    	sql += " join SecondCategory d on c.second_id = d.id ";
    	sql += " join FirstCategory e on d.first_id = e.id ";
    	sql += " where 1=1 ";
    	if(StringUtils.isNotBlank(dep)) {
    		sql += " and department_id like ?";
    		params.add("%" + dep + "%");
    	}
    	if(firstCategoryId!=null) {
    		sql += " and e.id = ?";
    		params.add(firstCategoryId);
    	}
    	if(StringUtils.isNotBlank(key)) {
    		sql += " and (a.detail like ? or b.name like ? or c.name like ? or d.name like ? or e.name like ?)";
    		params.add("%" + key + "%");
    		params.add("%" + key + "%");
    		params.add("%" + key + "%");
    		params.add("%" + key + "%");
    		params.add("%" + key + "%");
    	}
    	sql += ") x";
    	Page<Record> page = Db.paginate(pageNumber, pageSize, "select *", sql, params.toArray());
//    	List<DepartmentModel> departments = DepartmentModel.dao.find("select * from department");
//    	List<String> depnames = Lists.transform(departments, new Function<DepartmentModel, String>() {
//			@Override
//			public String apply(DepartmentModel dm) {
//				return dm.getStr("name");
//			}
//		});
    	for(Record record : page.getList()) {
    		int fourthid = record.getInt("id");
    		List<ReviewPointModel> list = ReviewPointModel.dao.find("select id,department_id from ReviewPoint where fourth_id = ?", fourthid);
    		Set<String> depset = new HashSet<>();
    		List<Integer> reviewids = new ArrayList<>();
    		for(ReviewPointModel rpm : list) {
    			String depstr = rpm.getStr("department_id");
    			String[] depnamez = depstr.split(",");
    			for(String depname : depnamez) {
    				depset.add(depname);
    			}
    			int id = rpm.getInt("id");
    			reviewids.add(id);
    		}
    		String reviewidstr = StringUtils.join(reviewids, ",");
    		List<Record> records = Db.find("select dep_name from dep_reviewpoint_status where review_point_id in (" + reviewidstr + ")");
    		Set<String> auditdepset = new HashSet<>();
    		for(Record r : records) {
    			String depname = r.getStr("dep_name");
    			auditdepset.add(depname);
    		}
    		Set<String> depsetcopy = new HashSet<>();
    		depsetcopy.addAll(depset);
    		depset.removeAll(auditdepset);
//    		record.set("auditdeps", auditdepset);
//    		record.set("unauditdeps", depset);
    		
    		//处理成老接口的格式
    		Map map = new HashMap();
    		List depList = new ArrayList();
    		for(String depcopy : depsetcopy) {
    			Map depmap = new HashMap();
    			depmap.put("name", depcopy);
    			if(auditdepset.contains(depcopy)) {
    				depmap.put("type", 1);
    			} else if(depset.contains(depcopy)) {
    				depmap.put("type", 2);
    			}
    			depList.add(depmap);
    		}
    		map.put("depList", depList);
    		record.set("reviewDep", map);
    	}
    	
    	return success(page);
    }
    
    public static void main(String[] args) {
		Set<Integer> set1 = new HashSet<>();
		set1.add(1);set1.add(2);set1.add(3);set1.add(4);set1.add(5);
		Set<Integer> set2 = new HashSet<>();
		set2.add(1);set2.add(2);set2.add(3);
		System.out.println(set1);
		System.out.println(set2);
		set1.removeAll(set2);
		System.out.println(set1);
	}
    
    
    @Autowired
	ReviewPointService reviewPointService;
    
    @ResponseBody
    @RequestMapping(value = "/fourthcategory/{id}/reviewDep", method = RequestMethod.GET)
    @ApiOperation(value="四级条目的评审部门",notes = "四级条目的评审部门", httpMethod = "GET")
    public FourthcategoryReviewDep   fourthcategoryReviewDep(@PathVariable("id") Integer id) throws Exception{
    	return reviewPointService.getFourthcategoryReviewDep(id);
    }
    
    @Autowired
    CacheHelper cacheHelper;
    
    /**
     * 给四级条目分配审核员
     * @param fourthids
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fourthcategory/assignAuditor", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ApiOperation(value="分配审核员",notes = "分配审核员", httpMethod = "POST")
    public String assignAuditor(@RequestParam(required=false, value="fourthids") String fourthids, 
    		@RequestParam(required=true, value="username") String username, 
    		@RequestParam(required=false, value="depname") String depname) {
    	String[] fourthidz = fourthids.split(",");
    	Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				if("".equals(username)) {
					Department department = cacheHelper.getCacheDepartmentByName(depname);
					for(String fourthid : fourthidz) {
						Db.update("delete from fourthcat_dep_relation where fourthid = ? and depid = ?"
								, fourthid, department.getId());
					}
				} else {
					UserModel um = UserModel.dao.findFirst("select * from users where user_name = ?", username);
					Integer depid = um.getInt("department_id");
					
					for(String fourthid : fourthidz) {
						FourthcatDepRelation.dao.deleteById(fourthid, depid);
	//					Db.update("delete from fourthcat_dep_relation where fourthid = ? and depid = ?", fourthid, depid);
						FourthcatDepRelation relation = new FourthcatDepRelation();
						relation.set("fourthid", fourthid);
						relation.set("depid", depid);
						relation.set("username", username);
						relation.save();
					}
				}
				return true;
			}
		});
    	return success();
    }
    
    /**
     * 四级条目的分配列表
     * @param userName
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/fourthcategory/assignList", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    @ApiOperation(value="四级条目分配列表",notes = "四级条目分配列表", httpMethod = "GET")
    public String list(@RequestParam(required=true, value="userName") String userName,
    		@RequestParam(required=false, value="pageNumber", defaultValue="1") int pageNumber,
    		@RequestParam(required=false, value="pageSize", defaultValue="10") int pageSize) {
    	UserModel um = UserModel.dao.findFirst("select * from users where user_name = ?", userName);
    	int depid = um.getInt("department_id");
    	DepartmentModel dm = DepartmentModel.dao.findById(depid);
    	String depname = dm.getStr("name");
    	Page<Record> page = Db.paginate(pageNumber, pageSize, "select *"
    			, "from (select distinct a.id, a.name,c.username from FourthCategory a left join ReviewPoint b on a.id = b.fourth_id "
    					+ "left join (select * from fourthcat_dep_relation where depid = ?) c on a.id = c.fourthid where b.department_id LIKE ?) x"
    			, dm.getInt("id"), "%" + depname + "%");
    	return success(page);
    }
    
    /**
     * 任务概览（管理员）
     * @param userName
     * @param key
     * @param firstCategoryId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/fourthcategory/taskOverview", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
    @ApiOperation(value="任务概览（管理员）",notes = "任务概览（管理员）", httpMethod = "POST")
    public String taskOverview(
    		@RequestParam(value ="userName",required=true) String userName,
    		@RequestParam(value ="key",required=false) String key,
    		@RequestParam(value ="firstCategoryId",required=false) Integer firstCategoryId,
    		@RequestParam(value ="pageNumber",required=false, defaultValue="1") Integer pageNumber,
    		@RequestParam(value ="pageSize",required=false, defaultValue="10") Integer pageSize) {
    	UserModel um = UserModel.dao.findFirst("select * from users where user_name = ?", userName);
		Integer depid = um.getInt("department_id");
		DepartmentModel dm =  DepartmentModel.dao.findById(depid);
		String depname = dm.getStr("name");
		
		List params = new ArrayList();
		String sql = "from (select distinct b.id,b.name,b.total_score from ReviewPoint a ";
    	sql += " join FourthCategory b on a.fourth_id = b.id ";
    	sql += " join ThirdCategory c on b.third_id = c.id ";
    	sql += " join SecondCategory d on c.second_id = d.id ";
    	sql += " join FirstCategory e on d.first_id = e.id ";
    	sql += " where a.department_id like ?";
    	params.add("%" + depname + "%");
    	if(firstCategoryId!=null) {
    		sql += " and e.id = ?";
    		params.add(firstCategoryId);
    	}
    	if(StringUtils.isNotBlank(key)) {
    		sql += " and (b.name like ? or c.name like ? or d.name like ? or e.name like ?)";
    		params.add("%" + key + "%");
    		params.add("%" + key + "%");
    		params.add("%" + key + "%");
    		params.add("%" + key + "%");
    	}
    	sql += ") x";
    	Page<Record> page = Db.paginate(pageNumber, pageSize, "select *", sql, params.toArray());
    	for(Record record : page.getList()) {
    		int id = record.getInt("id");
    		FourthcatDepRelation relation = FourthcatDepRelation.dao.findById(id, depid);
    		if(relation!=null) {
    			record.set("auditor", relation.getStr("username"));
    			
    			String sql1 = "select b.dep_name, b.status, b.review_point_id, a.id, a.department_id "
        				+ " from ReviewPoint a left join (select * from dep_reviewpoint_status where "
        				+ " dep_name = ?) b ON a.id = b.review_point_id "
        				+ " where a.fourth_id = ? and a.department_id like ?";
        		List<Record> list = Db.find(sql1, depname, id, "%" + depname + "%");
        		
        		boolean hasAudit = true;
        		for(Record r : list) {
        			String dname = r.getStr("dep_name");
        			if(StringUtils.isBlank(dname)) {
        				hasAudit = false;
        				break;
        			}
        		}
        		record.set("hasAudit", hasAudit);
    		}
    	}
    	return success(page);
    }
}
