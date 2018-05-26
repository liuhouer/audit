package com.auditing.work.controller;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.utils.mapper.BeanMapper;

import com.auditing.work.dal.daointerface.UsersMapper;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.jpa.dao.AnnouncementDocRepository;
import com.auditing.work.jpa.dao.AnnouncementRepository;
import com.auditing.work.jpa.po.Announcement;
import com.auditing.work.jpa.po.AnnouncementCreateVo;
import com.auditing.work.jpa.po.AnnouncementDoc;
import com.auditing.work.jpa.po.AnnouncementEditVo;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing Announcement.
 */
@RestController
@RequestMapping("/api")
@Api(tags="公告 API")
public class AnnouncementResource {

    private final Logger log = LoggerFactory.getLogger(AnnouncementResource.class);
        
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired 
    private AnnouncementDocRepository announcementDocRepository;
    @Autowired
    protected UsersMapper usersMapper;
    /**
     * POST  /announcements : Create a new announcement.
     *
     * @param announcement the announcement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new announcement, or with status 400 (Bad Request) if the announcement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/announcements", method = RequestMethod.POST)  
    @ApiOperation(value="创建公告",notes = "创建公告", httpMethod = "POST")
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody AnnouncementCreateVo announcementCreateVo) throws URISyntaxException {
        log.debug("REST request to add Announcement : {}", announcementCreateVo);
        Announcement announcement = BeanMapper.map(announcementCreateVo, Announcement.class);
        announcement.setCreationTime(LocalDate.now());     
        Announcement result = announcementRepository.save(announcement);
        announcementCreateVo.getDocList().forEach(docId->{
        	 AnnouncementDoc announcementDoc = announcementDocRepository.findOne(docId);
        	 announcementDoc.setAnnouncementId(result.getId());
        	 announcementDocRepository.save(announcementDoc);
        });
        
        return ResponseEntity.ok().body(result);
    }

    
    @RequestMapping(value = "/announcements", method = RequestMethod.PUT)  
    @ApiOperation(value="编辑公告",notes = "编辑公告", httpMethod = "PUT")
    public ResponseEntity<Announcement> editAnnouncement(@RequestBody AnnouncementEditVo announcementEditVo) throws URISyntaxException {
        log.debug("REST request to edit Announcement : {}", announcementEditVo);
        Announcement announcement = BeanMapper.map(announcementEditVo, Announcement.class);
        if (announcement.getId() == null) {
			return ResponseEntity.badRequest().header("errorMessage", "id null").body(null);
		}
        announcement.setCreationTime(LocalDate.now());     
        Announcement result = announcementRepository.save(announcement);
        
        announcementDocRepository.findByAnnouncementId(announcement.getId()).forEach(adoc->{
        		adoc.setAnnouncementId(null);
        	 announcementDocRepository.save(adoc);
        });
        
        announcementEditVo.getDocList().forEach(docId->{
        	 AnnouncementDoc announcementDoc = announcementDocRepository.findOne(docId);
        	 announcementDoc.setAnnouncementId(result.getId());
        	 announcementDocRepository.save(announcementDoc);
        });
        
        return ResponseEntity.ok().body(result);
    }



    /**
     * GET  /announcements : get all the announcements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of announcements in body
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;
    @RequestMapping(value = "/announcements", method = RequestMethod.GET)  
    @ApiOperation(value="公告列表",notes = "公告列表", httpMethod = "GET")
    public List<Announcement> getAllAnnouncements(@RequestParam(value="key",required=false) String  key) {
        log.debug("REST request to get all Announcements");
    	Specification<Announcement> spec = new Specification<Announcement>() {
			
			@Override
			public Predicate toPredicate(Root<Announcement> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				

				
				if (StringUtils.isNotBlank(key)) {
					List<Predicate> predicates = Lists.newArrayList();
					predicates.add(cb.like(root.get("title").as(String.class), "%"+key+"%"));
					predicates.add(cb.like(root.get("content").as(String.class), "%"+key+"%"));
					return cb.or(predicates.toArray(new Predicate[predicates.size()]));
				}else{
					return cb.conjunction();
				}
			
			
				
			}
		};
		
		List<Announcement> list = announcementRepository.findAll(spec);
		list.forEach(announcement->{
			Users user =  usersMapper.selectByPrimaryKey(announcement.getUserId());
			if (user != null) {
				announcement.setUserName(user.getUserName()) ;
			}
			
			announcement.setCreationTimeView( announcement.getCreationTime().format(formatter) );
		});
        return list;
    }

    /**
     * GET  /announcements/:id : get the "id" announcement.
     *
     * @param id the id of the announcement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the announcement, or with status 404 (Not Found)
     */
    @RequestMapping(value="/announcements/{id}", method = RequestMethod.GET)
    @ApiOperation(value="获取公告",notes = "获取公告", httpMethod = "GET")
    public ResponseEntity<Announcement> getAnnouncement(@PathVariable Long id) {
        log.debug("REST request to get Announcement : {}", id);
        Announcement announcement = announcementRepository.findOne(id);
        announcement.setUserName( usersMapper.selectByPrimaryKey(announcement.getUserId()).getUserName()  );
		announcement.setCreationTimeView( announcement.getCreationTime().format(formatter) );
        return Optional.ofNullable(announcement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /announcements/:id : delete the "id" announcement.
     *
     * @param id the id of the announcement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value="/announcements/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value="删除公告",notes = "删除公告", httpMethod = "DELETE")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        log.debug("REST request to delete Announcement : {}", id);
        announcementRepository.delete(id);
        return ResponseEntity.ok().build();
    }




}
