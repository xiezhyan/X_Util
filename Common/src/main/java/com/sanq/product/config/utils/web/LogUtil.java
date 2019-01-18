package com.sanq.product.config.utils.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *	version:主要用于输出信息，不同的级别可以限制在控制台输出，避免使用System.out()来进行输出信息
 *			在正式环境中可以在log4j.properties中设置输出级别
 *			DEBUG < INFO < WARN < ERROR
 *-------------------------------------
 *	author:xiezhyan
 *	date:2017-5-9
 */
public class LogUtil{

	private static Logger logger = null;

	private static LogUtil instance = null;

	private Class clazz;
	private LogUtil(Class clazz) {
		this.clazz = clazz;
	}

	public static LogUtil getInstance(Class clazz) {
		if(instance == null) {
			synchronized (LogUtil.class) {
				if(instance == null) {
					instance = new LogUtil(clazz);
					logger = LoggerFactory.getLogger(clazz);
				}
			}
		}
		return instance;
	}
	
	public void d(String msg) {
		if(logger.isDebugEnabled()) {
			logger.debug(msg);
		}
	}
	
	public void i(String msg) {
		if(logger.isInfoEnabled())
			logger.info(msg);
	}
	
	public void w(String msg) {
		if(logger.isWarnEnabled())
			logger.warn(msg);
	}
	
	public void e(String msg) {
		if(logger.isErrorEnabled())
			logger.error(msg);
	}
	
	
}
