<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
                        http://www.springframework.org/schema/context
                        http://www.springframework.org/schema/context/spring-context-3.1.xsd
                        http://www.springframework.org/schema/mvc
                        http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd
                        http://www.springframework.org/schema/tx
   						http://www.springframework.org/schema/tx/spring-tx-3.1.xsd
   						http://www.springframework.org/schema/aop
    					http://www.springframework.org/schema/aop/spring-aop-3.1.xsd">


    <context:component-scan base-package="${basePackage}, com.sanq.product.redis.service.impl.single">
        <!-- 扫描时跳过 @Controller 注解的JAVA类（控制器） -->
        <context:exclude-filter type="annotation"
                                expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>

    <!-- 启用 @AspectJ -->
    <aop:aspectj-autoproxy/>

    <bean id="propertyConfigurer"
          class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations">
            <list>
                <value>classpath:config.properties</value>
            </list>
        </property>
    </bean>

    <import resource="classpath:mybatis/spring-mybatis.xml"/>
    <import resource="classpath:redis/spring-redis-single.xml"/>
    <import resource="classpath:thread/spring-thread.xml" />
</beans>