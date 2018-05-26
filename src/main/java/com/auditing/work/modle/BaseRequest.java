package com.auditing.work.modle;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 基础表单
 * 
 * @author wb-yuweixiang
 * @version $Id: BaseRequest.java, v 0.1 2014-9-12 下午05:21:19 wb-yuweixiang Exp $
 */
public class BaseRequest implements Serializable {

    /** 序列号 */
    private static final long serialVersionUID = -9176613957620664789L;

    /** 
     * @see Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
