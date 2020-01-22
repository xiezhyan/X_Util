package com.sanq.product.security.aspect;

import com.sanq.product.config.utils.auth.exception.BusException;
import com.sanq.product.config.utils.entity.Response;
import com.sanq.product.config.utils.entity.Codes;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

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
        return new Response().failure("传递参数转换JSON格式有误");
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
        return new Response().failure("提交方式不正确");
    }

    /**
     * 415 - Unsupported Media Type。HttpMediaTypeNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public Response handleHttpMediaTypeNotSupportedException(Exception e) {
        e.printStackTrace();
        return new Response().failure("content_type_not_supported");
    }

    /**
     * Token is invaild
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusException.class)
    public Response handleBusException(BusException e) {
        return new Response().failure(e.getMsg(), e.getCode());
    }

    /**
     * 出现异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        e.printStackTrace();
        return new Response().failure(e.getMessage());
    }

    /**
     * VO中验证出现错误
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleValidationException(MethodArgumentNotValidException e) {

        return new Response().failure(e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), Codes.NOT_VALIDATION);
    }

    /**
     * 单一参数验证
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleValidationException(ConstraintViolationException e) {

        return new Response().failure(e.getConstraintViolations().iterator().next().getMessage(), Codes.NOT_VALIDATION);
    }
}
