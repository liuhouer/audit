package com.auditing.work.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auditing.work.jpa.po.ReviewPointMessage;

public interface ReviewPointMessageDao extends JpaRepository<ReviewPointMessage, Integer> {
	static final Integer readed = 1;
	static final Integer noeReaded = 0;
}
