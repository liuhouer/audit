package com.auditing.work.controller;



import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.utils.mapper.BeanMapper;

import com.auditing.work.controller.util.HeaderUtil;
import com.auditing.work.dal.daointerface.FourthCategoryMapper;
import com.auditing.work.dal.daointerface.ReviewPointMapper;
import com.auditing.work.jpa.dao.AuditHistoryRepository;
import com.auditing.work.jpa.po.AuditHistory;
import com.auditing.work.modle.vo.ScoreStatisticVo;
import com.auditing.work.modle.vo.TwoAuditHistory;
import com.auditing.work.modle.vo.TwoAuditHistoryUpdateVo;
import com.auditing.work.service.AuditHistoryService;
import com.auditing.work.service.ReviewPointService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * REST controller for managing AuditHistory.
 */
@RestController
@RequestMapping("/api")
@Api(tags="历史审核 API")
public class AuditHistoryResource {

    private final Logger log = LoggerFactory.getLogger(AuditHistoryResource.class);
        
    @Autowired
    private AuditHistoryRepository auditHistoryRepository;
	@Autowired
	   ReviewPointService reviewPointService;
	@Autowired
	AuditHistoryService auditHistoryService;
	
	
	 @RequestMapping(value = "/audit-histories/isStart", method = RequestMethod.GET)
	  @ApiOperation(value="审核是否开始",notes = "审核是否开始", httpMethod = "GET")
	  public ResponseEntity<Boolean> isStart() throws URISyntaxException{
		  
		 if(auditHistoryRepository.countByEndTimeIsNull() > 0){
			 return ResponseEntity.ok(Boolean.TRUE);
		 } 
		  
		 
		 return ResponseEntity.ok(Boolean.FALSE);
	 }
	 @Autowired
	    protected ReviewPointMapper reviewPointMapper;
	 
	 @Autowired
	    protected FourthCategoryMapper fourthCategoryMapper;
	  @RequestMapping(value = "/audit-histories/start", method = RequestMethod.POST)
	  @ApiOperation(value="开始审核（新增一条）",notes = "开始审核（新增一条）", httpMethod = "POST")
	  @ApiResponses({
	    	@ApiResponse(code = 200, message = "成功",response=AuditHistory.class),
	    	@ApiResponse(code = 400 , message = "失败-还有审核尚未结束不可以开始新的审核"),
	    })
	  public ResponseEntity<AuditHistory> startAudit(
			   @ApiParam(name = "isReset", value = "true :删除所有评分信息 ,false: 不变",required=false,defaultValue="false") 
			   @RequestParam(value="isReset",required=false,defaultValue="false") Boolean isReset			   			   
			  ) throws URISyntaxException{
		  
		 if(auditHistoryRepository.countByEndTimeIsNull() > 0){
			 return ResponseEntity.badRequest().body(null);
		 } 
		  
		 if(isReset){
			 reviewPointMapper.stautsReset();
			    //<!-- 清空附件 -->
			    // <!-- 清空留言 -->
			    //<!-- 清空审核记录 -->
			    //<!-- 清空消息记录 -->
			    //added by bruce 2017-8-24
			 reviewPointMapper.actionlogReset();
			 reviewPointMapper.commentReset();
			 reviewPointMapper.docReset();
			 reviewPointMapper.messageReset();
			 fourthCategoryMapper.totalScoreReset();
		 }
		 
		 AuditHistory auditHistory = new AuditHistory();
		 auditHistory.setStartTime(LocalDate.now());
		 auditHistory = auditHistoryRepository.save(auditHistory);	 
		 auditHistory.setName("第"+auditHistory.getId()+"次审核");
		 return updateAuditHistory(auditHistory);
	 }
	  @ApiResponses({
	    	@ApiResponse(code = 200, message = "成功",response=AuditHistory.class),
	    	@ApiResponse(code = 400 , message = "失败-还有没有开始审核"),
	    })
	  @ApiOperation(value="结束审核（统计分数）",notes = "结束审核（统计分数）", httpMethod = "POST")
	  @RequestMapping(value = "/audit-histories/stop", method = RequestMethod.POST)
	  public ResponseEntity<AuditHistory> stopAudit() throws URISyntaxException{
		 AuditHistory auditHistory = auditHistoryRepository.findByEndTimeIsNull();
		 if(auditHistory == null ){
			 return ResponseEntity.badRequest().body(null);
		 }
		  log.debug("REST request to stop AuditHistory : {}", auditHistory);
		 ScoreStatisticVo scoreStatisticVo = reviewPointService.getFourthCategoriesScoreStatisticVo();
		 ScoreStatisticVo keyScoreStatisticVo = reviewPointService.getKeyFourthCategoriesScoreStatisticVo();
		 AuditHistory auditHistoryTmp = BeanMapper.map(scoreStatisticVo, AuditHistory.class);
		 auditHistoryTmp.setId(auditHistory.getId());
		 auditHistoryTmp.name(auditHistory.getName())
		 .startTime(auditHistory.getStartTime())
		 .endTime(LocalDate.now())
		 .keyANumber(keyScoreStatisticVo.aNumber)
		 .keyAPercentageComplete(keyScoreStatisticVo.aPercentageComplete)
		 .keyBNumber(keyScoreStatisticVo.bNumber)
		 .keyBPercentageComplete(keyScoreStatisticVo.bPercentageComplete)
		 .keyCNumber(keyScoreStatisticVo.cNumber)
		 .keyCPercentageComplete(keyScoreStatisticVo.cPercentageComplete)
		 .keyDNumber(keyScoreStatisticVo.dNumber)
		 .keyDPercentageComplete(keyScoreStatisticVo.dPercentageComplete)
		 .keyTotalNumber(keyScoreStatisticVo.totalNumber) ;
		 
		 
		 return auditHistoryService.updateAuditHistory(auditHistoryTmp);
	 }
	  

