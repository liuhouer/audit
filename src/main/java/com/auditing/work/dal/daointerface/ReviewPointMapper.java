package com.auditing.work.dal.daointerface;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.auditing.work.dal.dataobject.ReviewPoint;
/**
 * Created by innolab on 16-12-25.
 */
public interface ReviewPointMapper {

    public List<ReviewPoint> selectAllReviewPointByFourthId(Integer fourth_id) throws Exception;
    public List<ReviewPoint> queryByFourthId(Integer fourth_id);
    public List<ReviewPoint> selectByDepartmentAndFourth(@Param("department_id")String department_id, @Param("fourth_id")Integer fourth_id) throws Exception;
    public List<ReviewPoint> selectByDetailAndFourth(@Param("detail")String detail, @Param("fourth_id")Integer fourth_id) ;
    public List<ReviewPoint> selectAllReviewPoint() throws Exception;
    ReviewPoint selectReviewPointById(Integer id);
    ReviewPoint queryReviewPointById(Integer id);
    public Integer countReviewPointByStatus(							
			@Param("status")Integer status ,
			@Param("depName")String depName	,
			@Param("userName")String userName) throws  Exception;
    void updateReviewPoint(ReviewPoint reviewPoint) throws  Exception;
    int insert(ReviewPoint reviewPoint) throws  Exception;
    void setEdit() ;
    void setNotEdit() ;
    void setManagerEdit() ;
    void setNotManagerEdit() ;
    void stautsReset() ;
    //<!-- 清空未读消息 -->
    int messageReset() ;
    //<!-- 清空附件 -->
    int docReset() ;
    // <!-- 清空留言 -->
    int commentReset() ;
    //<!-- 清空审核记录 -->
    int actionlogReset() ;
    
    public List<ReviewPoint> select(@Param("department_id")String department_id,							
    								@Param("fourth_id")Integer fourth_id,
    								@Param("status")Integer status, 
    								@Param("key")String key,
    								@Param("docFileName")String docFileName) throws Exception;
    
    public List<ReviewPoint> selectPage(@Param("department_id")String department_id,							
			@Param("fourth_id")Integer fourth_id,
			@Param("status")Integer status, 
			@Param("key")String key,
			@Param("docFileName")String docFileName,
			@Param("startRow")Integer startRow, 
			@Param("size")Integer size) throws Exception;
    
    
    int deleteReviewPoint();
    int deleteFourthCategory();
    int deleteThirdCategory();
    int deleteSecondCategory();
    int deleteFirstCategory();
}
