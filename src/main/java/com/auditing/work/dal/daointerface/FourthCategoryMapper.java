package com.auditing.work.dal.daointerface;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.auditing.work.dal.dataobject.FourthCategory;
import com.auditing.work.dal.dataobject.ReviewPointsReturn;

/**
 * Created by innolab on 16-12-30.
 */
public interface FourthCategoryMapper {
    public List<FourthCategory> selectAllFourthCategoryByThird(@Param("third_id") Integer third_id, @Param("key")String key) throws Exception;
//    public List<FourthCategory> selectAllFourthCategoryByThird(@Param("third_id") Integer third_id) throws Exception;
    public List<FourthCategory> selectAll();
    public List<FourthCategory> selectAllView(
    		@Param("key")String key,
    		@Param("userName")String userName,
    		@Param("firstCategoryId")Integer firstCategoryId, 
    		@Param("dep")String dep);
    public List<FourthCategory> selectKeyList();
    public List<FourthCategory> selectList(	@Param("userName")String userName) throws Exception;
    public void updateFourthCategory(FourthCategory fourthCategory) throws Exception;
    void totalScoreReset() ;
    int insert(FourthCategory fourthCategory) throws  Exception;
    FourthCategory selectByLevel(String level) throws Exception;
    FourthCategory selectById(Integer id) throws Exception;
    List<ReviewPointsReturn> query(
    		@Param("key")String key,
    		@Param("userName")String userName,
    		@Param("startRow")Integer startRow,
    		@Param("size")Integer size,
    		@Param("firstCategoryId")Integer firstCategoryId,
    		@Param("fourthCategoryId")Integer fourthCategoryId,
    		@Param("dep")String dep,
    		@Param("depid")Integer depid, 
    		@Param("status")Integer status);
    Integer querytotalRow(@Param("key")String key,
    		@Param("userName")String userName,
    		@Param("firstCategoryId")Integer firstCategoryId, 
    		@Param("fourthCategoryId")Integer fourthCategoryId,
    		@Param("dep")String dep,
    		@Param("depid")Integer depid, 
    		@Param("status")Integer status);
}
