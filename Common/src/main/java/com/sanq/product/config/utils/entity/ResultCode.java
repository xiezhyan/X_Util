package com.sanq.product.config.utils.entity;

public class ResultCode {

    public static final Integer RESULT_OK = 200;    //成功
    public static final Integer RESULT_ERROR = 500;    //失败

    public static final Integer NO_TOKEN = 400;        //没有token
    public static final Integer NO_ACCESS = 401;    //没有授权
    public static final Integer PARAM_ERROR = 402;    //参数有误
    public static final Integer MESSAGE_NOT_READ = 403;    //不能读取该json
    public static final Integer NOT_FIND_METHOD = 405;    //没有发现该方法
    public static final Integer NOT_VALIDATION = 406;   //参数校验失败

}
