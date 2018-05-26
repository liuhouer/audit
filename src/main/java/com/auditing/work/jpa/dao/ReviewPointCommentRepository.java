package com.auditing.work.jpa.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.auditing.work.jpa.po.ReviewPointComment;

/**
 * Spring Data JPA repository for the ReviewPointComment entity.
 */
public interface ReviewPointCommentRepository extends JpaRepository<ReviewPointComment,Long> {

	List<ReviewPointComment> findByReviewPointId(Long reviewPointId);
	Page<ReviewPointComment> findByReviewPointId(Long reviewPointId,Pageable pageable);
}
