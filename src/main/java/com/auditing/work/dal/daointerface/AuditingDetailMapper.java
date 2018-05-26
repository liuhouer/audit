package com.auditing.work.dal.daointerface;

import com.auditing.work.dal.dataobject.AuditingDetail;

import java.util.List;

public interface AuditingDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AuditingDetail record);

    List<AuditingDetail> queryAuditingDetail(AuditingDetail record);

    List<AuditingDetail> queryDetailLikely(AuditingDetail record);

    int insertSelective(AuditingDetail record);

    AuditingDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuditingDetail record);

    int updateByPrimaryKey(AuditingDetail record);
}