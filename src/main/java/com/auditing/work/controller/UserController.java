package com.auditing.work.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auditing.work.constants.Constants;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.exception.WorkException;
import com.auditing.work.jpa.po.PublicDoc;
import com.auditing.work.jpa.po.ReviewPointDoc;
import com.auditing.work.modle.jf.UserModel;
import com.auditing.work.modle.vo.UserRequest;
import com.auditing.work.result.BaseResult;
import com.google.common.base.Objects;


@RequestMapping("/api/user")
@Controller
public class UserController extends BaseController {

	
	@RequestMapping(value="{id}" ,method=RequestMethod.DELETE)
	 @ApiOperation(value="删除用户",notes = "删除用户", httpMethod = "DELETE")
	@ResponseBody
	public BaseResult delete(@PathVariable("id") Long  id){
		 BaseResult baseResult = new BaseResult();
		 usersMapper.deleteByPrimaryKey(id);
		 baseResult.setData("删除成功");
		 baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
         baseResult.setSuccess(true);
         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
         return baseResult;
	}
	
	@RequestMapping(value="{id}/passwd/{passwd}" ,method=RequestMethod.PUT)
	 @ApiOperation(value="更新用户密码",notes = "更新用户密码", httpMethod = "PUT")
	@ResponseBody
	public BaseResult updatePasswd(@PathVariable("id") Long  id,@PathVariable("passwd") String  passwd,@RequestBody String newPasswd){
		 BaseResult baseResult = new BaseResult();
		 Users user =  usersMapper.selectByPrimaryKey(id);
		 if (user == null) {
			 baseResult.setData("用户ID 无效");
			 baseResult.setResultCode("400");
	        baseResult.setSuccess(false);
	        baseResult.setResultMessage("用户ID 无效");
	        return baseResult;
		 }
		 
		 String oldPassWd = user.getPassword();
		 if (!Objects.equal(oldPassWd, passwd)) {
			 baseResult.setData("原密码错误");
			 baseResult.setResultCode("400");
	        baseResult.setSuccess(false);
	        baseResult.setResultMessage("原密码错误");
	        return baseResult;
		}
		 
		 user.setPassword(newPasswd);
		 usersMapper.updateByPrimaryKey(user);
		 baseResult.setData("更新成功");
		 baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
        baseResult.setSuccess(true);
        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
        return baseResult;
	}
	
	
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public DeferredResult<String> login(final HttpServletRequest request) {
        final DeferredResult<String> deferredResult = new DeferredResult<String>();
        final UserRequest userForm = (UserRequest) getClassFromRequestNotEncode(request, UserRequest.class, deferredResult);
        if (userForm == null)
            return deferredResult;
        final BaseResult result = new BaseResult();
        processWithQuery(userForm, result, deferredResult, new BusinessProcess() {
            public void doProcess() throws Exception {

                LOGGER.info("requestForm:" + userForm);

                //1. 参数校验
                if (StringUtils.isBlank(userForm.getUserName())
                    || StringUtils.isBlank(userForm.getPassword())) {
                    throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "请求参数缺失!");
                }

                Users users = new Users();
                users.setUserName(userForm.getUserName());
                Users user = usersMapper.queryByUser(users);

                if (user == null) {
                    throw new WorkException(ResultCodeEnum.USER_NOT_EXIST, "用户不存在");
                }
                if (user.getPassword().equals(userForm.getPassword())) {
                    result.setData(user);
                } else {
                    throw new WorkException(ResultCodeEnum.PASSWORD_WRONG, "密码错误!");
                }

                // 3. 组装返回数据
                buildSuccessResult(result, deferredResult);
            }
        });

        // 4. 跳转
        return deferredResult;
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public DeferredResult<String> createUser(final HttpServletRequest request) {
        final DeferredResult<String> deferredResult = new DeferredResult<String>();
        final UserRequest userForm = (UserRequest) getClassFromRequestNotEncode(request, UserRequest.class,
            deferredResult);
        if (userForm == null)
            return deferredResult;
        final BaseResult result = new BaseResult();
        processWithQuery(userForm, result, deferredResult, new BusinessProcess() {
            public void doProcess() throws Exception {

                LOGGER.info("requestForm:" + userForm);
                UserRequest newUser = userForm.getNewUser();

                //1. 参数校验
                if (StringUtils.isBlank(userForm.getUserName())
                        || null == newUser
                        || StringUtils.isBlank(newUser.getUserName())
                        || StringUtils.isBlank(newUser.getPassword())
                        || StringUtils.isBlank(newUser.getDepartment())
                        || StringUtils.isBlank(newUser.getRole())) {
                    throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "请求参数缺失!");
                }

                String userName = userForm.getUserName();
                Users users = new Users();
                users.setUserName(userName);
                Users user = usersMapper.queryByUser(users);

                if ((!user.getRole().equals(Constants.SUPER_ADMIN)) &&!user.getDepartment_id().equals(newUser.getDepartment())) {
                    throw new WorkException(ResultCodeEnum.NO_PERMISSION_CREATE, "无法跨部门创建用户!");
                }
                if (user.getRole().equals(Constants.USER+"")) {
                    throw new WorkException(ResultCodeEnum.CREATE_USER_NO_PERMISSION, "无权限创建用户!");
                }
                
                String newUserName = newUser.getUserName();
                UserModel um = UserModel.dao.findFirst("select * from users where user_name = ?", newUserName);
                if(um!=null) {
                	throw new WorkException(ResultCodeEnum.USER_EXIST, "用户名已经存在!");
                }

                Users createUsers = new Users();
                createUsers.setUserName(newUser.getUserName());
                createUsers.setPassword(newUser.getPassword());
                createUsers.setRole(Integer.parseInt(newUser.getRole()));
                createUsers.setDepartment_id(Integer.parseInt(newUser.getDepartment()));
                createUsers.setGmtCreate(new Date());
                createUsers.setGmtModified(new Date());
                System.err.println(JSON.toJSONString(createUsers, true));
                usersMapper.insert(createUsers);

                // 3. 组装返回数据
                buildSuccessResult(result, deferredResult);
            }
        });

