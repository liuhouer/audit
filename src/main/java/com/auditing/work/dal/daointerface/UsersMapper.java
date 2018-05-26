package com.auditing.work.dal.daointerface;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.auditing.work.dal.dataobject.Users;

public interface UsersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Long id);
    List<Users> selectAllUsers();
    List<Users> selectUsersByDepartmentId(Integer department_id);
    List<Users> selectLisByRoleAndDepName(@Param("role")Integer role,@Param("depName")String depName);

    Users queryByUser(Users record);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
    
    Users selectUserByName(String name);
    
   
}