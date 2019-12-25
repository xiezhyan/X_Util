package com.sanq.product.config.utils.string;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DigestUtil {

	private DigestUtil() {}
	
	private static volatile DigestUtil instance;
	
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
	public String md5(String msg) throws Exception {
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
		} catch (Exception e) {
			e.printStackTrace();
			throw  e;
		}
		return "";
	}

	/**
	 *  base64加密
	 */
	public String base64Encode(String msg) throws UnsupportedEncodingException {
		if(!StringUtil.isEmpty(msg)) {
			BASE64Encoder encoder = new BASE64Encoder();
			try {
				return encoder.encode(msg.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return "";
	}

	/**
	 *  base64解密
	 */
	public String base64Decode(String msg) throws IOException {
		if(!StringUtil.isEmpty(msg)) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				return new String(decoder.decodeBuffer(msg),"UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return "";
	}
}
