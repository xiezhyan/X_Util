package com.sanq.product.config.utils.entity;

/**
 * 
 * version: 统一json返回格式
 * author: xiezhyan
 * date: 2018年3月28日
 *
 */
public class Response{

	private static final String OK = "ok";
	private static final String ERROR = "error";
	
	private Meta meta;
	private Object data;
	
	public Response success() {
		this.meta = new Meta(true, OK, ResultCode.RESULT_OK);
		this.data = true;
		return this;
	}
	
	public Response success(Object data) {
		this.meta = new Meta(true, OK, ResultCode.RESULT_OK);
		this.data = data;
		return this;
	}
	
	public Response failure() {
		this.meta = new Meta(false, ERROR, ResultCode.RESULT_ERROR);
		return this;
	}
	
	public Response failure(String message) {
		this.meta = new Meta(false, message, ResultCode.RESULT_ERROR);
		return this;
	}
	
	public Response failure(String message, Integer code) {
		this.meta = new Meta(false, message, code);
		return this;
	}
	
	
	public Response() {
		super();
	}

	public Response(Meta meta, Object data) {
		super();
		this.meta = meta;
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}



	public class Meta {
		private Boolean success;
		private String message;
		private Integer code;
		
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		public Boolean getSuccess() {
			return success;
		}
		public void setSuccess(Boolean success) {
			this.success = success;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Meta(Boolean success, String message, Integer code) {
			super();
			this.success = success;
			this.message = message;
			this.code = code;
		}
		
		public Meta(Boolean success, String message) {
			super();
			this.success = success;
			this.message = message;
		}
		public Meta() {
			super();
		}
	}
}
