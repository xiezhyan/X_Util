<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.1.xsd  
    http://code.alibabatech.com/schema/dubbo  
    http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <dubbo:application name="" />

    <dubbo:registry address="${r"${zookeeper.address}"}" />

    <dubbo:protocol name="dubbo" port="${r"${zookeeper.port}"}" />


    <#list tables as table>
    <dubbo:reference interface="${servicePackage}.${table.javaName?cap_first}Service"  version="1.0.0" timeout=""
                     id="${table.javaName}Service" />
    </#list>
</beans>