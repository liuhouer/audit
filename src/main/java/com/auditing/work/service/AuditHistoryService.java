package com.auditing.work.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.auditing.work.dal.daointerface.FourthCategoryMapper;
import com.auditing.work.dal.dataobject.FourthCategory;
import com.auditing.work.jpa.dao.AuditHistoryDetailDao;
import com.auditing.work.jpa.dao.AuditHistoryRepository;
import com.auditing.work.jpa.po.AuditHistory;
import com.auditing.work.jpa.po.AuditHistoryDetail;
import com.auditing.work.modle.vo.TwoAuditHistory;
import com.auditing.work.modle.vo.TwoAuditHistoryDetail;
import com.auditing.work.modle.vo.TwoAuditHistoryUpdateVo;
import com.google.common.collect.Lists;

@Service
public class AuditHistoryService {
	  @Autowired
	    protected  FourthCategoryMapper fourthCategoryMapper;
	  @Autowired
	    private AuditHistoryRepository auditHistoryRepository;
	  @Autowired
	    private AuditHistoryDetailDao auditHistoryDetailDao;
	
	
	 List<FourthCategory> queryAll(){
		try {
			return fourthCategoryMapper.selectAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Lists.newArrayList();
					
		}
	}

	 public TwoAuditHistory queryTwoAuditHistory(AuditHistory auditHistoryA ,
			 AuditHistory auditHistoryB){
		 List<FourthCategory> fourthCategoryList = this.queryAll();
		 List<TwoAuditHistoryDetail> list = Lists.newArrayList();
		 TwoAuditHistory twoAuditHistory = new TwoAuditHistory();
		 for (FourthCategory fourthCategory : fourthCategoryList) {
			 TwoAuditHistoryDetail twoAuditHistoryDetail = new TwoAuditHistoryDetail();
			 twoAuditHistoryDetail.fourthCategory = fourthCategory;
			 twoAuditHistoryDetail.auditHistoryDetailA =
					 this.auditHistoryDetailDao.
					 findByFourthCategoryIdAndAuditHistoryId(fourthCategory.getId(), auditHistoryA.getId());
		
			 twoAuditHistoryDetail.auditHistoryDetailB =
					 this.auditHistoryDetailDao.
					 findByFourthCategoryIdAndAuditHistoryId(fourthCategory.getId(), auditHistoryB.getId());
			 list.add(twoAuditHistoryDetail);
		 }
		 twoAuditHistory.auditHistoryDetails = list;
		 return twoAuditHistory;
	 }

	 
	 
	public ResponseEntity<AuditHistory> updateAuditHistory(AuditHistory auditHistory) {
		
		List<FourthCategory> fourthCategoryList = this.queryAll();
		
	
		fourthCategoryList.forEach(fourthCategory->{
			AuditHistoryDetail auditHistoryDetail = new AuditHistoryDetail();
			auditHistoryDetail.setAuditHistoryId(auditHistory.getId()); 
			auditHistoryDetail.setFourthCategoryId(fourthCategory.getId());
			auditHistoryDetail.setScore(fourthCategory.getTotal_score());
			auditHistoryDetailDao.save(auditHistoryDetail);
		});
		
		
		
		// TODO Auto-generated method stub
		  AuditHistory result = auditHistoryRepository.save(auditHistory);
	        return ResponseEntity.ok()
	            .body(result);
	}

	public void updateTwoAuditHistoryUpdateVo(TwoAuditHistoryUpdateVo twoAuditHistoryUpdateVo) {
		// TODO Auto-generated method stub
		AuditHistoryDetail auditHistoryDetailA = null;
		
		//c处理空指针异常  by bruce 2017-8-22
		if(twoAuditHistoryUpdateVo.auditHistoryDetailAId!=null){
			auditHistoryDetailA = auditHistoryDetailDao.findOne(twoAuditHistoryUpdateVo.auditHistoryDetailAId);
		}
		
				
		
		AuditHistoryDetail auditHistoryDetailB = null;
		if(twoAuditHistoryUpdateVo.auditHistoryDetailBId!=null){
			auditHistoryDetailB = auditHistoryDetailDao.findOne(twoAuditHistoryUpdateVo.auditHistoryDetailBId);
		
		}
		if (auditHistoryDetailA != null) {
			auditHistoryDetailA.setRemarks(twoAuditHistoryUpdateVo.auditHistoryDetailAremarks);
			auditHistoryDetailDao.save(auditHistoryDetailA);
		}
		
		if (auditHistoryDetailB != null) {
			auditHistoryDetailB.setRemarks(twoAuditHistoryUpdateVo.auditHistoryDetailBremarks);
			auditHistoryDetailDao.save(auditHistoryDetailB);
		}
		
		 FourthCategory fourthCategory = new FourthCategory();
         fourthCategory.setId(twoAuditHistoryUpdateVo.fourthCategoryId);

     
             fourthCategory.setRemarks(twoAuditHistoryUpdateVo.fourthCategoryRemarks);
        
    
         try {
			fourthCategoryMapper.updateFourthCategory(fourthCategory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
