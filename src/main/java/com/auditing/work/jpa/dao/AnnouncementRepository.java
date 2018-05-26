package com.auditing.work.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.auditing.work.jpa.po.Announcement;

/**
 * Spring Data JPA repository for the Announcement entity.
 */
public interface AnnouncementRepository extends JpaRepository<Announcement,Long>  ,JpaSpecificationExecutor<Announcement>{

}
