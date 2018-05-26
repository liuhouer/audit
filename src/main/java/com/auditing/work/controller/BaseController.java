package com.auditing.work.controller;

import com.alibaba.fastjson.JSONObject;
import com.auditing.work.auditing.DependentStrategy;
import com.auditing.work.auditing.Rule;
import com.auditing.work.dal.daointerface.*;
import com.auditing.work.dal.dataobject.AuditingDetail;
import com.auditing.work.dal.dataobject.ThirdCategory;
import com.auditing.work.enums.AuditingStrategyEnum;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.exception.WorkException;
import com.auditing.work.modle.BaseRequest;
import com.auditing.work.modle.RowItem;
import com.auditing.work.result.BaseResult;
import com.auditing.work.template.RestMethod;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Prop;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础控制类
 * @author wb-yuweixiang
 * @version $Id: BaseController.java, v 0.1 2014-8-22 下午01:38:29 wb-yuweixiang Exp $
 */
public class BaseController {

    protected static final Logger  LOGGER = LoggerFactory.getLogger(BaseController.class);
    
    public static Prop prop;
    
    public String getProp(String key) {
    	return prop.get(key);
    }

    @Autowired
    protected AuditingDetailMapper auditingDetailMapper;

    @Autowired
    protected UsersMapper usersMapper;


    @Autowired
    protected DependentStrategy    dependentStrategy;

    @Autowired
    protected ReviewPointMapper reviewPointMapper;

    @Autowired
    protected DepartmentMapper departmentMapper;

    @Autowired
    protected FirstCategoryMapper firstCategoryMapper;

    @Autowired
    protected SecondCategoryMapper secondCategoryMapper;

    @Autowired
    protected ThirdCategoryMapper thirdCategoryMapper;

    @Autowired
    protected FourthCategoryMapper fourthCategoryMapper;

    private static String          header = "{\"typ\":\"JWT\",\"alg\":\"HS256\"}";

    /**
     * 带事务的模板处理方法。不抛异常方法，只返回reslut
     *
     * @param request         处理请求
     * @param result          处理结果
     * @param processCallback 动作回调
     */
    public void processWithQuery(BaseRequest request, BaseResult result,
                                 final DeferredResult<String> deferredResult,
                                 BusinessProcess processCallback) {

        LOGGER.info("-开始业务处理,请求参数为：" + request);
        final long startTime = System.currentTimeMillis();
        try {
            // 业务处理
            processCallback.doProcess();

            LOGGER.info("-业务处理完成.");

        } catch (WorkException e) {

            // 输出错误信息
            LOGGER.error("业务处理错误原因", e);

            buildErrorResult(result, e.getErrorCode(), e.getMessage(), deferredResult);

        } catch (Throwable e) {

            // 输出错误信息
            LOGGER.error("业务处理未知异常.", e);

            buildErrorResult(result, ResultCodeEnum.SYSTEM_ERROR.getCode(), "系统异常！",
                deferredResult);
        }
    }

    /**
     * 构建错误结果
     *
     * @param result 结果对象
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     */
    protected void buildErrorResult(BaseResult result, String errorCode, String errorMsg,
                                    DeferredResult<String> deferredResult) {
        result.setResultCode(errorCode);
        result.setResultMessage(errorMsg);
        deferredResult.setErrorResult(JSONObject.toJSONString(result));
    }

    /**
     * 业务处理
     *
     * @author yuweixiang
     * @version $Id: BusinessProcess.java, v 0.1 2014-12-1 下午11:21:21 yuweixiang Exp $
     */
    public interface BusinessProcess {

        /**
         * 执行业务处理
         *
         * @throws Exception
         */
        void doProcess() throws Exception;
    }

    protected AuditingDetail buildAudtingDetail(RowItem rowItem) {
        AuditingDetail auditingDetail = new AuditingDetail();
        auditingDetail.setDepartment(rowItem.getDepartment());
        auditingDetail.setDetail(rowItem.getDetail());
        auditingDetail.setStandards(rowItem.getStandards());
        auditingDetail.setGmtCreate(new Date());
        auditingDetail.setGmtModified(new Date());
        auditingDetail.setLevel(rowItem.getLevel());
        auditingDetail.setResult(rowItem.getResult());
        auditingDetail.setType(rowItem.getType());
        auditingDetail.setTitle(rowItem.getTitle());
        auditingDetail.setIsDelete(0);
        return auditingDetail;
    }

    protected void buildSuccessResult(BaseResult result, DeferredResult<String> deferredResult) {
        result.setSuccess(true);
        result.setResultCode(ResultCodeEnum.SUCCESS.getCode());
        result.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
        deferredResult.setResult(JSONObject.toJSONString(result));
    }
    //    /**
    //     * 参数校验
    //     * @param baseRequest 请求表单
    //     */
    //    protected void validate(BaseRequest baseRequest) {
    //
    //        ValidateResult validateResult = validateService.validate(baseRequest);
    //
    //        if (!validateResult.isSuccess()) {
    //            //获取错误信息
    //            final FieldError fieldError = validateResult.getError().getFieldError();
    //            throw new WorkException(ResultCodeEnum.ILLEGAL_ARGUMENT,
    //                fieldError.getDefaultMessage());
    //        }
    //    }

