package com.sanq.product.config.utils.interceptors;

import com.sanq.product.config.utils.annotation.IgnoreSecurity;
import com.sanq.product.config.utils.annotation.Security;
import com.sanq.product.config.utils.auth.exception.NoParamsException;
import com.sanq.product.config.utils.auth.exception.TokenException;
import com.sanq.product.config.utils.date.LocalDateUtils;
import com.sanq.product.config.utils.filter.security.PostParams;
import com.sanq.product.config.utils.string.StringUtil;
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
public abstract class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            Security security = hm.getMethodAnnotation(Security.class);

            if (security != null) {
                return true;
            }

            IgnoreSecurity s = hm.getMethodAnnotation(IgnoreSecurity.class);

            Map<String, Object> objectMap;
            if (request.getMethod().equalsIgnoreCase("get"))
                objectMap = PostParams.getInstance().getParam2Get(request);
            else
                objectMap = PostParams.getInstance().json2Map(PostParams.getInstance().get());


            if (objectMap != null && !objectMap.isEmpty()) {
                Object o = null;

                if (s == null) {
                    o = objectMap.get(SecurityEnum.TOKEN.getName());
                    if (o == null)
                        throw new NoParamsException("参数" + SecurityEnum.TOKEN.getName() + "不存在");

                    if (!validateToken(request, (String) o)) {
                        throw new TokenException(SecurityEnum.TOKEN.getName() + "已过期，请重新登录");
                    }
                }

                o = objectMap.get(SecurityEnum.TIMESTAMP.getName());
                if (o == null)
                    throw new NoParamsException("参数" + SecurityEnum.TIMESTAMP.getName() + "不存在");

                Long timestamp = StringUtil.toLong(o);

                if (LocalDateUtils.nowTime().getTime() - timestamp >= 300 * 1000)
                    throw new NoParamsException(SecurityEnum.TIMESTAMP.getName() + "已过期");

                o = objectMap.get(SecurityEnum.CLIENT.getName());    // H5,APP
                if (o == null || SecurityEnum.APP.getName().equals((String) o)) {
                    o = objectMap.get(SecurityEnum.SIGN.getName());
                    if (o == null)
                        throw new NoParamsException("参数" + SecurityEnum.SIGN.getName() + "不存在");

                    String sign = (String) o;

                    String paramsSign = PostParams.getInstance().getSign(objectMap);
                    if (!sign.equals(paramsSign))
                        throw new NoParamsException(SecurityEnum.SIGN.getName() + "验证不正确");
                }
                return true;
            }
        }
        return true;
    }

    public abstract boolean validateToken(HttpServletRequest request, String token);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
