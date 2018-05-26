package com.auditing.work.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auditing.work.jpa.po.ReviewPointActionLog;

public interface ReviewPointActionLogDao extends JpaRepository<ReviewPointActionLog,Long> {

}
