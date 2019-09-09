<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context 
        http://www.springframework.org/schema/context/spring-context-4.3.xsd">

	<context:component-scan base-package="${controllerPackage},
			com.sanq.product.security.aspect,
			com.sanq.product.redis.service.impl.single" />

    <context:property-placeholder location="classpath:config.properties"/>

    <!--引入通用mvc配置-->
    <import resource="classpath:spring/base-mvc.xml"/>


</beans>