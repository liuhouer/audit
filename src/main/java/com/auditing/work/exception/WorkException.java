package com.auditing.work.exception;


import com.auditing.work.enums.ResultCodeEnum;

/**
 * 我的异常
 *
 * @author wb-yuweixiang
 * @version $Id: myException.java, v 0.1 2014-7-24 下午4:59:23 wb-yuweixiang Exp $
 */
public class WorkException extends RuntimeException {

    /** 序列号 */
    private static final long serialVersionUID = 7721109146547159676L;

    /** 服务结果码枚举 */
    private ResultCodeEnum resultCode;

    /**
     * Constructors
     * 
     * @param resultCode 服务结果码枚举
     * @param message 异常信息
     */
    public WorkException(ResultCodeEnum resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    /**
     * Constructors
     * 
     * @param e 异常
     * @param resultCode 服务结果码枚举
     * @param resultDesc 异常信息
     */
    public WorkException(Throwable e, ResultCodeEnum resultCode, String resultDesc) {
        super(resultDesc, e);
        this.resultCode = resultCode;
    }

    /**
     * Getter method for property <tt>resultCode</tt>.
     * 
     * @return property value of resultCode
     */
    public String getErrorCode() {
        if (resultCode != ResultCodeEnum.SUCCESS) {
            return resultCode.getCode();
        }
        return null;
    }

    /**
     * Getter method for property <tt>resultCode</tt>.
     * 
     * @return property value of resultCode
     */
    public ResultCodeEnum getResultCode() {
        return resultCode;
    }

    /**
     * Setter method for property <tt>resultCode</tt>.
     * 
     * @param resultCode value to be assigned to property resultCode
     */
    public void setResultCode(ResultCodeEnum resultCode) {
        this.resultCode = resultCode;
    }
}
