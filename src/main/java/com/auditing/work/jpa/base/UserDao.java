package com.auditing.work.jpa.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auditing.work.jpa.po.User;

public interface UserDao extends JpaRepository<User,Integer> {

}
