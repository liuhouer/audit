package com.auditing.work.auditing;

import com.auditing.work.dal.daointerface.AuditingDetailMapper;
import com.auditing.work.dal.dataobject.AuditingDetail;
import com.auditing.work.enums.DetailTypeEnum;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.enums.RoleTypeEnum;
import com.auditing.work.modle.AuditingRequest;
import com.auditing.work.result.BaseResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependentStrategy implements Rule{

    @Autowired
    private AuditingDetailMapper auditingDetailMapper;

    public boolean auditing(AuditingRequest request, BaseResult result) {
        AuditingDetail auditingDetail = new AuditingDetail();
        if (request.getType().equals(DetailTypeEnum.C.getCode())){
            if(updateRowItem(auditingDetail,request,result)) {
                return true;
            }
        }
        if (request.getType().equals(DetailTypeEnum.B.getCode())){
            if(justType(auditingDetail,request,result,DetailTypeEnum.C)) {
                if (updateRowItem(auditingDetail, request, result)) {
                    return true;
                }
            }
        }

        if (request.getType().equals(DetailTypeEnum.A.getCode())){
            if(justType(auditingDetail,request,result,DetailTypeEnum.C)){
                if(justType(auditingDetail,request,result,DetailTypeEnum.B)){
                    if (updateRowItem(auditingDetail,request,result)){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public boolean updateRowItem(AuditingDetail auditingDetail,AuditingRequest request,BaseResult result){
        auditingDetail.setLevel(request.getLevel());
        auditingDetail.setStandards(request.getStandards());
        auditingDetail.setDetail(request.getDetail());
        auditingDetail.setType(request.getType());
        List<AuditingDetail> auditingDetailList = auditingDetailMapper.queryAuditingDetail(auditingDetail);
        if (auditingDetailList ==null||auditingDetailList.isEmpty()){
            result.setSuccess(false);
            result.setResultCode(ResultCodeEnum.ITEM_NOT_EXIST.getCode());
            result.setResultMessage("没有此信息,无法审核");
            return false;
        }
        auditingDetail = auditingDetailList.get(0);
        if (!StringUtils.equals(auditingDetail.getDepartment(),request.getUserDepartment())){
            result.setSuccess(false);
            result.setResultCode(ResultCodeEnum.CANNOT_AUDIT_ITEM.getCode());
            result.setResultMessage("无法夸部门进行审核!");
            return false;
        }
        if (!StringUtils.equals(request.getUserRole(), RoleTypeEnum.ADMIN.getCode())&&!StringUtils.equals(request.getUserRole(),RoleTypeEnum.AUDITOR.getCode())){
            result.setSuccess(false);
            result.setResultCode(ResultCodeEnum.ITEM_NO_PERMISSION_AUDIT.getCode());
            result.setResultMessage("改用户无法审核次信息!");
            return false;
        }
        auditingDetail.setResult(request.getResult());
        auditingDetailMapper.updateByPrimaryKey(auditingDetail);
        return true;
    }

    public boolean justType(AuditingDetail auditingDetail,AuditingRequest request,BaseResult result,DetailTypeEnum detailTypeEnum){
        auditingDetail.setLevel(request.getLevel());
        auditingDetail.setType(detailTypeEnum.getCode());
        List<AuditingDetail> auditingDetailList = auditingDetailMapper.queryAuditingDetail(auditingDetail);
        for(AuditingDetail auditingDetail1 : auditingDetailList){
            if (!auditingDetail1.getResult().equals("passed")){
                result.setSuccess(false);
                result.setResultCode(ResultCodeEnum.CANNOT_AUDIT_ITEM.getCode());
                result.setResultMessage("存在没有审核通过的"+detailTypeEnum.getCode()+"级目录");
                return false;
            }
        }
        return true;
    }
}
