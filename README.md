# X_Util

#### 使用
> 添加maven仓库
```xml
<profile>
    <id>jitpack.io</id>
    <activation>
        <activeByDefault>false</activeByDefault>
        <jdk>1.8</jdk>
    </activation>
    <repositories>
        <!-- jitpack.io地址-->
        <repository>
        <id>jitpack.io</id>
        <url>https://www.jitpack.io</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
        </repository>
    </repositories>   
</profile>

<activeProfiles>
    <activeProfile>jitpack.io</activeProfile>
</activeProfiles>

```
> 添加pom

```xml
<parent>
    <groupId>com.github.xiezhyan.X_Util</groupId>
    <artifactId>x_utils</artifactId>
    <version>找到最新的Latest release对应的版本号</version>
</parent>

<dependency>
    <groupId>com.github.xiezhyan</groupId>
    <artifactId>X_Util</artifactId>
    <version>找到最新的Latest release对应的版本号</version>
</dependency>
```

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
4. Security
> 接口安全限制
5. Search
> 集成ElasticSearch常用方法

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
    1. 过滤resources文件， 配置jetty(maven中暂不支持tomcat8, 可使用外部引入的tomcat)
    ```
    <build>
        <finalName>${project.artifactId}</finalName>
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
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>

        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-maven-plugin</artifactId>
                <version>9.4.24.v20191120</version>
                <configuration>
                    <scanIntervalSeconds>10</scanIntervalSeconds>
                    <webApp>
                        <contextPath>/${project.artifactId}</contextPath>
                    </webApp>
                    <httpConnector>
                        <port>8080</port>
                    </httpConnector>
                </configuration>
            </plugin>
            <!--通过profile打包-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <configuration>
                    <warName>${project.artifactId}</warName>
                    <webResources>
                        <resource>
                            <filtering>true</filtering>
                            <directory>src/main/webapp</directory>
                            <includes>
                                <include>**/web.xml</include>
                            </includes>
                        </resource>
                    </webResources>
                    <warSourceDirectory>src/main/webapp</warSourceDirectory>
                    <webXml>src/main/webapp/WEB-INF/web.xml</webXml>
                </configuration>
            </plugin>
        </plugins>
    </build>
    ```
    2. 基于server打包
    ```
    <!--动态配置文件-->
    <profiles>
        <profile>
            <id>dev</id>
            <properties>
                <env>dev</env>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        <profile>
            <id>prod</id>
            <properties>
                <env>prod</env>
            </properties>
        </profile>
    </profiles>
    
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
                <directory>src/main/resources/spring/profile</directory>
                <filtering>true</filtering>
                <includes>
                    <include>spring-${env}-application.xml</include>
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
    <filter-class>com.sanq.product.security.filters.CorsFilter</filter-class>
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

### 验证签名安全

1. maven 配置
```
<dependency>
    <groupId>com.sanq.product.x_utils</groupId>
    <artifactId>util_security</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2. web.xml 配置
```
<filter>
    <filter-name>securityFilter</filter-name>
    <filter-class>com.sanq.product.security.filters.SecurityFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>securityFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

#### 如何使用

**BaseInterceptor**

在该类中包含以下判断：

1. 非正常访问验证
2. 验证参数中是否存在**token**参数, 并且判断**token**是否过期

**CheckHasPermissionInterceptor**

该类是**BaseInterceptor**的子类, 包含以下判断

1. 判断当前用户是否有当前接口的权限

**SecurityInterceptor**

该类是**BaseInterceptor**的子类, 该类在使用的时候会进行一系列验证, 包括:

1. token
2. timestamp
3. sign


### 系统中参数验证

采用 Hibernate Validator 的方式进行验证

```
<context:component-scan base-package="com.sanq.product.security.aspect"/>
```

