
package com.sanq.product.config.utils.auth.exception;

/**
 * Title: 自定义的RuntimeException
 * Description:参数不全
 */
public class NoParamsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String msg;

	public NoParamsException(String msg) {
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
