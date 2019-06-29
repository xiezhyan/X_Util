package com.sanq.product.config.utils.annotation;

import java.lang.annotation.*;


/**        
 * Title:自定义注解     
 * Description: 忽略Token检查
 */      
@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface IgnoreSecurity {

}
