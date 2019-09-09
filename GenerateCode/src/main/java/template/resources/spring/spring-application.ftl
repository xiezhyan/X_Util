<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
                        http://www.springframework.org/schema/context
                        http://www.springframework.org/schema/context/spring-context-4.3.xsd
   						http://www.springframework.org/schema/aop
    					http://www.springframework.org/schema/aop/spring-aop-4.3.xsd">


    <context:component-scan base-package="${basePackage},
            com.sanq.product.redis.service.impl.single" />

    <!-- 启用 @AspectJ -->
    <aop:aspectj-autoproxy/>

    <context:property-placeholder
            location="classpath:config.properties"/>


    <import resource="classpath:mybatis/spring-mybatis.xml"/>
    <import resource="classpath:redis/spring-redis-single.xml"/>
    <import resource="classpath:thread/spring-thread.xml" />
</beans>