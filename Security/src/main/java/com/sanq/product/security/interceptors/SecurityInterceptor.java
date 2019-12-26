package com.sanq.product.security.interceptors;

import com.sanq.product.config.utils.auth.exception.NoParamsException;
import com.sanq.product.config.utils.date.LocalDateUtils;
import com.sanq.product.config.utils.string.StringUtil;
import com.sanq.product.security.enums.SecurityFieldEnum;
import com.sanq.product.security.utils.ParamUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * com.sanq.product.security.interceptors.SecurityInterceptor
 * <p>
 * 前端调用接口验证
 *
 * @author sanq.Yan
 * @date 2019/8/3
 */

public abstract class SecurityInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {

            super.preHandle(request, response, handler);

            if (objectMap == null || objectMap.isEmpty())
                throw new NoParamsException("缺少必要参数");


            Object o = objectMap.get(SecurityFieldEnum.TIMESTAMP.getName());
            if (o == null)
                throw new NoParamsException(String.format("参数%s不存在", SecurityFieldEnum.TIMESTAMP.getName()));

            Long timestamp = StringUtil.toLong(o);

            if (LocalDateUtils.nowTime().getTime() - timestamp >= 60 * 1000)
                throw new NoParamsException(String.format("参数%s已过期", SecurityFieldEnum.TIMESTAMP.getName()));

            o = objectMap.get(SecurityFieldEnum.SIGN.getName());
            if (o == null)
                throw new NoParamsException(String.format("参数%s不存在", SecurityFieldEnum.SIGN.getName()));

            String sign = (String) o;

            String paramsSign = ParamUtils.getInstance().getSign(objectMap);
            if (!sign.equals(paramsSign)) {
                throw new NoParamsException(String.format("参数%s验证不正确", SecurityFieldEnum.SIGN.getName()));
            }
            return true;
        }
        throw new Exception("访问被限制");
    }

}
