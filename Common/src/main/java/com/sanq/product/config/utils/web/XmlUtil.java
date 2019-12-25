package com.sanq.product.config.utils.web;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class XmlUtil {

	private static volatile XmlUtil instance = null;
	
	private XmlUtil() {}
	
	private static XStream mXstream = null;
	
	public static XmlUtil getInstance() {
		if(instance == null) {
			synchronized (XmlUtil.class) {
				if(instance == null) {
					instance = new XmlUtil();
					mXstream = new XStream(new DomDriver());
				}
			}
		}
		return instance;
	}

	/**
	 * 
	 *	version:将bean转换成xml字符串
	 *	@param obj
	 *	@return
	 *-------------------------------------
	 *	author:xiezhyan
	 *	date:2017-7-20
	 */
	public String parseObjToXml(Object obj, String alias) {
		mXstream.alias(alias, obj.getClass());
		return mXstream.toXML(obj);
	}
	
	/**
	 * 
	 *	version: 将xml字符串转换成obj类型
	 *	@param text	xml字符串
	 *	@param clazz 要转换的bean
	 *	@return
	 *-------------------------------------
	 *	author:xiezhyan
	 *	date:2017-7-20
	 */
	@SuppressWarnings("unchecked")
	public <T> T parseXmlText(String text,Class<T> clazz, String alias) {
		
		mXstream.alias(alias, clazz);
		
		return (T) mXstream.fromXML(text);
	}
	
	/**
	 * 
	 *	version: 将xml文件转换成实体
	 *	@param fileUrl
	 *	@param clazz
	 *	@return
	 *-------------------------------------
	 *	author:xiezhyan
	 *	date:2017-7-20
	 */
	@SuppressWarnings("unchecked")
	public <T> T parseXmlUrl(String fileUrl,Class<T> clazz, String alias) {
		
		mXstream.alias(alias, clazz);
		
		try {
			return (T) mXstream.fromXML(new BufferedReader(new FileReader(fileUrl)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
