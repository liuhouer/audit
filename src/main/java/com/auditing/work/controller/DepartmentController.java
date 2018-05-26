package com.auditing.work.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.auditing.work.dal.dataobject.Department;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.jpa.dao.DepartmentDao;
import com.auditing.work.result.BaseResult;

/**
 * Created by innolab on 16-12-30.
 */
@RequestMapping("/api")
@Controller
public class DepartmentController extends BaseController{
    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public DeferredResult<String> department() {
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        BaseResult baseResult = new BaseResult();
        try {
            List<Department> list = departmentMapper.selectAllDepartment();
            baseResult.setData(list);
            buildSuccessResult(baseResult, deferredResult);

        }
        catch (Throwable e) {
            buildErrorResult(baseResult, ResultCodeEnum.SYSTEM_ERROR.getCode(), ResultCodeEnum.SYSTEM_ERROR.getDesc(), deferredResult);
        }

        return deferredResult;
    }
    
    
    @Autowired
    DepartmentDao departmentDao;
    
    @RequestMapping(value = "/department", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult save(@Validated @RequestBody com.auditing.work.jpa.po.Department department) {
        BaseResult baseResult = new BaseResult();
        
        if (departmentDao.findByName(department.getName()) == null) {
        	 baseResult.setData("name 重复");
             baseResult.setResultCode("100");
             baseResult.setSuccess(true);
             baseResult.setResultMessage("name 重复");
            return baseResult;
		}
        departmentDao.save(department);
	     baseResult.setData("保存成功");
         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
         baseResult.setSuccess(true);
         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
        return baseResult;
    }
    
    
    @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public BaseResult delete(@PathVariable("id") Long  id) {
        BaseResult baseResult = new BaseResult();
        departmentDao.delete(id);
        baseResult.setData("删除");
        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
        baseResult.setSuccess(true);
        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
        return baseResult;
    }
    
}
