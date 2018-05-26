/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.auditing.work.constants;

/**
 * demo常亮集合地
 * 
 * @author wb-yuweixiang
 * @version $Id: Constants.java, v 0.1 2014-9-10 下午06:18:43 wb-yuweixiang Exp $
 */
public class Constants {
    /* 要点的几种状态, 未审核, 已经审核, 驳回, 驳回后待确认*/
    public final  static Integer UNREVIEWED = 0;
    public final  static Integer REVIEWED = 1;
    public final  static Integer REJECT = 2;
    public final  static Integer REJECT_TO_COMFIRMED = 3;

    public final  static String STR_UNREVIEWED = "unreviewed";
    public final  static String STR_REVIEWED = "reviewed";
    public final  static String STR_REJECT = "reject";
    public final  static String STR_REJECT_TO_COMFIRMED = "reject_to_comfirmed";

    public final static Integer SUPER_ADMIN = 0;
    public final static Integer ADMIN = 1;
    public final static Integer USER = 2;

    /** 首页VM */
    public final static String INDEX_VM    = "index";

    /** 你好世界 */
    public final static String HELLO_WORLD = "helloworld";

    /** 你好世界 */
    public final static String UPLOAD      = "upload";

    /** 错误页面 */
    public final static String ERROR       = "error";

    /** 用户信息 */
    public final static String USER_INFO   = "user_info";

    /** 搜索 */
    public final static String SEARCH   = "search";
}
