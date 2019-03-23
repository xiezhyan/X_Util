<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc" 
	xmlns:aop="http://www.springframework.org/schema/aop"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
		http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context 
        http://www.springframework.org/schema/context/spring-context-3.1.xsd
        http://www.springframework.org/schema/mvc 
        http://www.springframework.org/schema/mvc/spring-mvc-3.1.xsd
		http://www.springframework.org/schema/aop
		http://www.springframework.org/schema/aop/spring-aop-3.1.xsd">

	<context:component-scan base-package="${controllerPackage}" />

	<!-- 启用 @AspectJ	-->
	<aop:aspectj-autoproxy />

	<mvc:annotation-driven>
		<mvc:message-converters register-defaults="true">
			<bean
				class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
				<property name="objectMapper">
					<bean class="com.fasterxml.jackson.databind.ObjectMapper">
						<property name="serializationInclusion">
							<value type="com.fasterxml.jackson.annotation.JsonInclude.Include">NON_NULL</value>
						</property>
					</bean>
				</property>
			</bean>
		</mvc:message-converters>
	</mvc:annotation-driven>

	<mvc:annotation-driven />
    <mvc:resources location="/static/" mapping="/static/**"/>

	<bean id="mappingJackson2HttpMessageConverter"
		class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
		<property name="supportedMediaTypes">
			<list>
				<value>text/html;charset=UTF-8</value>
			</list>
		</property>
	</bean>

	<bean
		class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter">
		<property name="messageConverters">
			<list>
				<ref bean="mappingJackson2HttpMessageConverter" />
			</list>
		</property>
	</bean>

	<bean id="multipartResolver"
		class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<property name="defaultEncoding" value="utf-8" />
		<property name="maxUploadSize" value="10485760000" />
		<property name="maxInMemorySize" value="40960" />
		<property name="resolveLazily" value="true" />
	</bean>

    <!--视图解析器-->
    <!--配置试图解析器  -->
    <bean class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver">
        <property name="order" value="1"></property>
        <property name="suffix" value=".ftl"></property>
        <property name="contentType" value="text/html;charset=utf-8"></property>
        <property name="viewClass">
            <value>org.springframework.web.servlet.view.freemarker.FreeMarkerView</value>
        </property>
        <property name="requestContextAttribute" value="request"></property>
    </bean>
    <!--加载freemarker属性  -->
    <bean id="propertySetting" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
        <property name="locations">
            <list>
                <value>classpath:config.properties</value>
            </list>
        </property>
    </bean>
    <!--freemarker配置  -->
    <bean id="freemarkerConfig" class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
        <property name="templateLoaderPath">
            <value>/WEB-INF/ftl/</value>
        </property>
        <property name="freemarkerSettings" ref="propertySetting"></property>
    </bean>

</beans>