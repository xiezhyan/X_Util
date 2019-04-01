# X_Util

#### 介绍
Java常用工具类

#### 初始配置

```
-Dmaven.multiModuleProjectDirectory=$M2_HOME
```


#### 软件架构
1. Common
> 常用工具类封装
2. Redis
> Redis的封装， 可以在单机和集群之间随意切换
3. GenerateCode
> 自动生成代码工具， 包括entity, mapper, service, controller, resources， 通过简单修改， 生成项目

#### 安装教程


1. Common

```
<dependency>
    <groupId>com.sanq.product.x_utils</groupId>
    <artifactId>util_common</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2. Redis

```
<dependency>
    <groupId>com.sanq.product.x_utils</groupId>
    <artifactId>util_redis</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

3. GenerateCode
    1. 修改config.properties里的url， table_schema和packageName
    2. 运行App.java
    3. 在当前目录下会看到生成的代码 


4. pom 配置
    1. 过滤resources文件， 配置tomcat
    ```
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
                <include>**/*.dat</include>
            </includes>
            <filtering>false</filtering>
        </resource>
    </resources>

    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>1.7</source>
                <target>1.7</target>
                <encoding>UTF8</encoding>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <configuration>
                <url>http://localhost:8080/manager/text</url>
                <username>admin</username>
                <password>admin</password>
                <path>/ad-web</path>
                <port>9990</port>
                <update>true</update>
                <server>tomcat7</server>
            </configuration>
        </plugin>
    </plugins>
    ```
    2. 基于server打包
    ```
    <packaging>jar</packaging>
    <build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                    <include>**/*.dat</include>
                </includes>
                <filtering>false</filtering>
            </resource>

            <!-- 结合com.alibaba.dubbo.container.Main -->
            <resource>
                <targetPath>${project.build.directory}/classes/META-INF/spring</targetPath>
                <directory>src/main/resources/spring</directory>
                <filtering>true</filtering>
                <includes>
                    <include>spring-application.xml</include>
                </includes>
            </resource>
        </resources>

        <pluginManagement>
            <plugins>
                <!-- 解决Maven插件在Eclipse内执行了一系列的生命周期引起冲突 -->
                <plugin>
                    <groupId>org.eclipse.m2e</groupId>
                    <artifactId>lifecycle-mapping</artifactId>
                    <version>1.0.0</version>
                    <configuration>
                        <lifecycleMappingMetadata>
                            <pluginExecutions>
                                <pluginExecution>
                                    <pluginExecutionFilter>
                                        <groupId>org.apache.maven.plugins</groupId>
                                        <artifactId>maven-dependency-plugin</artifactId>
                                        <versionRange>[2.0,)</versionRange>
                                        <goals>
                                            <goal>copy-dependencies</goal>
                                        </goals>
                                    </pluginExecutionFilter>
                                    <action>
                                        <ignore />
                                    </action>
                                </pluginExecution>
                            </pluginExecutions>
                        </lifecycleMappingMetadata>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
        <plugins>
            <!-- 打包jar文件时，配置manifest文件，加入lib包的jar依赖 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <classesDirectory>target/classes/</classesDirectory>
                    <archive>
                        <manifest>
                            <mainClass>com.alibaba.dubbo.container.Main</mainClass>
                            <!-- 打包时 MANIFEST.MF文件不记录的时间戳版本 -->
                            <useUniqueVersions>false</useUniqueVersions>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                        </manifest>
                        <manifestEntries>
                            <Class-Path>.</Class-Path>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <type>jar</type>
                            <includeTypes>jar</includeTypes>
                            <useUniqueVersions>false</useUniqueVersions>
                            <outputDirectory>
                                ${project.build.directory}/lib
                            </outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    ```
   
### 反爬虫约定 robots.txt
> 项目跟路径（webapp）下添加robots.txt文件
```
User-agent: *
Disallow: /
```

### 针对跨域设置
```
<!-- ajax 跨域 -->
<filter>
    <filter-name>corsFilter</filter-name>
    <filter-class>com.sanq.product.config.utils.filter.CorsFilter</filter-class>
    <init-param>
        <param-name>allowOrigin</param-name>
        <param-value>地址设置</param-value>
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
        <param-value>Content-Type,Token,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>corsFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

### 针对xss设置
```
<filter>
    <filter-name>xssFilter</filter-name>
    <filter-class>com.sanq.product.config.utils.filter.XssFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>xssFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
  ```