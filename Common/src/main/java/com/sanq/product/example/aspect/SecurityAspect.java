package com.sanq.product.example.aspect;

/**
 * Title:安全检查切面(是否登录检查) 
 * Description: 通过验证Token维持登录状态
 * 
 */
//@Component
//@Aspect
public class SecurityAspect {

//	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
//	public Object execute(ProceedingJoinPoint pjp) throws Throwable {
//		// 从切点上获取目标方法
//		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
//		Method method = methodSignature.getMethod();
//		// 若目标方法忽略了安全性检查,则直接调用目标方法
//		if (method.isAnnotationPresent(IgnoreSecurity.class)) {
//			return pjp.proceed();
//		}
//		String token = WebUtil.getToken(getRequest());
//		if (!tokenManage.checkToken(token)) {
//			String message = String.format("token [%s] is invalid", token);
//			throw new TokenException(message);
//		}
//
//		return pjp.proceed();
//	}
//
//	public static HttpServletRequest getRequest() {
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
//				.getRequestAttributes()).getRequest();
//		return request;
//	}
}
