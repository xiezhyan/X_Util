package com.sanq.product.config.utils.auth.exception;

import com.sanq.product.config.utils.entity.Codes;

/**
 * com.sanq.product.config.utils.auth.exception.BusException
 *
 * @author sanq.Yan
 * @date 2020/1/22
 */
public class BusException extends RuntimeException {
    private String msg;

    private Integer code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BusException(String msg) {
        super();
        this.msg = msg;
        this.code = Codes.RESULT_ERROR;
    }

    public BusException(String msg, Integer code) {
        super();
        this.msg = msg;
        this.code = code;
    }

    public BusException(BusExceptionEnum busExceptionEnum) {
        super();
        this.msg = busExceptionEnum.getMsg();
        this.code = busExceptionEnum.getCode();
    }
}
