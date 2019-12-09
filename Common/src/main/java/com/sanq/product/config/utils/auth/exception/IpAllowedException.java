package com.sanq.product.config.utils.auth.exception;

/**
 * com.sanq.product.config.utils.auth.exception.IpAllowedException
 *
 * @author sanq.Yan
 * @date 2019/12/6
 */
public class IpAllowedException extends Exception {
    private String msg;

    public IpAllowedException(String msg) {
        super();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
