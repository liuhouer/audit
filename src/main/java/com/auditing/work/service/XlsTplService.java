package com.auditing.work.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auditing.work.dal.daointerface.FirstCategoryMapper;
import com.auditing.work.dal.daointerface.FourthCategoryMapper;
import com.auditing.work.dal.daointerface.ReviewPointMapper;
import com.auditing.work.dal.daointerface.SecondCategoryMapper;
import com.auditing.work.dal.daointerface.ThirdCategoryMapper;
import com.auditing.work.dal.dataobject.FourthCategory;
import com.auditing.work.jpa.dao.AuditHistoryRepository;
import com.auditing.work.jpa.po.AuditHistory;
import com.auditing.work.modle.vo.FourthcategoryReviewDep;
import com.auditing.work.modle.vo.FourthcategoryReviewDepVo;
import com.auditing.work.modle.vo.TwoAuditHistory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class XlsTplService {
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
    protected  ReviewPointService reviewPointService;
	
	@Autowired
	AuditHistoryService auditHistoryService;
	
   

	public List<Map<String, Object>> getXlsFourthcategoryViewList() {
		List<Map<String, Object>> list = Lists.newArrayList();
		List<FourthCategory> fourthCategories = 
				this.fourthCategoryMapper.selectAll();
		for (FourthCategory fourthCategory : fourthCategories) {
			Map<String, Object> data = Maps.newHashMap();
			data.put("content", fourthCategory.getName());
			data.put("score", fourthCategory.getTotal_score());
			FourthcategoryReviewDep fourthcategoryReviewDep=
					reviewPointService.getFourthcategoryReviewDep(fourthCategory.getId());
			if (fourthcategoryReviewDep.status == 1) {
				data.put("status", "已评");				
			}else{
				data.put("status", "未评审");
			}
			

			List<String> okDepList = Lists.newArrayList();
			List<String> noOkDepList = Lists.newArrayList();
			for (FourthcategoryReviewDepVo vo : fourthcategoryReviewDep.depList) {
				if (vo.type == 1) {
					okDepList.add(vo.depName);
				}else{
					noOkDepList.add(vo.depName);
				}
			}
		
			
			String okDepArrayStr = StringUtils.join(okDepList, ",");;
			String noOkDepArrayStr = StringUtils.join(noOkDepList, ",");;;
			data.put("okDepArrayStr", okDepArrayStr);
		
			data.put("noOkDepArrayStr", noOkDepArrayStr);
			
			list.add(data);
		}
		return list;
	}

	 @Autowired
	     AuditHistoryRepository auditHistoryRepository;

	public TwoAuditHistory getXlsTwoAuditHistoryViewList(Long auditHistoryIdA, Long auditHistoryIdB) {
		 AuditHistory auditHistoryA = auditHistoryRepository.findOne(auditHistoryIdA);
		 AuditHistory auditHistoryB = auditHistoryRepository.findOne(auditHistoryIdB);
		 
		 
		 TwoAuditHistory twoAuditHistory = 
				 auditHistoryService.queryTwoAuditHistory(auditHistoryA, auditHistoryB);
		 twoAuditHistory.auditHistoryA = auditHistoryA;
		 twoAuditHistory.auditHistoryB = auditHistoryB;
		return twoAuditHistory;
	}






}
