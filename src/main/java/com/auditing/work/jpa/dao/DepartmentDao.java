package com.auditing.work.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auditing.work.jpa.po.Department;

public interface DepartmentDao   extends JpaRepository<Department,Long> {
	Department findByName(String name);
}
