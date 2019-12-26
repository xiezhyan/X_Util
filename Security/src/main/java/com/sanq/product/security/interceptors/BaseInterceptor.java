package com.sanq.product.security.interceptors;

import com.sanq.product.config.utils.auth.exception.IpAllowedException;
import com.sanq.product.config.utils.auth.exception.NoParamsException;
import com.sanq.product.config.utils.auth.exception.TokenException;
import com.sanq.product.config.utils.web.GlobalUtil;
import com.sanq.product.security.annotation.IgnoreSecurity;
import com.sanq.product.security.annotation.Security;
import com.sanq.product.security.enums.SecurityFieldEnum;
import com.sanq.product.security.utils.ParamUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * com.sanq.product.security.interceptors.BaseInterceptor
 *
 * @author sanq.Yan
 * @date 2019/8/8
 */
public abstract class BaseInterceptor  implements HandlerInterceptor {

    protected  Map<String, Object> objectMap;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod) {
            //验证ip是否在黑名单中
            String ip = GlobalUtil.getIpAddr(request);
            if(checkIp(request, ip)) {
                throw new IpAllowedException(String.format("ip：%s 被禁止访问", ip));
            }

            HandlerMethod hm = (HandlerMethod) handler;
            Security security = hm.getMethodAnnotation(Security.class);

            if (security != null) {
                return true;
            }

            IgnoreSecurity s = hm.getMethodAnnotation(IgnoreSecurity.class);

            if (request.getMethod().equalsIgnoreCase("get"))
                objectMap = ParamUtils.getInstance().getParam2Get(request);
            else
                objectMap = ParamUtils.getInstance().json2Map(ParamUtils.getInstance().get());


            if (objectMap != null && !objectMap.isEmpty()) {
                Object o = null;

                if (s == null) {
                    o = objectMap.get(SecurityFieldEnum.TOKEN.getName());
                    if (o == null)
                        throw new NoParamsException(String.format("参数%s不存在", SecurityFieldEnum.TOKEN.getName()));

                    if (!checkToken(request, (String) o)) {
                        throw new TokenException(String.format("%s已过期，请重新登录", SecurityFieldEnum.TOKEN.getName()));
                    }
                }
                return true;
            }
            throw new NoParamsException("缺少必要验证参数");
        }
        throw new Exception("Api 异常");
    }

    public abstract boolean checkToken(HttpServletRequest request, String token);
    public abstract boolean checkIp(HttpServletRequest request, String ip);


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
