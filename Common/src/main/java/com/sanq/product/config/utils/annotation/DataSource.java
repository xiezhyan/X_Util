package com.sanq.product.config.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * com.sanq.product.config.utils.annotation.DataSource
 *
 * @author sanq.Yan
 * @date 2020/1/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSource {
    String db();
}
