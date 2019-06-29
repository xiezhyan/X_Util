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

	<!--数据源-->
	<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
		destroy-method="close">
		<property name="url" value="${r"${jdbc.url}"}" />
		<property name="username" value="${r"${jdbc.username}"}" />
		<property name="password" value="${r"${jdbc.password}"}" />
		<property name="initialSize" value="1"></property>
		<property name="maxActive" value="100"></property>
		<property name="minIdle" value="1"></property>
		<!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
		<property name="maxWait" value="60000"></property>
		<!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
		<property name="timeBetweenEvictionRunsMillis" value="60000" />
		<!-- 配置一个连接在池中最小生存的时间，单位是毫秒 --> 
		<property name="minEvictableIdleTimeMillis" value="300000" />
		<!-- 这里建议配置为TRUE，防止取到的连接不可用 --> 
	    <property name="testOnBorrow" value="true" /> 
	    <property name="testOnReturn" value="false" />
	    <!-- 打开PSCache，并且指定每个连接上PSCache的大小 --> 
		<property name="poolPreparedStatements" value="true" /> 
		<property name="maxPoolPreparedStatementPerConnectionSize" value="20" />
	</bean>

    <!-- 2、配置sqlSessionFactory对象-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--注入数据库连接池-->
        <property name="dataSource" ref="dataSource"/>
        <!-- 配置mybatis全局配置文件：mybatis-config.xml-->
        <property name="configLocation" value="classpath:mybatis/mybatis-config.xml"/>
        <!-- 扫描entity包，使用别名，多个用;隔开-->
        <property name="typeAliasesPackage" value="${entityVoPackage}"/>
        <!-- 扫描sql配置文件:mapper需要的xml文件-->
        <property name="mapperLocations" value="classpath:${fileMapperPackage}/*.xml"/>
    </bean>


    <!-- 3.配置扫描Dao接口包，动态实现Dao接口，注入到spring容器-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!-- 注入sqlSessionFactory-->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!--给出需要扫描的Dao接口-->
        <property name="basePackage" value="${mapperPackage}"/>
    </bean>

	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>
	<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<tx:attributes>
			<tx:method name="save*" propagation="REQUIRED" />
			<tx:method name="add*" propagation="REQUIRED" />
			<tx:method name="delete*" propagation="REQUIRED" />
			<tx:method name="update*" propagation="REQUIRED" />
			<tx:method name="find*" propagation="SUPPORTS" read-only="true" />
			<tx:method name="get*" propagation="SUPPORTS" read-only="true" />
		</tx:attributes>
	</tx:advice>
	<!-- 切面 -->
	<aop:config>
		<aop:advisor advice-ref="txAdvice"
			pointcut="execution(* ${serviceImplPackage}.*.*(..))" />
	</aop:config>
</beans>