	  @ApiOperation(value="更新历史审核备注信息",notes = "更新历史审核备注信息", httpMethod = "POST")
	  @RequestMapping(value = "/audit-histories/remarks", method = RequestMethod.POST)
	  public ResponseEntity<Void> update(HttpServletRequest request) {
          
          //处理接收参数不匹配的错误 by bruce 2017-8-22
          String fourthCategoryId = request.getParameter("fourthCategoryId");
          String fourthCategoryRemarks = request.getParameter("fourthCategoryRemarks");
        
          String auditHistoryDetailAId = request.getParameter("auditHistoryDetailAId");
          String auditHistoryDetailAremarks = request.getParameter("auditHistoryDetailAremarks");
        
          String auditHistoryDetailBId = request.getParameter("auditHistoryDetailBId");
          String auditHistoryDetailBremarks = request.getParameter("auditHistoryDetailBremarks");
         
          TwoAuditHistoryUpdateVo twoAuditHistoryUpdateVo = new TwoAuditHistoryUpdateVo();
         
          if(StringUtils.isNotEmpty(auditHistoryDetailAId)){
           
            twoAuditHistoryUpdateVo.auditHistoryDetailAId = Long.valueOf(auditHistoryDetailAId);
          }
	      if(StringUtils.isNotEmpty(auditHistoryDetailBId)){
	           
	            twoAuditHistoryUpdateVo.auditHistoryDetailBId = Long.valueOf(auditHistoryDetailBId);
	          }
	      if(StringUtils.isNotEmpty(fourthCategoryId)){
	           
	            twoAuditHistoryUpdateVo.fourthCategoryId = Integer.valueOf(fourthCategoryId);
          }
	     twoAuditHistoryUpdateVo.fourthCategoryRemarks = fourthCategoryRemarks;
	     twoAuditHistoryUpdateVo.auditHistoryDetailAremarks = auditHistoryDetailAremarks;
	     twoAuditHistoryUpdateVo.auditHistoryDetailBremarks = auditHistoryDetailBremarks;
     
	     auditHistoryService.updateTwoAuditHistoryUpdateVo(twoAuditHistoryUpdateVo);
       return ResponseEntity.ok().build();
}

	
	  @ApiResponses({
	    	@ApiResponse(code = 200, message = "成功",response=TwoAuditHistory.class),
	    	@ApiResponse(code = 400 , message = "失败-还有没有结束审核"),
	    })
	  @ApiOperation(value="比较历史审核",notes = "比较历史审核", httpMethod = "GET")
	  @RequestMapping(value = "/audit-histories/twoAuditHistory", method = RequestMethod.GET)
	  public ResponseEntity<TwoAuditHistory> readTwoAuditHistory(
			  @RequestParam(value="auditHistoryIdA")  Long auditHistoryIdA ,
			  @RequestParam(value="auditHistoryIdB")  Long auditHistoryIdB) throws URISyntaxException{
		 AuditHistory auditHistoryA = auditHistoryRepository.findOne(auditHistoryIdA);
		 AuditHistory auditHistoryB = auditHistoryRepository.findOne(auditHistoryIdB);
		 if(auditHistoryA == null ){
			 return ResponseEntity.badRequest().body(null);
		 }
		 if(auditHistoryB == null ){
			 return ResponseEntity.badRequest().body(null);
		 }
		 
		 if(auditHistoryA.getEndTime() == null){
			 return ResponseEntity.badRequest().body(null);
		 }
		 if(auditHistoryB.getEndTime() == null){
			 return ResponseEntity.badRequest().body(null);
		 }
		
		 TwoAuditHistory twoAuditHistory = 
				 auditHistoryService.queryTwoAuditHistory(auditHistoryA, auditHistoryB);
		 twoAuditHistory.auditHistoryA = auditHistoryA;
		 twoAuditHistory.auditHistoryB = auditHistoryB;
		 
		 return ResponseEntity.ok(twoAuditHistory);
	 }
	  
	
    /**
     * POST  /audit-histories : Create a new auditHistory.
     *
     * @param auditHistory the auditHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new auditHistory, or with status 400 (Bad Request) if the auditHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiIgnore
    @RequestMapping(value = "/audit-histories", method = RequestMethod.POST)  
    public ResponseEntity<AuditHistory> createAuditHistory(@RequestBody AuditHistory auditHistory) throws URISyntaxException {
        log.debug("REST request to save AuditHistory : {}", auditHistory);
        if (auditHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("auditHistory", "idexists", "A new auditHistory cannot already have an ID")).body(null);
        }
        AuditHistory result = auditHistoryRepository.save(auditHistory);
        return ResponseEntity.created(new URI("/api/audit-histories/" + result.getId()))
            .body(result);
    }

    /**
     * PUT  /audit-histories : Updates an existing auditHistory.
     *
     * @param auditHistory the auditHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated auditHistory,
     * or with status 400 (Bad Request) if the auditHistory is not valid,
     * or with status 500 (Internal Server Error) if the auditHistory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiIgnore
    @RequestMapping(value = "/audit-histories", method = RequestMethod.PUT)  
    public ResponseEntity<AuditHistory> updateAuditHistory(@RequestBody AuditHistory auditHistory) throws URISyntaxException {
        log.debug("REST request to update AuditHistory : {}", auditHistory);
        if (auditHistory.getId() == null) {
            return createAuditHistory(auditHistory);
        }
        AuditHistory result = auditHistoryRepository.save(auditHistory);
        return ResponseEntity.ok()
            .body(result);
    }

    /**
     * GET  /audit-histories : get all the auditHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of auditHistories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;
    @RequestMapping(value = "/audit-histories", method = RequestMethod.GET)  
    public List<AuditHistory> getAllAuditHistories()
        throws URISyntaxException {
        log.debug("REST request to get a page of AuditHistories");
        List<AuditHistory> list = auditHistoryRepository.findAll();
        list.forEach(data->{
        	
        	if (data.getStartTime() != null) {
        		data.setStartTimeView( data.getStartTime().format(formatter) );
			}
        	if (data.getEndTime() != null) {
        		data.setEndTimeView( data.getEndTime().format(formatter) );
			}
        	
        });
        return list;
    }

    /**
     * GET  /audit-histories/:id : get the "id" auditHistory.
     *
     * @param id the id of the auditHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the auditHistory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/audit-histories/{id}", method = RequestMethod.GET)  
//    @ApiIgnore
    public ResponseEntity<AuditHistory> getAuditHistory(@PathVariable Long id) {
        log.debug("REST request to get AuditHistory : {}", id);
        AuditHistory auditHistory = auditHistoryRepository.findOne(id);
        
        if (auditHistory.getStartTime() != null) {
        	auditHistory.setStartTimeView( auditHistory.getStartTime().format(formatter) );
		}
    	if (auditHistory.getEndTime() != null) {
    		auditHistory.setEndTimeView( auditHistory.getEndTime().format(formatter) );
		}
        
        return Optional.ofNullable(auditHistory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /audit-histories/:id : delete the "id" auditHistory.
     *
     * @param id the id of the auditHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/audit-histories/{id}", method = RequestMethod.DELETE)  
    public ResponseEntity<Void> deleteAuditHistory(@PathVariable Long id) {
        log.debug("REST request to delete AuditHistory : {}", id);
        auditHistoryRepository.delete(id);
        return ResponseEntity.ok().build();
    }

}
