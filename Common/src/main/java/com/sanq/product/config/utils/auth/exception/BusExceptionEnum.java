package com.sanq.product.config.utils.auth.exception;

import com.sanq.product.config.utils.entity.Codes;

/**
 * com.sanq.product.config.utils.auth.exception.BusExceptionEnum
 *
 * @author sanq.Yan
 * @date 2020/1/22
 */
public enum BusExceptionEnum {
    TOKEN("用户Token不存在或以过期", Codes.TOKEN_CODE),
    IP("当前IP禁止访问", Codes.IP_CODE),
    PARAMS("默认参数出现问题", Codes.PARAM_CODE),
    AUTH("权限管理异常", Codes.AUTH_CODE);

    private String msg;
    private Integer code;

    BusExceptionEnum(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }
}