        // 4. 跳转
        return deferredResult;
    }

    
    @RequestMapping(value = "/getusers", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getUsers(@RequestParam("userName") String userName) {
     

         BaseResult result = new BaseResult();

        Users users = new Users();
        users.setUserName(userName);
        Users user = usersMapper.queryByUser(users);
        List<Users> userses = null;
        if (user.getRole()==Constants.SUPER_ADMIN) {
            userses = usersMapper.selectAllUsers();
        }else {
            userses= usersMapper.selectUsersByDepartmentId(user.getDepartment_id());
        }
        
        List<Users> data = userses.stream().filter( tmp->{
          return !Objects.equal(userName, tmp.getUserName());	
        }).collect(Collectors.toList());
        result.setData(data);
        result.setResultCode(ResultCodeEnum.SUCCESS.getCode());
        result.setSuccess(true);
        result.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());

        // 4. 跳转
        return result;
    }
    
    @RequestMapping(value = "/getusers", method = RequestMethod.POST)
    public DeferredResult<String> getAllusers(final HttpServletRequest request) {
        final DeferredResult<String> deferredResult = new DeferredResult<String>();
        final JSONObject obj = getClassFromRequestByJson(request, deferredResult);
        if (obj == null)
            return deferredResult;
        final BaseResult result = new BaseResult();

        //1. 参数校验
        if (StringUtils.isBlank(obj.get("userName").toString())) {
            throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "请求参数缺失!");
        }

        Users users = new Users();
        users.setUserName(obj.get("userName").toString());
        Users user = usersMapper.queryByUser(users);
        List<Users> userses = null;
        if (user.getRole()==Constants.SUPER_ADMIN) {
            userses = usersMapper.selectAllUsers();
        }else {
            userses= usersMapper.selectUsersByDepartmentId(user.getDepartment_id());
        }
        
        List<Users> data = userses.stream().filter( tmp->{
          return !Objects.equal(obj.get("userName").toString(), tmp.getUserName());	
        }).collect(Collectors.toList());
        result.setData(data);
        buildSuccessResult(result, deferredResult);

        // 4. 跳转
        return deferredResult;
    }
    
    
    
    
    /**
     * @author yang zhang
     * @date 2017-9-13
     * 上传头像功能
     */
    @RequestMapping(value = "/addAvatar", method = RequestMethod.POST )
    @ApiOperation(value="上传头像",notes = "上传头像", httpMethod = "POST")
    @ApiResponses(value = {  	
    		@ApiResponse(code=400,message="创建失败,上传文件为空"),
 	    	@ApiResponse(code=200,message="创建成功",response=ReviewPointDoc.class)
 	 })
    @Transactional
	public ResponseEntity<BaseResult> addAvatar(
			@ApiParam(name="userId",value="表 users 的 id",required=true) 
			@RequestParam("userId") Long  userId,
			final HttpServletRequest request){
    	 BaseResult baseResult = new BaseResult();
    	 Users model = null;
    	FileItem fileItem = findFile(request);
    	 if (fileItem==null||fileItem.getSize() == 0) {
    		  baseResult.setResultCode("400");
 	          baseResult.setSuccess(false);
 	          baseResult.setResultMessage("file Content is null");
        	  return ResponseEntity.ok(baseResult);
         }
    	 model = usersMapper.selectByPrimaryKey(userId);
    	 
    	 String fileName = fileItem.getName();
         fileName = fileName.toLowerCase();
         
         if(model!=null){
        	 model.setAvatar(fileItem.get());
        	 model.setAvatarName(fileName);
         }else{
        	  baseResult.setResultCode("400");
	          baseResult.setSuccess(false);
	          baseResult.setResultMessage("user is null");
       	  return ResponseEntity.ok(baseResult);
         }
         int res = usersMapper.updateByPrimaryKeySelective(model);
    	 
    	
         baseResult.setData(res);
         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
         baseResult.setSuccess(true);
         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
         return ResponseEntity.ok(baseResult);
        
	} 
