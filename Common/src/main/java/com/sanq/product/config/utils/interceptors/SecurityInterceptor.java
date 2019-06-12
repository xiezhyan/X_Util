package com.sanq.product.config.utils.interceptors;

import com.sanq.product.config.utils.annotation.IgnoreSecurity;
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
        if(request.getMethod().equalsIgnoreCase("get")) {
            objectMap = PostParams.getInstance().getParam2Get(request);
        } else {
            objectMap = PostParams.getInstance().json2Map(PostParams.getInstance().get());
        }

        if(objectMap != null && !objectMap.isEmpty()) {
            Long timestamp = (long) objectMap.get("timestamp");
            if(timestamp == null)
                return false;

            if(LocalDateUtils.nowTime().getTime() - timestamp >= 30*1000) {
                return false;
            }

            String sign = (String) objectMap.get("sign");
            String paramsSign = PostParams.getInstance().getSign(objectMap);
            if(!sign.equals(paramsSign)) {
                return false;
            }

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
