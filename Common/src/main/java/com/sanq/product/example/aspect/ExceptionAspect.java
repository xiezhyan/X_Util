package com.sanq.product.example.aspect;

import com.sanq.product.config.utils.auth.exception.TokenException;
import com.sanq.product.config.utils.entity.Response;
import com.sanq.product.config.utils.entity.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice   // 控制器增强
@ResponseBody
public class ExceptionAspect {

	/**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
    	e.printStackTrace();
        return new Response().failure("could_not_read_json", ResultCode.MESSAGE_NOT_READ);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Response handleValidationException(MethodArgumentNotValidException e) {
    	e.printStackTrace();
        return new Response().failure("parameter_validation_exception", ResultCode.PARAM_ERROR);
    }

    /**
     * 405 - Method Not Allowed。HttpRequestMethodNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
    	e.printStackTrace();
        return new Response().failure("request_method_not_supported", ResultCode.NOT_FIND_METHOD);
    }

    /**
     * 415 - Unsupported Media Type。HttpMediaTypeNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    public Response handleHttpMediaTypeNotSupportedException(Exception e) {
    	e.printStackTrace();
        return new Response().failure("content_type_not_supported");
    }

    /**
     * 500 - Token is invaild
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TokenException.class)
    public Response handleTokenException(TokenException e) {
    	e.printStackTrace();
        return new Response().failure(e.getMsg(), ResultCode.NO_TOKEN);
    }
    
    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
    	e.printStackTrace();
        return new Response().failure("Internal Server Error");
    }
}
