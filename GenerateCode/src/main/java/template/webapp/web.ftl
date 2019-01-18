<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <display-name></display-name>
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
        <welcome-file>index.htm</welcome-file>
        <welcome-file>index.jsp</welcome-file>
        <welcome-file>default.html</welcome-file>
        <welcome-file>default.htm</welcome-file>
        <welcome-file>default.jsp</welcome-file>
    </welcome-file-list>

    <!-- ajax 跨域 -->
    <filter>
        <filter-name>corsFilter</filter-name>
        <filter-class>com.sanq.product.config.utils.filter.CorsFilter</filter-class>
        <init-param>
            <param-name>allowOrigin</param-name>
            <param-value>http://localhost:63343,http://localhost:8080</param-value>
        </init-param>
        <init-param>
            <param-name>allowMethods</param-name>
            <param-value>GET,POST,PUT,DELETE,OPTIONS</param-value>
        </init-param>
        <init-param>
            <param-name>allowCredentials</param-name>
            <param-value>true</param-value>
        </init-param>
        <init-param>
            <param-name>allowHeaders</param-name>
            <param-value>Content-Type,Token</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>corsFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!-- xss -->
    <filter>
        <filter-name>defendXssFilter</filter-name>
        <filter-class>com.sanq.product.config.utils.filter.XssFilter</filter-class>
        <init-param>
            <param-name>excludedPaths</param-name>
            <param-value>
                *.js,*.css,/css/*,/image/*,/js/*,/lib/*
            </param-value>
        </init-param>
    </filter>

    <filter-mapping>
        <filter-name>defendXssFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>



    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring/spring-application.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <async-supported>true</async-supported>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <servlet>
        <servlet-name>SpringMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring/spring-mvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
        <async-supported>true</async-supported>
    </servlet>
    <servlet-mapping>
        <servlet-name>SpringMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

</web-app>
