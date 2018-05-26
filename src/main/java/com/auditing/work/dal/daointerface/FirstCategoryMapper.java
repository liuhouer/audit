package com.auditing.work.dal.daointerface;

import com.auditing.work.dal.dataobject.FirstCategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;


/**
 * Created by innolab on 16-12-30.
 */
public interface FirstCategoryMapper {
    public List<FirstCategory> selectAllFirstCategory() throws Exception;
    int insert(FirstCategory firstCategory)throws Exception;
    FirstCategory selectByLevel(String level) throws Exception;
    public List<FirstCategory> select(@Param("id")Integer id, @Param("key")String key) throws Exception;
}
