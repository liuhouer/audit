package com.auditing.work.jpa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.auditing.work.jpa.po.ReviewPointDoc;

/**
 * Spring Data JPA repository for the ReviewPointDoc entity.
 */
public interface ReviewPointDocRepository extends JpaRepository<ReviewPointDoc,Long> ,JpaSpecificationExecutor<ReviewPointDoc>{

	List<ReviewPointDoc> findByReviewPointId(Long reviewPointId);
	Long countByReviewPointId(Long reviewPointId);
}
