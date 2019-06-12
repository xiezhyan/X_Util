package com.sanq.product.config.utils.interceptors;

import com.sanq.product.config.utils.annotation.IgnoreSecurity;
import com.sanq.product.config.utils.auth.exception.NoParamsException;
import com.sanq.product.config.utils.date.LocalDateUtils;
import com.sanq.product.config.utils.filter.security.PostParams;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author sanq.Yan
 * @date 2019/6/11
 */
public class SecurityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod hm = (HandlerMethod)handler;
        IgnoreSecurity s = hm.getMethodAnnotation(IgnoreSecurity.class);
        if(s != null)
            return true;

        Map<String, Object> objectMap;
        if(request.getMethod().equalsIgnoreCase("get"))
            objectMap = PostParams.getInstance().getParam2Get(request);
        else
            objectMap = PostParams.getInstance().json2Map(PostParams.getInstance().get());
        

        if(objectMap != null && !objectMap.isEmpty()) {
        	Object o = objectMap.get("timestamp");
        	if(o == null)
        		throw new NoParamsException("参数timestamp有误");

            Long timestamp = (Long) o;

            if(LocalDateUtils.nowTime().getTime() - timestamp >= 30*1000)
                throw new NoParamsException("timestamp已过期");
           

            o = objectMap.get("sign");
            if(o == null)
            	throw new NoParamsException("参数sign有误");	

            String sign = (String) o;

            String paramsSign = PostParams.getInstance().getSign(objectMap);
            if(!sign.equals(paramsSign))
                throw new NoParamsException("sign验证不正确");	
           	

            return true;
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
