/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.auditing.work.result;

import com.auditing.work.enums.ResultCodeEnum;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;



/**
 *  * 业务处理基本返回信息
 * 包括所有业务处理的基础返回信息
 *
 * <ul>
 *  <li>成功标志，业务处理结果;
 *  <li>结果码;
 *  <li>结果信息;
 * </ul>
 * </p>
 * 
 * @author wb-yuweixiang
 * @version $Id: BaseResult.java, v 0.1 2014-9-12 下午05:41:40 wb-yuweixiang Exp $
 */
public class BaseResult implements Serializable {

    /** 序列号 */
    private static final long serialVersionUID = -5254648185049414019L;

    /** 是否成功（默认成功，相比设置false，设置true的地方更多，业务出错时请务必设置为false.） */
    protected boolean             isSuccess        = false;

    protected Object              data;

    /** 错误代码 */
    protected String              resultCode       = ResultCodeEnum.SYSTEM_ERROR
                                                       .getCode();

    /** 错误消息 */
    protected String              resultMessage    = ResultCodeEnum.SYSTEM_ERROR
                                                       .getDesc();

    /**
     * 构造函数
     */
    public BaseResult() {
    }

    /**
     * 构造函数
     * @param isSuccess 操作是否成功
     */
    public BaseResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * 构造函数
     * @param isSuccess 操作是否成功
     * @param resultMessage 结果描述信息
     */
    public BaseResult(boolean isSuccess, String resultMessage) {
        this.isSuccess = isSuccess;
        this.resultMessage = resultMessage;
    }

    /**
     * Getter method for property <tt>isSuccess</tt>.
     * 
     * @return property value of isSuccess
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Setter method for property <tt>isSuccess</tt>.
     * 
     * @param isSuccess value to be assigned to property isSuccess
     */
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * Getter method for property <tt>resultCode</tt>.
     * 
     * @return property value of resultCode
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * Setter method for property <tt>resultCode</tt>.
     * 
     * @param resultCode value to be assigned to property resultCode
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * Getter method for property <tt>resultMessage</tt>.
     * 
     * @return property value of resultMessage
     */
    public String getResultMessage() {
        return resultMessage;
    }

    /**
     * Setter method for property <tt>resultMessage</tt>.
     * 
     * @param resultMessage value to be assigned to property resultMessage
     */
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    /**
     * Getter method for property <tt>data</tt>.
     *
     * @return property value of data
     */
    public Object getData() {
        return data;
    }

    /**
     * Setter method for property <tt>data</tt>.
     *
     * @param data value to be assigned to property data
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
