package com.auditing.work.enums;

public enum DetailTypeEnum {

    A("A", "A类"),

    B("B", "B类"),

    C("C", "C类"),
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
    private DetailTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param resultCode 结果码
     * @return 服务结果枚举
     */
    public static DetailTypeEnum getByCode(String resultCode) {
        for (DetailTypeEnum type : values()) {
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
