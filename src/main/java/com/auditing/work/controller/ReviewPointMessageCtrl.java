package com.auditing.work.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.jpa.dao.ReviewPointMessageDao;
import com.auditing.work.jpa.po.ReviewPointMessage;
import com.auditing.work.modle.vo.ReviewPointActionLogView;
import com.auditing.work.result.BaseResult;
import com.auditing.work.service.ReviewPointMessageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/api/reviewPointMessage")
@Api(description="评审要点消息  API")
@RestController
public class ReviewPointMessageCtrl {
	 @Autowired
	    ReviewPointMessageService reviewPointMessageService;
	 
	 @Autowired
	 private ReviewPointMessageDao reviewPointMessageDao;
	 
	    @RequestMapping(value = "/{id}/read", method = RequestMethod.POST)
	    @ApiOperation(value="设置为已读消息",notes = "设置为已读消息", httpMethod = "POST")
	    @ResponseBody
	    public BaseResult read(@PathVariable("id") Integer id){
	    	 BaseResult baseResult = new BaseResult();
	    	 ReviewPointMessage reviewPointMessage = reviewPointMessageDao.getOne(id);
	    	 reviewPointMessage.setIsReaded(ReviewPointMessageDao.readed);
	    	 reviewPointMessageDao.save(reviewPointMessage);
	    	     baseResult.setData("设置成功");
		         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
		         baseResult.setSuccess(true);
		         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
		         return baseResult;
	    }
	    
	    @RequestMapping(value = "/{userId}/noReadList", method = RequestMethod.GET)
	    @ApiOperation(value="未读消息列表",notes = "未读消息列表", httpMethod = "GET")
	    @ResponseBody
	    public List<ReviewPointActionLogView> noReadList(@PathVariable("userId") Integer userId){
	    	
		         return reviewPointMessageService.queryNoReadActionLog(userId);
	    }
}
