package com.auditing.work.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auditing.work.dal.daointerface.UsersMapper;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.jpa.dao.ReviewPointCommentRepository;
import com.auditing.work.jpa.po.ReviewPointComment;
import com.auditing.work.jpa.po.ReviewPointCommentAddVo;
import com.auditing.work.result.BaseResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
@Api(description="评审 评论 API")
@Transactional
public class ReviewPointCommentCtrl {
	 @Autowired
	    protected UsersMapper usersMapper;

	
	 		@Autowired
	 	  private ReviewPointCommentRepository reviewPointCommentRepository;

	 		private final Logger log = LoggerFactory.getLogger(ReviewPointCommentCtrl.class);
	    /**
	     * POST  /review-point-docs : Create a new reviewPointDoc.
	     *
	     * @param reviewPointDoc the reviewPointDoc to create
	     * @return the ResponseEntity with status 201 (Created) and with body the new reviewPointDoc, or with status 400 (Bad Request) if the reviewPointDoc has already an ID
	     * @throws URISyntaxException if the Location URI syntax is incorrect
	     */
	 	 @RequestMapping(value = "/reviewPointComment", method = RequestMethod.POST)
	     @ApiOperation(value="创建评审评论",notes = "创建评审评论", httpMethod = "POST")
	 	  public ResponseEntity<BaseResult> createReviewPointComment(@Validated @RequestBody ReviewPointCommentAddVo reviewPointCommentAddVo) throws URISyntaxException {
	         log.debug("REST request to save ReviewPointComment : {}", reviewPointCommentAddVo);
	         ReviewPointComment reviewPointComment = new ReviewPointComment();
	         BaseResult baseResult = new BaseResult();
	         
	         if (reviewPointCommentAddVo.getReviewPointId() == null ) {
	        	  baseResult.setResultCode("400");
	 	          baseResult.setSuccess(false);
	 	          baseResult.setResultMessage("reviewPointId is null");
	        	  return ResponseEntity.ok(baseResult);
			  }
	         if (StringUtils.isEmpty(reviewPointCommentAddVo.getContent()) ) {
	        	 
	        	  baseResult.setResultCode("400");
	 	          baseResult.setSuccess(false);
	 	          baseResult.setResultMessage("Content is null");
	        	  return ResponseEntity.ok(baseResult);
			}
	         if (reviewPointCommentAddVo.getUserId()== null ) {
	        	  baseResult.setResultCode("400");
	 	          baseResult.setSuccess(false);
	 	          baseResult.setResultMessage("userId is null");
	        	  return ResponseEntity.ok(baseResult);
			}
	         reviewPointComment.setReviewPointId(reviewPointCommentAddVo.getReviewPointId());
	         reviewPointComment.setContent(reviewPointCommentAddVo.getContent());
	         reviewPointComment.setTime(ZonedDateTime.now());
	         reviewPointComment.setUserId(reviewPointCommentAddVo.getUserId());
	         ReviewPointComment result = reviewPointCommentRepository.save(reviewPointComment);
	         
	        
	         baseResult.setData(result);
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return ResponseEntity.ok(baseResult);
	        
	     }
	 	 
	 	  @RequestMapping(value = "/reviewPointComment/list", method = RequestMethod.GET)
		    @ApiOperation(value="评审评论列表",notes = "评审评论列表", httpMethod = "GET")
			public ResponseEntity<BaseResult> list(
				@ApiParam(name="reviewPointId",value="表 review_point 的 id",required=true) 
				@RequestParam("reviewPointId") Long  reviewPointId ) throws IOException {
				
				List<ReviewPointComment> list = reviewPointCommentRepository.findByReviewPointId(reviewPointId);
				List<Map<String, Object>> restList = Lists.newArrayList();
				for (ReviewPointComment reviewPointComment : list) {
					Map<String, Object> data = Maps.newHashMap();
					data.put("id", reviewPointComment.getId());
					data.put("content", reviewPointComment.getContent());
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") ;
					data.put("time", reviewPointComment.getTime().format(formatter ));
					Users users = usersMapper.selectByPrimaryKey(reviewPointComment.getUserId());
					if (users != null) {
						data.put("userName", users.getUserName());
					}
				
					restList.add(data);
				}
				     BaseResult baseResult = new BaseResult();
			         baseResult.setData(restList);
			         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
			         baseResult.setSuccess(true);
			         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
			         return ResponseEntity.ok(baseResult);
//				return ResponseEntity.ok(restList);
			}
	 	  
	 	  @RequestMapping(value = "/reviewPointComment/page", method = RequestMethod.GET)
		    @ApiOperation(value="评审评论分页列表",notes = "评审评论分页列表", httpMethod = "GET")
			public ResponseEntity<BaseResult> page(
				@ApiParam(name="reviewPointId",value="表 review_point 的 id",required=true) 
				@RequestParam("reviewPointId") Long  reviewPointId ,
				@RequestParam(value="page",defaultValue="0") Integer  page,
				@RequestParam(value="size",defaultValue="8") Integer  size) throws IOException {
				
	 		  
	 		  
	 		  Pageable pageable = new PageRequest(page, size);
				Page<ReviewPointComment> list = reviewPointCommentRepository.findAll(pageable);
				
				
				
				List<Map<String, Object>> restList = Lists.newArrayList();
				for (ReviewPointComment reviewPointComment : list) {
					Map<String, Object> data = Maps.newHashMap();
					data.put("id", reviewPointComment.getId());
					data.put("content", reviewPointComment.getContent());
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") ;
					data.put("time", reviewPointComment.getTime().format(formatter ));
					Users users = usersMapper.selectByPrimaryKey(reviewPointComment.getUserId());
					if (users != null) {
						data.put("userName", users.getUserName());
					}
				
					restList.add(data);
				}
				
				Map<String, Object> data = Maps.newHashMap();
				data.put("restList", restList);
				data.put("X-Total-Count", list.getTotalElements());
				     BaseResult baseResult = new BaseResult();
			         baseResult.setData(data);
			         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
			         baseResult.setSuccess(true);
			         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
			         return ResponseEntity.ok(baseResult);
//				return ResponseEntity.ok(restList);
			}

	 	 @RequestMapping(value="{id}" ,method=RequestMethod.DELETE)
	 	  @ApiOperation(value="删除评论",notes = "删除评论", httpMethod = "DELETE")
	 	@ResponseBody
	 	public BaseResult delete(@PathVariable("id") Long  id){
	 		 BaseResult baseResult = new BaseResult();
	 		reviewPointCommentRepository.delete(id);
	 		 baseResult.setData("删除成功");
	 		 baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	          baseResult.setSuccess(true);
	          baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	          return baseResult;
	 	}
	 	  
}
