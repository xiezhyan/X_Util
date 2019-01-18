package com.sanq.product.config.utils.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring工具类
 */
public class SpringUtil implements ApplicationContextAware {

    /** 上下文 */
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 根据Bean ID获取Bean
     * 
     * @param beanId
     * @return
     */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanId) {
        if(applicationContext == null){
            return null;
        }
        return (T) applicationContext.getBean(beanId);
    }
    
    /** 
     * 从静态变量applicationContext中得到Bean, 自动转型为所赋值对象的类型. 
     */  
    public static <T> T getBean(Class<T> requiredType) {  
    	if(applicationContext == null){
            return null;
        }
        return applicationContext.getBean(requiredType);  
    }  
  
    /** 
     * 清除SpringContextHolder中的ApplicationContext为Null. 
     */  
    public static void clearHolder() {  
        applicationContext = null;  
    }  
}
