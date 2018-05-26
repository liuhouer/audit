package com.auditing.work.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auditing.work.dal.daointerface.DepartmentMapper;
import com.auditing.work.dal.daointerface.UsersMapper;
import com.auditing.work.dal.dataobject.Department;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.jpa.base.UserDao;
import com.auditing.work.jpa.dao.DepartmentDao;
import com.auditing.work.jpa.po.User;
import com.auditing.work.modle.jf.DepartmentModel;
import com.auditing.work.modle.jf.UserModel;

import jodd.cache.Cache;
import jodd.cache.FIFOCache;

@Component
public class CacheHelper {
	
	@Autowired
	DepartmentMapper departmentMapper;
	@Autowired
	UsersMapper usersMapper;
	
	public static Cache<String, Object> cache = new FIFOCache<String, Object>(10000);
	
	public Department getCacheDepartmentByName(String name) {
		String key = "department:" + name;
		Object obj = cache.get(key);
		if(obj==null) {
			Department department = departmentMapper.selectDepartmentByName(name);
			if(department!=null) {
				cache.put(key, department);
			}
			return department;
		} else {
			return (Department)obj;
		}
	}
	
	public Users getCacheUserByName(String name) {
		String key = "user:" + name;
		Object obj = cache.get(key);
		if(obj==null) {
			Users user = usersMapper.selectUserByName(name);
			if(user!=null) {
				cache.put(key, user);
			}
			return user;
		} else {
			return (Users)obj;
		}
	}
	
	public UserModel getCacheUserById(Long id) {
		String key = "user:id:" + id;
		Object obj = cache.get(key);
		if(obj==null) {
			UserModel user = UserModel.dao.findById(id);
			if(user!=null) {
				cache.put(key, user);
			}
			return user;
		} else {
			return (UserModel)obj;
		}
	}
	
	public DepartmentModel getCacheDepartmentById(Long id) {
		String key = "department:id:" + id;
		Object obj = cache.get(key);
		if(obj==null) {
			DepartmentModel department = DepartmentModel.dao.findById(id);
			if(department!=null) {
				cache.put(key, department);
			}
			return department;
		} else {
			return (DepartmentModel)obj;
		}
	}

}
