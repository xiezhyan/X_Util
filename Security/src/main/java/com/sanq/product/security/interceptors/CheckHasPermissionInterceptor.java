package com.sanq.product.security.interceptors;

import com.sanq.product.config.utils.auth.exception.AuthException;
import com.sanq.product.config.utils.auth.exception.TokenException;
import com.sanq.product.security.annotation.Security;
import com.sanq.product.security.enums.SecurityFieldEnum;
import org.apache.zookeeper.KeeperException;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * com.sanq.product.security.interceptors.CheckHasPermissionInterceptor
 *
 * 后台权限验证等
 * @author sanq.Yan
 * @date 2019/8/19
 */
public abstract class CheckHasPermissionInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {

            super.preHandle(request, response, handler);

//            HandlerMethod hm = (HandlerMethod) handler;
//            Security security = hm.getMethodAnnotation(Security.class);
//
//            if (security != null) {
//                return true;
//            }

            String uri = request.getRequestURI().replace(request.getContextPath(), "");

            if (!checkHasThisUrl(request, uri, (String) objectMap.get(SecurityFieldEnum.TOKEN.getName()))) {
                throw new AuthException("无当前接口权限");
            }
            return true;
        }
        throw new Exception("访问被限制");
    }


    protected abstract boolean checkHasThisUrl(HttpServletRequest request, String uri, String token);
}
