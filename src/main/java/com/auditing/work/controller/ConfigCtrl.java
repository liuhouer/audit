package com.auditing.work.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auditing.work.dal.daointerface.FirstCategoryMapper;
import com.auditing.work.dal.daointerface.ReviewPointMapper;
import com.auditing.work.dal.dataobject.FirstCategory;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.jpa.dao.ConfigDao;
import com.auditing.work.jpa.po.Config;
import com.auditing.work.modle.vo.ConfigVo;
import com.auditing.work.result.BaseResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/config")
@Api(description="配置信息 API")
@Transactional
public class ConfigCtrl {
	@Autowired ConfigDao configDao;
	

    @Autowired
    protected FirstCategoryMapper firstCategoryMapper;
    
       @RequestMapping(value = "/firstCategoryList", method = RequestMethod.GET)
	   @ResponseBody
	   public List<FirstCategory> getFirstCategoryList() throws Exception{
    	return firstCategoryMapper.selectAllFirstCategory();
     }
    
	@RequestMapping(method=RequestMethod.GET)
	public ConfigVo getConfig(){
//		Map<String,String> map = Maps.newHashMap();
		Config reviewPointisEditConfig = configDao.findByKey(ConfigDao.key_reviewPointisEdit);
		Config reviewPointisManagerEditConfig = configDao.findByKey(ConfigDao.key_reviewPointisManagerEdit);
		if (reviewPointisEditConfig == null) {
			reviewPointisEditConfig = new Config();
			reviewPointisEditConfig.setKey(ConfigDao.key_reviewPointisEdit);
			reviewPointisEditConfig.setValue(Boolean.TRUE.toString());
			configDao.save(reviewPointisEditConfig);
		}
		
		if (reviewPointisManagerEditConfig == null) {
			reviewPointisManagerEditConfig = new Config();
			reviewPointisManagerEditConfig.setKey(ConfigDao.key_reviewPointisManagerEdit);
			reviewPointisManagerEditConfig.setValue(Boolean.TRUE.toString());
			configDao.save(reviewPointisManagerEditConfig);
		}
		
//		map.put(reviewPointisManagerEditConfig.getKey(), reviewPointisManagerEditConfig.getValue());
//		map.put(reviewPointisEditConfig.getKey(), reviewPointisEditConfig.getValue());
//		return map;
		ConfigVo configVo = new ConfigVo();
		configVo.reviewPointisEditConfig = reviewPointisEditConfig.getValue();
		configVo.reviewPointisManagerEditConfig = reviewPointisManagerEditConfig.getValue();
		return configVo;
	}
	
	 @Autowired
	    protected ReviewPointMapper reviewPointMapper;
	
	   @RequestMapping(value = "/reviewPointisEditConfig/setValue", method = RequestMethod.GET)
	   @ApiOperation(value="reviewPointisEditConfig 设置 (超级管理员是否可以进行审核)",notes = "reviewPointisEditConfig 设置 (超级管理员是否可以进行审核)", httpMethod = "GET")
	   @ResponseBody
	   public BaseResult setReviewPointisEditConfig(
//			   @ApiParam(name = "isReset", value = "true : ReviewPoint中的passed ,status 设置为 0 ,false: 不变") 
//			   @RequestParam("isReset") Boolean isReset
//			   ,
			   @ApiParam(name = "value", value = "true or false") 
			   @RequestParam("value") Boolean value
			   ){
		     BaseResult baseResult = new BaseResult();
		     Config reviewPointisEditConfig = configDao.findByKey(ConfigDao.key_reviewPointisEdit);
				if (reviewPointisEditConfig == null) {
					reviewPointisEditConfig = new Config();
					reviewPointisEditConfig.setKey(ConfigDao.key_reviewPointisEdit);
				
				}
				
//			     if (isReset) {
//		    	 reviewPointMapper.stautsReset();
//			 }
				
				reviewPointisEditConfig.setValue(value.toString());
				configDao.save(reviewPointisEditConfig);
				
		     baseResult.setData("设置成功");
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return baseResult;
	   }
	   
	   @RequestMapping(value = "/reviewPointisManagerEditConfig/setValue", method = RequestMethod.GET)
	   @ApiOperation(value="reviewPointisManagerEditConfig 设置(管理员是否可以进行审核)",notes = "reviewPointisManagerEditConfig 设置(管理员是否可以进行审核)", httpMethod = "GET")
	   @ResponseBody
	   public BaseResult setNotEdit(
			  
			   @ApiParam(name = "value", value = "true or false") 
			   @RequestParam("value") Boolean value
			   ){
			Config reviewPointisManagerEditConfig = configDao.findByKey(ConfigDao.key_reviewPointisManagerEdit);
		
			
			if (reviewPointisManagerEditConfig == null) {
				reviewPointisManagerEditConfig = new Config();
				reviewPointisManagerEditConfig.setKey(ConfigDao.key_reviewPointisManagerEdit);
				
			}
			reviewPointisManagerEditConfig.setValue(value.toString());
			configDao.save(reviewPointisManagerEditConfig);
			   BaseResult baseResult = new BaseResult();	
		     baseResult.setData("设置成功");
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return baseResult;
	   }
}
