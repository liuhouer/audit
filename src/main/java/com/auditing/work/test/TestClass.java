package com.auditing.work.test;

import com.alibaba.fastjson.JSONObject;
import com.auditing.work.controller.FileController;
import com.auditing.work.dal.daointerface.AuditingDetailMapper;
import com.auditing.work.dal.dataobject.AuditingDetail;
import com.auditing.work.result.BaseResult;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class TestClass extends BaseTest{

    @Autowired
    AuditingDetailMapper auditingDetailMapper;

    @Autowired
    FileController fileController;
    @Test
    public void testDB() throws Exception{
        AuditingDetail auditingDetail = new AuditingDetail();
        auditingDetail.setLevel("1");
        auditingDetail.setStandards("a");
        auditingDetail.setDetail("fdf");
        auditingDetail.setResult("bb");
        auditingDetail.setDepartment("中文测试");
        auditingDetail.setIsDelete(0);
        auditingDetailMapper.insert(auditingDetail);
        List<AuditingDetail> list =  auditingDetailMapper.queryAuditingDetail(auditingDetail);
        System.out.println(list.size());
    }

    @Test
    public void testDBLikey() throws Exception{
        BaseResult baseResult = new BaseResult();
        AuditingDetail auditingDetail = new AuditingDetail();
        auditingDetail.setLevel("1");
//        auditingDetail.setStandards("描述细则");
//        auditingDetail.setDetail("时间少于1周");
//        auditingDetail.setResult("bb");
//        auditingDetail.setDepartment("中文测试");
        auditingDetail.setIsDelete(0);
        if (StringUtils.isNotBlank(auditingDetail.getDetail())) {
            auditingDetail.setDetail("%" + auditingDetail.getDetail() + "%");
        }
        if (StringUtils.isNotBlank(auditingDetail.getStandards())) {
            auditingDetail.setStandards("%" + auditingDetail.getStandards() + "%");
        }
        if (StringUtils.isNotBlank(auditingDetail.getLevel())){
            auditingDetail.setLevel(auditingDetail.getLevel()+"%");
        }
        List<AuditingDetail> list =  auditingDetailMapper.queryDetailLikely(auditingDetail);
        baseResult.setData(list);
        buildSuccessResult(baseResult);
        System.out.println(JSONObject.toJSONString(baseResult));
    }

    @Test
    public void testImport() throws Exception{
        String fileName = "/Users/yuweixiang/Downloads/final.xlsx";
        File file = new File(fileName);
        InputStream is = new FileInputStream(file);
        XSSFWorkbook w = new XSSFWorkbook(is);

        XSSFSheet sheet = w.getSheetAt(0);

        fileController.assemblydata2(sheet);
    }

    @Test
    public void testAuditing(){

    }
}