    protected Object getClassFromRequest(HttpServletRequest request, Class clazz,
                                         DeferredResult<String> deferredResult) {
        try {
            String json = getData(request, true);

            if (StringUtils.isBlank(json)) {
                return null;
            }
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            LOGGER.info("请求参数有误!", e);
            buildErrorResult(new BaseResult(), ResultCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                ResultCodeEnum.ILLEGAL_ARGUMENT.getDesc(), deferredResult);
            return null;
        }
    }

    protected Object getClassFromRequestNotEncode(HttpServletRequest request, Class clazz,
                                         DeferredResult<String> deferredResult) {
        try {
            String json = getData(request, false);

            if (StringUtils.isBlank(json)) {
                return null;
            }
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            LOGGER.info("请求参数有误!", e);
            buildErrorResult(new BaseResult(), ResultCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResultCodeEnum.ILLEGAL_ARGUMENT.getDesc(), deferredResult);
            return null;
        }
    }

    protected JSONObject getClassFromRequestByJson(HttpServletRequest request,
                                                  DeferredResult<String> deferredResult) {
        try {
            String json = getData(request, false);

            if (StringUtils.isBlank(json)) {
                return null;
            }
            return JSONObject.parseObject(json);
        } catch (Exception e) {
            LOGGER.info("请求参数有误!", e);
            buildErrorResult(new BaseResult(), ResultCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResultCodeEnum.ILLEGAL_ARGUMENT.getDesc(), deferredResult);
            return null;
        }
    }
    /**
     * rest 输入
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getData(HttpServletRequest request, boolean isBase64) throws IOException {
        String method = request.getMethod();
        String ret = null;
        if (method.equalsIgnoreCase(RestMethod.GET) || method.equalsIgnoreCase(RestMethod.DELETE)) {
            ret = request.getQueryString();
        } else {
            ret = getBodyData(request);
        }
        if (ret == null) {
            return null;
        }
        if (isBase64) {
            ret = new String(Base64.decodeBase64(ret), "UTF-8");
        } else {
            ret = URLDecoder.decode(ret, "UTF-8");
        }
        return ret;
    }

    public static String getBodyData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader;
        try {
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (Exception e) {
            LOGGER.info("获取数据异常!", e);
            throw new WorkException(ResultCodeEnum.SYSTEM_ERROR, "数据获取异常");
        }
        return data.toString();
    }

    protected Rule getRule(AuditingStrategyEnum auditingStrategyEnum) {
        switch (auditingStrategyEnum) {
            case DEPEDENT:
                return dependentStrategy;
            default:
                return null;
        }
    }
    
    
    public FileItem findFile(HttpServletRequest request) {
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);

        //DefaultMultipartHttpServletRequest request1 = (DefaultMultipartHttpServletRequest) request;
        CommonsMultipartFile mf = (CommonsMultipartFile) multipartRequest.getFile("file");
        if (mf != null && mf.getFileItem() != null && mf.getFileItem().getSize()>0) {
            return mf.getFileItem();
        } else {
            return null;
        }
    }

    protected static String getUser(String token) {
        try {
            String[] strs = token.split("\\.");
            if (strs.length < 3) {
                throw new WorkException(ResultCodeEnum.TOKEN_ERROR, "token错误!");
            }
            String jsonStr = new String(new BASE64Decoder().decodeBuffer(strs[1]));
            Map<String, String> map = JSONObject.parseObject(jsonStr, Map.class);
            if (new Date().getTime() - Long.parseLong(map.get("time")) > 30 * 60 * 1000) {
                throw new WorkException(ResultCodeEnum.TOKEN_PASSED, "token已⃣过期");
            }
            String userName = map.get("userName");
            if (!StringUtils.equals(buildToken(userName, map.get("time")), token)) {
                throw new WorkException(ResultCodeEnum.TOKEN_ERROR, "token错误!");
            }
            return userName;
        } catch (Exception e) {
            throw new WorkException(ResultCodeEnum.TOKEN_ERROR, "token错误!");
        }
    }

    protected static String buildToken(String userName, String time) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userName", userName);
        if (StringUtils.isBlank(time)) {
            map.put("time", String.valueOf(new Date().getTime()));
        } else {
            map.put("time", time);
        }
        String encodedString = new BASE64Encoder().encode(header.getBytes()) + "."
                               + new BASE64Encoder()
                                   .encode(JSONObject.toJSONString(map).getBytes());
        String encryptStr = HMACSHA256(encodedString.getBytes(), "secret".getBytes());
        return encodedString + "." + encryptStr;
    }

    public static String HMACSHA256(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }
    
    public String success() {
    	BaseResult baseResult = new BaseResult();
    	baseResult.setData("成功");
        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
        baseResult.setSuccess(true);
        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
        return JsonKit.toJson(baseResult);
    }
    
    public String success(Object obj) {
    	BaseResult baseResult = new BaseResult();
    	baseResult.setData(obj);
        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
        baseResult.setSuccess(true);
        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
        return JsonKit.toJson(baseResult);
    }
}
