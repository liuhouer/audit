package com.auditing.work.service;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auditing.work.constants.Constants;
import com.auditing.work.dal.daointerface.ReviewPointActionLogMapper;
import com.auditing.work.dal.daointerface.UsersMapper;
import com.auditing.work.dal.dataobject.ReviewPoint;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.jpa.dao.ReviewPointActionLogDao;
import com.auditing.work.jpa.dao.ReviewPointCommentRepository;
import com.auditing.work.jpa.dao.ReviewPointMessageDao;
import com.auditing.work.jpa.po.ReviewPointActionLog;
import com.auditing.work.jpa.po.ReviewPointComment;
import com.auditing.work.jpa.po.ReviewPointMessage;
import com.auditing.work.modle.vo.ReviewPointActionLogView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class ReviewPointMessageService {
	 @Autowired
	    private ReviewPointActionLogDao reviewPointActionLogDao;
	 @Autowired
	 private ReviewPointMessageDao reviewPointMessageDao;
	 
	 @Autowired
	 private ReviewPointActionLogMapper reviewPointActionLogMapper;
	 @Autowired
	    protected UsersMapper usersMapper;
	public void addActionLog(ReviewPoint reviewPoint, Users user) {
		 ReviewPointActionLog reviewPointActionLog = new ReviewPointActionLog();
         reviewPointActionLog.setActionDate(new Date());
         reviewPointActionLog.setReviewPointId(reviewPoint.getId().longValue());
         reviewPointActionLog.setUserId(user.getId());
         reviewPointActionLog.setStatus(reviewPoint.getStatus());
         reviewPointActionLog = reviewPointActionLogDao.save(reviewPointActionLog);
         
         if (reviewPointActionLog.getStatus() == Constants.REVIEWED) {
			return ;
		 }
         
         Integer role = user.getRole();
         
         if (role == Constants.SUPER_ADMIN) {
        	sendMessage(reviewPoint,reviewPointActionLog,Constants.ADMIN);
        	sendMessage(reviewPoint,reviewPointActionLog,Constants.USER);
		 }
         
         
         if (role == Constants.USER) {
         	sendMessage(reviewPoint,reviewPointActionLog,Constants.USER);
 		 }
          
         if (role == Constants.ADMIN) {
        	sendMessage(reviewPoint,reviewPointActionLog,Constants.ADMIN);
        	sendMessage(reviewPoint,reviewPointActionLog,Constants.USER);
		 }
         
    
		
	}
	
	 void sendMessage(ReviewPoint reviewPoint, ReviewPointActionLog reviewPointActionLog, Integer role){
		 
		 List<ReviewPointMessage> msgList = Lists.newArrayList();
		 
		  reviewPoint.getDepartmentIdList().forEach(depName->{
		 		usersMapper.selectLisByRoleAndDepName(role, depName).forEach(showMessageUser->{
		 			ReviewPointMessage reviewPointMessage = new ReviewPointMessage();	
		 			reviewPointMessage.setUserId(showMessageUser.getId().intValue());
		 			reviewPointMessage.setIsReaded(ReviewPointMessageDao.noeReaded);
		 			reviewPointMessage.setReviewPointActionLogId(reviewPointActionLog.getId());
//		 			reviewPointMessageDao.save(reviewPointMessage);
		 			msgList.add(reviewPointMessage);
		 			
		 		});		 				 
		 });
		  
		  
		  
		 reviewPointMessageDao.save(msgList);
	}
	
	

	
	 		@Autowired
	 	  private ReviewPointCommentRepository reviewPointCommentRepository;

	 	
	public List<ReviewPointActionLogView> queryNoReadActionLog(Integer userId){
		List<ReviewPointActionLogView> list = reviewPointActionLogMapper.queryNoReadByUserId(userId);
		list.forEach(reviewPointActionLogView->{
			
			List<ReviewPointComment> listReviewPointComment = 
					reviewPointCommentRepository.findByReviewPointId(reviewPointActionLogView.getReviewPointId());
			List<Map<String, Object>> restList = Lists.newArrayList();
			for (ReviewPointComment reviewPointComment : listReviewPointComment) {
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
			
			reviewPointActionLogView.setReviewPointCommentList(restList);
			
		});
		
		return list;
	} 
	 
}
