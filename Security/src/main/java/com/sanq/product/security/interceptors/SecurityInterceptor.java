package com.sanq.product.security.interceptors;

import com.sanq.product.config.utils.auth.exception.NoParamsException;
import com.sanq.product.config.utils.auth.exception.TokenException;
import com.sanq.product.config.utils.date.LocalDateUtils;
import com.sanq.product.config.utils.string.StringUtil;
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
 * com.sanq.product.security.interceptors.SecurityInterceptor
 *
 * @author sanq.Yan
 * @date 2019/8/3
 */

public abstract class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod) {
            //验证ip是否在黑名单中
            if(checkIp(request, GlobalUtil.getIpAddr(request))) {
                return false;
            }

            HandlerMethod hm = (HandlerMethod) handler;
            Security security = hm.getMethodAnnotation(Security.class);

            if (security != null) {
                return true;
            }

            IgnoreSecurity s = hm.getMethodAnnotation(IgnoreSecurity.class);

            Map<String, Object> objectMap;
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

                o = objectMap.get(SecurityFieldEnum.TIMESTAMP.getName());
                if (o == null)
                    throw new NoParamsException(String.format("参数%s不存在", SecurityFieldEnum.TIMESTAMP.getName()));

                Long timestamp = StringUtil.toLong(o);

                if (LocalDateUtils.nowTime().getTime() - timestamp >= 60 * 1000)
                    throw new NoParamsException(String.format("参数%s已过期", SecurityFieldEnum.TIMESTAMP.getName()));


                o = objectMap.get(SecurityFieldEnum.CLIENT.getName());
                if (o == null || SecurityFieldEnum.APP.getName().equals((String) o)) {  //这里获取CLIENT是为了防止有些项目无法进行sign验证
                    o = objectMap.get(SecurityFieldEnum.SIGN.getName());
                    if (o == null)
                        throw new NoParamsException(String.format("参数%s不存在", SecurityFieldEnum.SIGN.getName()));

                    String sign = (String) o;

                    String paramsSign = ParamUtils.getInstance().getSign(objectMap);
                    if (!sign.equals(paramsSign)) {
                        throw new NoParamsException(String.format("参数%s验证不正确", SecurityFieldEnum.SIGN.getName()));
                    }

                }
                return true;
            }
        }
        return false;
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
