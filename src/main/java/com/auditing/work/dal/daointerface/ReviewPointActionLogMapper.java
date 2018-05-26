package com.auditing.work.dal.daointerface;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.auditing.work.modle.vo.ReviewPointActionLogView;

public interface ReviewPointActionLogMapper {
	

		List<ReviewPointActionLogView> queryNoReadByUserId(@Param("userId")Integer userId);
}
