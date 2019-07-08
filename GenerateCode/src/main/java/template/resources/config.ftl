# mysql
jdbc.url=jdbc:mysql://ip/database?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
jdbc.username=root
jdbc.password=


# redis
redis.ip=ip
redis.port=6379


# zookeeper
zookeeper.address=ip:2181
zookeeper.port=20880

# kafka
bootstrap.servers=域名:9092

# kafka topic


# oss
oss.AccessKeyId=
oss.AccessKeySecret=
oss.endpoint=
oss.show=
oss.bucketName=


#freemarker

#设置标签类型：square_bracket:[]     auto_detect:[]<>
tag_syntax=auto_detect
#模版缓存时间，单位：秒
template_update_delay=0
default_encoding=UTF-8
output_encoding=UTF-8
locale=zh_CN
#设置数字格式 ，防止出现 000.00
number_format=\#
#变量为空时，不会报错
classic_compatible=true
date_format=yyyy-MM-dd
time_format=HH\:mm\:ss
datetime_format=yyyy-MM-dd HH\:mm\:ss
#这个表示每个freemarker的视图页面都会自动引入这个ftl文件。里面定议的就是一些宏，如text文本框，各种form元素
#auto_import="/WEB-INF/templates/index.ftl" as do