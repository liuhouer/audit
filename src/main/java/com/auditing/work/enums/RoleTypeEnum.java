package com.auditing.work.enums;

public enum RoleTypeEnum {


    ADMIN("admin", "管理员"),

    AUDITOR("auditor", "审核员"),

    ;

    /** 枚举代码 */
    private String code;

    /** 枚举描述 */
    private String desc;

    /**
     * Constructors
     *
     * @param code 结果码
     * @param desc 结果描述
     */
    private RoleTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param resultCode 结果码
     * @return 服务结果枚举
     */
    public static RoleTypeEnum getByCode(String resultCode) {
        for (RoleTypeEnum type : values()) {
            if (type.getCode().equals(resultCode)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Getter method for property <tt>code</tt>.
     *
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>desc</tt>.
     *
     * @return property value of desc
     */
    public String getDesc() {
        return desc;
    }
}
