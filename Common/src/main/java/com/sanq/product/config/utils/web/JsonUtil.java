package com.sanq.product.config.utils.web;

import com.alibaba.fastjson.JSON;

import java.util.List;


public class JsonUtil {

	private JsonUtil() {}
	
	/**
	 * 
	 *	version: 将Object转换成json字符串
	 *	@param obj
	 *	@return
	 *-------------------------------------
	 *	author:xiezhyan
	 *	date:2017-5-16
	 */
	public static String obj2Json(Object obj) {
		
		return JSON.toJSONString(obj);
	}
	
	/**
	 * 
	 *	version: 将JSON字符串转换成java对象
	 *	@param jsonStr
	 *	@return
	 *-------------------------------------
	 *	author:xiezhyan
	 *	date:2017-5-16
	 */
	public static <T> T json2Obj(String jsonStr,Class<T> clazz) {
		
		return JSON.parseObject(jsonStr, clazz);
	}
	
	/**
	 * 
	 *	version:解析json数组转换成List集合
	 *	@param jsonStr
	 *	@return
	 *-------------------------------------
	 *	author:xiezhyan
	 *	date:2017-5-16
	 */
	public static <T> List<T> json2ObjList(String jsonStr,Class<T> clazz) {
		
		return JSON.parseArray(jsonStr, clazz);
	}

}
