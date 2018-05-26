package com.auditing.work.dal.daointerface;

import com.auditing.work.dal.dataobject.Department;

import java.util.List;

/**
 * Created by innolab on 16-12-30.
 */
public interface DepartmentMapper {
    public List<Department> selectAllDepartment();
    Department selectDepartmentByName(String name);
    Department selectDepartmentById(Integer id);
}
