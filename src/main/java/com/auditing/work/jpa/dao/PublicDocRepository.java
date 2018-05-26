package com.auditing.work.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.auditing.work.jpa.po.PublicDoc;

/**
 * Spring Data JPA repository for the ReviewPointDoc entity.
 */
public interface PublicDocRepository extends JpaRepository<PublicDoc,Long> ,JpaSpecificationExecutor<PublicDoc>{



}
