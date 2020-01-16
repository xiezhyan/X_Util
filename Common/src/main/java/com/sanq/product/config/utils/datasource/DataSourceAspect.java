package com.sanq.product.config.utils.datasource;

import com.sanq.product.config.utils.annotation.DataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * com.sanq.product.config.utils.datasource.DataSourceAspect
 *
 * @author sanq.Yan
 * @date 2020/1/16
 */
public class DataSourceAspect {

    public void before(JoinPoint point) {

        Object target = point.getTarget();
        String method = point.getSignature().getName();

        Class<?>[] classz = target.getClass().getInterfaces();

        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
                .getMethod().getParameterTypes();

        try {
            Method m = classz[0].getMethod(method, parameterTypes);

            if (m != null && m.isAnnotationPresent(DataSource.class)) {

                DataSource data = m
                        .getAnnotation(DataSource.class);

                DynamicDataSourceHolder.putDataSource(data.db());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void after(JoinPoint point) {
        DynamicDataSourceHolder.clearDataSource();
    }
}
