package com.auditing.work.modle;

/**
 * 请求参数表单
 * 
 * @author wb-yuweixiang
 * @version $Id: RequestForm.java, v 0.1 2014-8-21 下午01:53:15 wb-yuweixiang Exp $
 */
public class RequestForm extends BaseRequest {

    /** 序列号 */
    private static final long serialVersionUID = -1073504461591955753L;

    /** 用户名称 */
    //    @Number(lessThanOrEqualTo = 9999999, greaterThanOrEqualTo = 0)
    private String            userId;

    /**
     * Getter method for property <tt>userId</tt>.
     * 
     * @return property value of userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Setter method for property <tt>userId</tt>.
     * 
     * @param userId value to be assigned to property userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
