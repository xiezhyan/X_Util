package com.sanq.product.config.utils.string;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DigestUtil {

	private DigestUtil() {}
	
	private static DigestUtil instance;
	
	public static DigestUtil getInstance() {
		if(instance == null) {
			synchronized (DigestUtil.class) {
				if(instance == null) {
					instance = new DigestUtil();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 
	 *	version:md5加密
	 *	@param msg	要加密的数据
	 *	@return
	 *----------------------
	 * 	author:xiezhyan
	 *	date:2017-6-5
	 */
	public String md5(String msg) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bs = digest.digest(msg.getBytes("UTF-8"));
			if(bs != null) {
				StringBuffer sb = new StringBuffer();
				String hexStr;
				for(byte b : bs) {
					int i = b & 0xFF;
					hexStr = Integer.toHexString(i);
					if(hexStr.length() < 2) {
						hexStr = "0" + hexStr;
					}
					sb.append(hexStr);
				}
				return sb.toString();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 *		version:加密方法
	 *		@param encodeStr 需要加密的数据
	 *		@return 加密后的数据
	 *-------------------------------------
	 *		author:xiezhyan
	 *		date:2017-4-28
	 */
	public String encode(String encodeStr) {
		if(encodeStr != null && !"".equals(encodeStr)) {
			BASE64Encoder encoder = new BASE64Encoder();
			try {
				return encoder.encode(encodeStr.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	/**
	 * 解密
	 * @param decodeStr   需要解密的数据
	 * @return	原始数据
	 */
	public String decode(String decodeStr){
		if(decodeStr != null && !"".equals(decodeStr)) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				return new String(decoder.decodeBuffer(decodeStr),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
