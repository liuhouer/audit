package com.auditing.work.controller;

import com.alibaba.fastjson.JSONObject;
import com.auditing.work.dal.dataobject.*;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.exception.WorkException;
import com.auditing.work.modle.RowItem;
import com.auditing.work.result.BaseResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by innolab on 16-12-17.
 */
@RequestMapping("/api")
@Controller
public class AuditsController extends BaseController {
    @RequestMapping(value = "/audits", method = RequestMethod.GET)
    public DeferredResult<String> get(final HttpServletRequest request) {
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        BaseResult baseResult = new BaseResult();
        try {
            RowItem rowItem = new RowItem();
            Object ob = getClassFromRequest(request, RowItem.class, deferredResult);
            if (ob != null) {
                rowItem = (RowItem) ob;
            }

            AuditingDetail auditingDetail = buildAudtingDetail(rowItem);
            if (StringUtils.isNotBlank(auditingDetail.getDetail())) {
                auditingDetail.setDetail("%" + auditingDetail.getDetail() + "%");
            }
            if (StringUtils.isNotBlank(auditingDetail.getStandards())) {
                auditingDetail.setStandards("%" + auditingDetail.getStandards() + "%");
            }
            if (StringUtils.isNotBlank(auditingDetail.getLevel())) {
                auditingDetail.setLevel(auditingDetail.getLevel() + "%");
            }
            List<AuditingDetail> list = auditingDetailMapper.queryDetailLikely(auditingDetail);
            System.out.println(list.size());
            List<FirstLevels> firstLevelses = new ArrayList<FirstLevels>();

            for (AuditingDetail detail : list) {
                String level = detail.getLevel();
                String[] levelList = level.split("\\.");

                if (levelList.length == 4) {
                    for (FirstLevels first : firstLevelses) {
                        if (first.getLevel().equals(levelList[0])) {
                            List<SecondLevels> seconds = first.getSeconds();
                            for (SecondLevels second : seconds) {
                                if (second.getSecondLevel().equals(levelList[1])) {
                                    List<ThirdLevels> thirds = second.getThirds();
                                    for (ThirdLevels third : thirds) {
                                        if (third.getThirdLevel().equals(levelList[2])) {
                                            third.getAudits().add(detail);
                                            break;
                                        }
                                    }
                                    break;
                                }

                            }
                            break;
                        }
                    }

                } else if (levelList.length == 3) {
                    ThirdLevels thirdLevels = new ThirdLevels();
                    thirdLevels.setId(detail.getId());
                    thirdLevels.setLevel(level);
                    thirdLevels.setThirdLevel(levelList[2]);
                    thirdLevels.setStandards(detail.getStandards());
                    List<AuditingDetail> audits = new ArrayList<AuditingDetail>();
                    thirdLevels.setAudits(audits);

                    for (FirstLevels first : firstLevelses) {
                        if (first.getLevel().equals(levelList[0])) {
                            List<SecondLevels> seconds = first.getSeconds();
                            for (SecondLevels second : seconds) {
                                if (second.getSecondLevel().equals(levelList[1])) {
                                    second.getThirds().add(thirdLevels);
                                    break;
                                }
                            }
                            break;
                        }
                    }

                } else if (levelList.length == 2) {
                    SecondLevels secondLevels = new SecondLevels();
                    secondLevels.setId(detail.getId());
                    secondLevels.setLevel(level);
                    secondLevels.setSecondLevel(levelList[1]);
                    secondLevels.setStandards(detail.getStandards());
                    List<ThirdLevels> thirds = new ArrayList<ThirdLevels>();
                    secondLevels.setThirds(thirds);

                    for (FirstLevels first : firstLevelses) {
                        if (first.getLevel().equals(levelList[0])) {
                            first.getSeconds().add(secondLevels);
                            break;
                        }
                    }


                } else if (levelList.length == 1) {
                    FirstLevels firstLevels = new FirstLevels();
                    firstLevels.setId(detail.getId());
                    firstLevels.setLevel(level);
                    firstLevels.setStandards(detail.getStandards());
                    List<SecondLevels> seconds = new ArrayList<SecondLevels>();
                    firstLevels.setSeconds(seconds);
                    firstLevelses.add(firstLevels);
                }
            }
            List<AuditReturn> auditReturns = new ArrayList<AuditReturn>();
            for (FirstLevels firstLevels : firstLevelses) {
                for (SecondLevels secondLevels : firstLevels.getSeconds()) {
                    for (ThirdLevels thirdLevels : secondLevels.getThirds()) {
                        AuditReturn auditReturn = new AuditReturn();
                        auditReturn.setStandards1(firstLevels.getLevel()+firstLevels.getStandards());
                        auditReturn.setId1(firstLevels.getId());
                        auditReturn.setStandards2(secondLevels.getLevel()+secondLevels.getStandards());
                        auditReturn.setId2(secondLevels.getId());
                        auditReturn.setStandards3(thirdLevels.getLevel()+thirdLevels.getStandards());
                        auditReturn.setId3(thirdLevels.getId());
                        List<AuditingDetail> details = new ArrayList<AuditingDetail>();
                        auditReturn.setAudits(details);
                        auditReturns.add(auditReturn);
                        for (AuditingDetail auditingD : thirdLevels.getAudits()) {
                            auditReturn.getAudits().add(auditingD);
                        }
                    }
                }
            }
            baseResult.setData(auditReturns);
            buildSuccessResult(baseResult, deferredResult);
        } catch (Throwable e) {
            buildErrorResult(baseResult, ResultCodeEnum.SYSTEM_ERROR.getCode(), ResultCodeEnum.SYSTEM_ERROR.getDesc(), deferredResult);
        }
        return deferredResult;
    }


    @RequestMapping(value = "/audits", method = RequestMethod.POST)
    public DeferredResult<String> post(final HttpServletRequest request) {

        DeferredResult<String> deferredResult = new DeferredResult<String>();
        BaseResult baseResult = new BaseResult();
        try {
            Object ob = getClassFromRequestNotEncode(request, AuditingDetail.class, deferredResult);

            AuditingDetail auditingDetail = new AuditingDetail();

            if (ob != null) {
                auditingDetail = (AuditingDetail) ob;
                auditingDetailMapper.updateByPrimaryKey(auditingDetail);
            }

            baseResult.setData(null);
            buildSuccessResult(baseResult, deferredResult);
        } catch (Throwable e) {
            buildErrorResult(baseResult, ResultCodeEnum.SYSTEM_ERROR.getCode(), ResultCodeEnum.SYSTEM_ERROR.getDesc(), deferredResult);
        }
        return deferredResult;
    }


}