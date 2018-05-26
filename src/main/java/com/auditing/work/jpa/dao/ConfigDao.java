package com.auditing.work.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auditing.work.jpa.po.Config;

public interface ConfigDao  extends JpaRepository<Config,Long> {
	Config findByKey(String key);
	static final String key_reviewPointisEdit = "reviewPointisEdit";
	static final String key_reviewPointisManagerEdit = "reviewPointisManagerEdit";
}
