package com.sanq.product.security.enums;

/**
 * com.sanq.product.security.enums.SecurityFieldEnum
 *
 * @author sanq.Yan
 * @date 2019/8/3
 */
public enum SecurityFieldEnum {
    //需要验证的参数
    TOKEN("token"),
    TIMESTAMP("timestamp"),
    SIGN("sign"),
    CLIENT("client"),
    APP("APP"),
    ID("id"),
    PAGE_SIZE("pageSize");

    private String mName;

    SecurityFieldEnum(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }
}