//    @RequestMapping(value = "/modifyUser", method = RequestMethod.POST)
//    public DeferredResult<String> modifyUser(final HttpServletRequest request) {
//        final DeferredResult<String> deferredResult = new DeferredResult<String>();
//        final UserRequest userForm = (UserRequest) getClassFromRequest(request, UserRequest.class,
//            deferredResult);
//        if (userForm == null)
//            return deferredResult;
//        final BaseResult result = new BaseResult();
//        processWithQuery(userForm, result, deferredResult, new BusinessProcess() {
//            public void doProcess() throws Exception {
//
//                LOGGER.info("requestForm:" + userForm);
//
//                //1. 参数校验
//                if (StringUtils.isBlank(userForm.getUserName())) {
//                    throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "请求参数缺失!");
//                }
//
//
//                Users users = new Users();
//                users.setUserName(userName);
//                Users user = usersMapper.queryByUser(users);
//
//                if (StringUtils.isNotBlank(userForm.getPassword())) {
//                    user.setPassword(userForm.getPassword());
//                }
//                if(StringUtils.isNotBlank(userForm.getRole())) {
//                    user.setRole(Integer.parseInt(userForm.getRole()));
//                }
//                if(StringUtils.isNotBlank(userForm.getDepartment())){
//                    user.setDepartment_id(Integer.parseInt(userForm.getDepartment()));
//                }
//                user.setGmtModified(new Date());
//                if (usersMapper.updateByPrimaryKey(user) == 1) {
//                    // 3. 组装返回数据
//                    buildSuccessResult(result, deferredResult);
//                } else {
//                    throw new WorkException(ResultCodeEnum.SYSTEM_ERROR, "更新失败,系统异常!");
//                }
//            }
//        });
//
//        // 4. 跳转
//        return deferredResult;
//    }
//
//    @RequestMapping(value = "/queryUser", method = RequestMethod.POST)
//    public DeferredResult<String> queryUser(final HttpServletRequest request) {
//        final DeferredResult<String> deferredResult = new DeferredResult<String>();
//        final UserRequest userForm = (UserRequest) getClassFromRequest(request, UserRequest.class,
//                deferredResult);
//        final BaseResult result = new BaseResult();
//        processWithQuery(userForm, result, deferredResult, new BusinessProcess() {
//            public void doProcess() throws Exception {
//
//                LOGGER.info("requestForm:" + userForm);
//
//                //1. 参数校验
//                if (StringUtils.isBlank(userForm.getToken())) {
//                    throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "请求参数缺失!");
//                }
//
//                String userName = getUser(userForm.getToken());
//                Users users = new Users();
//                users.setUserName(userName);
//                Users user = usersMapper.queryByUser(users);
//                user.setPassword("");
//                result.setData(user);
//                buildSuccessResult(result, deferredResult);
//            }
//        });
//
//        // 4. 跳转
//        return deferredResult;
//    }
//
//    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
//    public DeferredResult<String> deleteUser(final HttpServletRequest request) {
//        final DeferredResult<String> deferredResult = new DeferredResult<String>();
//        final UserRequest userForm = (UserRequest) getClassFromRequest(request, UserRequest.class,
//                deferredResult);
//        final BaseResult result = new BaseResult();
//        processWithQuery(userForm, result, deferredResult, new BusinessProcess() {
//            public void doProcess() throws Exception {
//
//                LOGGER.info("requestForm:" + userForm);
//
//                //1. 参数校验
//                if (StringUtils.isBlank(userForm.getUserName())
//                        || StringUtils.isBlank(userForm.getToken())) {
//                    throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT, "请求参数缺失!");
//                }
//
//                Users users = new Users();
//                users.setUserName(userForm.getUserName());
//                Users user = usersMapper.queryByUser(users);
//                if (user==null){
//                    throw new WorkException(ResultCodeEnum.USER_NOT_EXIST,"删除用户不存在!");
//                }
//
//                String tokenUserName = getUser(userForm.getToken());
//                Users tokenUsers = new Users();
//                tokenUsers.setUserName(tokenUserName);
//                Users tokenUser = usersMapper.queryByUser(tokenUsers);
//
//                if (!tokenUser.getDepartment_id().equals(user.getDepartment_id())){
//                    throw new WorkException(ResultCodeEnum.CANNOT_DELETE_USER,"无法跨部门删除用户");
//                }
//                usersMapper.deleteByPrimaryKey(user.getId());
//
//                buildSuccessResult(result, deferredResult);
//            }
//        });
//
//        // 4. 跳转
//        return deferredResult;
//    }
}
