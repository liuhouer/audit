package com.auditing.work.jpa.dao;

import org.springframework.data.jpa.repository.*;

import com.auditing.work.jpa.po.DepDict;

import java.util.List;

/**
 * Spring Data JPA repository for the DepDict entity.
 */
@SuppressWarnings("unused")
public interface DepDictRepository extends JpaRepository<DepDict,Long> {

}
