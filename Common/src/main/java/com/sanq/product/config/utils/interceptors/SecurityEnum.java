package com.sanq.product.config.utils.interceptors;

/**
 * com.sanq.product.config.utils.interceptors.SecurityEnum
 *
 * @author sanq.Yan
 * @date 2019/7/15
 */
public enum SecurityEnum {

    //需要验证的参数
    TOKEN("token"),
    TIMESTAMP("timestamp"),
    SIGN("sign"),
    CLIENT("client"),
    APP("APP");

    private String mName;

    SecurityEnum(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }
}
