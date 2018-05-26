package com.auditing.work.dal.daointerface;

import com.auditing.work.dal.dataobject.ThirdCategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * Created by innolab on 16-12-30.
 */
public interface ThirdCategoryMapper {
    public List<ThirdCategory> selectAllThirdCategoryBySecond(@Param("second_id")Integer second_id,@Param("key") String key) throws Exception;
    int insert(ThirdCategory thirdCategory) throws  Exception;
    ThirdCategory selectByLevel(String level) throws Exception;
}
