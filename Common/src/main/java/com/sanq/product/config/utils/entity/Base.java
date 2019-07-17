package com.sanq.product.config.utils.entity;

import java.io.Serializable;

/**
 * com.sanq.product.config.utils.entity.Base
 *
 * @author sanq.Yan
 * @date 2019/7/16
 */
public class Base implements Serializable {
    private String token;
    private Long timestamp;
    private String sign;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
