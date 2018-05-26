package com.auditing.work.dal.daointerface;

import com.auditing.work.dal.dataobject.SecondCategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * Created by innolab on 16-12-30.
 */
public interface SecondCategoryMapper {
    public List<SecondCategory> selectAllSecondCategorybyFirst(@Param("first_id")Integer first_id, @Param("key")String key) throws Exception;
    int insert(SecondCategory secondCategory) throws  Exception;
    SecondCategory selectByLevel(String level) throws Exception;
}
