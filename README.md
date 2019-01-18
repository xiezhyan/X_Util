# X_Util

#### 介绍
Java常用工具类

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

