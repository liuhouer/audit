package com.auditing.work.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auditing.work.constants.Constants;
import com.auditing.work.dal.daointerface.ReviewPointMapper;
import com.auditing.work.jpa.base.AuditingDetailDao;
import com.auditing.work.jpa.base.UserDao;
import com.auditing.work.jpa.dao.AnnouncementDocRepository;
import com.auditing.work.jpa.dao.AnnouncementRepository;
import com.auditing.work.jpa.dao.ConfigDao;
import com.auditing.work.jpa.dao.DepartmentDao;
import com.auditing.work.jpa.dao.PublicDocRepository;
import com.auditing.work.jpa.dao.ReviewPointActionLogDao;
import com.auditing.work.jpa.dao.ReviewPointCommentRepository;
import com.auditing.work.jpa.dao.ReviewPointDocRepository;
import com.auditing.work.jpa.dao.ReviewPointMessageDao;
import com.auditing.work.jpa.po.User;

@Service
public class SysService {
		@Autowired
		AnnouncementDocRepository announcementDocRepository;
		
		@Autowired
		AnnouncementRepository announcementRepository;
		
		@Autowired
		ConfigDao configDao;
		
		@Autowired
		DepartmentDao departmentDao;
		
		@Autowired
		PublicDocRepository publicDocRepository;
		
		@Autowired
		ReviewPointActionLogDao reviewPointActionLogDao;
		
		@Autowired
		ReviewPointCommentRepository reviewPointCommentRepository;
		
		@Autowired
		ReviewPointDocRepository reviewPointDocRepository;
		
		@Autowired
		ReviewPointMessageDao reviewPointMessageDao;
		@Autowired
		AuditingDetailDao auditingDetailDao;
		
		@Autowired
		ReviewPointMapper reviewPointMapper;
	
	

		@Autowired
		UserDao userDao;
		
		
		
		public void delAll(){
			
			
			 announcementDocRepository.deleteAll();
			
			 announcementRepository.deleteAll();
			
			 configDao.deleteAll();
			
			
			
			 publicDocRepository.deleteAll();
			
			 reviewPointActionLogDao.deleteAll();
			
			 reviewPointCommentRepository.deleteAll();
			
			 reviewPointDocRepository.deleteAll();
			
			 reviewPointMessageDao.deleteAll();
			 auditingDetailDao.deleteAll();
			
			System.out.println("deleteReviewPoint : "+ reviewPointMapper.deleteReviewPoint()); ;
			System.out.println("deleteFourthCategory : "+  reviewPointMapper.deleteFourthCategory());
			System.out.println("deleteThirdCategory : "+  reviewPointMapper.deleteThirdCategory());
			System.out.println("deleteSecondCategory : "+  reviewPointMapper.deleteSecondCategory());
			System.out.println("deleteFirstCategory : "+  reviewPointMapper.deleteFirstCategory());

				
			 userDao.findAll().stream().filter(user->{
				 return !user.getUserName().equals("superadmin");
			 }).forEach(userDao::delete);
//			 userDao.deleteAll();
			 departmentDao.deleteAll();
			
//			
				 if (userDao.findAll().isEmpty()) {
					 User user = new User();
					 user.setUserName("superadmin");
					 user.setPassword("123123");
					 user.setRole(Constants.SUPER_ADMIN);
					
					 userDao.save(user);
			}
				
			
			
			
		}
		
}
