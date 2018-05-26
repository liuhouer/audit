package com.auditing.work.jpa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.auditing.work.jpa.po.AnnouncementDoc;

/**
 * Spring Data JPA repository for the ReviewPointDoc entity.
 */
public interface AnnouncementDocRepository extends JpaRepository<AnnouncementDoc,Long> ,JpaSpecificationExecutor<AnnouncementDoc>{

	List<AnnouncementDoc> findByAnnouncementId(Long announcementId);

}
