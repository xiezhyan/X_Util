package com.sanq.product.generate.utils;

public class StringUtil {

	private static StringUtil instance = null;
	
	private StringUtil() {}
	
	public static StringUtil getInstance() {
		if(instance == null) {
			synchronized (StringUtil.class) {
				if(instance == null)
					instance = new StringUtil();
			}
		}
		return instance;
	}
	
	/**
	 * 
	 *	version:替换字符串中指定字符，并将紧跟在它后面的字母大写
	 *	@param strVal
	 *	@param tag
	 *	@return
	 *----------------------
	 * 	author:xiezhyan
	 *	date:2017-6-5
	 */
	public String replaceChar(String strVal, String tag) {
        StringBuffer sb = new StringBuffer();  
        sb.append(strVal.toLowerCase());  
        int count = sb.indexOf(tag);  
        while(count!=0){  
            int num = sb.indexOf(tag,count);  
            count = num+1;  
            if(num!=-1){  
                char ss = sb.charAt(count);  
                char ia = (char) (ss - 32);  
                sb.replace(count,count+1,ia+"");  
            }  
        }  
      String ss = sb.toString().replaceAll(tag,"");  
      return ss;
	}
	
	/**
	 * 
	 *	version:将字符串首字母大写
	 *	@param strVal
	 *	@return
	 *----------------------
	 * 	author:xiezhyan
	 *	date:2017-6-6
	 */
	public static String firstUpperCase(String strVal) {
		StringBuffer sb = new StringBuffer();
		if(null != strVal && !"".equals(strVal)) {
			sb.append(String.valueOf(strVal.charAt(0)).toUpperCase());
			for(int i = 1; i < strVal.length(); i++) {
				sb.append(strVal.charAt(i));
			}
		}
		return sb.toString();
	}
}
