package com.auditing.work.jpa.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auditing.work.jpa.po.DepRevierPointStauts;

/**
 * Spring Data JPA repository for the DepDict entity.
 */
public interface DepRevierPointStautsRepository extends JpaRepository<DepRevierPointStauts,Long> {
	DepRevierPointStauts findByReviewPointIdAndDepName(Integer reviewPointId,String depName);
	List<DepRevierPointStauts> findByDepName(String depName);
}
