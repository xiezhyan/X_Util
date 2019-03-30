package com.sanq.product.example.aspect;

//@Component
//@Aspect
public class LogsAspect {

//	private  static  final Logger logger = LoggerFactory.getLogger(LogsAspect. class);
//
//	@Resource
//	private LogsService logsService;
//
//	@Resource
//	private TokenManage tokenManage;
//
//	@Autowired(required=false)
//	HttpServletRequest request;
//
//	@Autowired
//	private ThreadPoolTaskExecutor taskExecutor;
//
//	private static final ThreadLocal<UsersVo> threadUsers = new NamedThreadLocal<UsersVo>("user");
//	private static final ThreadLocal<LogsVo> threadLogs = new NamedThreadLocal<LogsVo>("Logs");
//	private static final ThreadLocal<Date> beginTimes = new NamedThreadLocal<Date>("beginTime");
//
//	/**
//	 * Controller层切点 注解拦截
//	 */
//	@Pointcut("@annotation(com.xiezhyan.utils.annotation.LogAnnotation)")
//	public void controllerAspect(){}
//
//	/**
//	 * 方法规则拦截
//	 */
//	@Pointcut("execution(* com.seenlan.ad.controller.*.*(..))")
//	public void controllerPointerCut(){}
//
//	/**
//	 * 前置通知 用于拦截Controller层记录用户的操作的开始时间
//	 */
//	@Before("controllerAspect()")
//	public void mybefore(JoinPoint joinPoint) {
//		Date time = new Date();
//		beginTimes.set(time);
//
//		String token = WebUtil.getToken(request);
//
//		//获取用户信息
//		UsersVo users = (UsersVo) JsonUtil.json2Obj((String) tokenManage.getObject(token), UsersVo.class);
//		threadUsers.set(users);
//
//	}
//
//
//	// 异常增强
//	@AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
//	public void myafterThrowing(Exception e) {
//		LogsVo log = threadLogs.get();
//		if(log != null){
//			log.setType(2);
//			log.setException(e.getMessage());
//			taskExecutor.execute(new UpdateLogThread(log, logsService));
////			logsService.update(log, log.getLogId());
//		}
//	}
//
//	/**
//	 * 后置通知 拦截Controller记录用户的信息
//	 */
//	@After("controllerAspect()")
//	public void myafterLogger(JoinPoint joinPoint) {
//
//		long beginTime = beginTimes.get().getTime();//得到线程绑定的局部变量（开始时间）
//    	long endTime = System.currentTimeMillis(); 	//2、结束时间
//		long currentTime = (endTime - beginTime);
//
//
//		LogUtil.i("计时结束：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(endTime) +
//				"  URI: " + request.getRequestURI() +
//				"  耗时： " + currentTime +
//				"   最大内存: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 +
//				"m  已分配内存: " + Runtime.getRuntime().totalMemory() / 1024 / 1024 +
//				"m  已分配内存中的剩余空间: " + Runtime.getRuntime().freeMemory() / 1024 / 1024 +
//				"m  最大可用内存: " + (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "m");
//
//		UsersVo users = threadUsers.get();
//		if(users == null) {
//			String token = WebUtil.getToken(request);
//			users = (UsersVo) JsonUtil.json2Obj((String) tokenManage.getObject(token), UsersVo.class);
//			if(users == null)
//				return;
//		}
//
//		String remark = "";
//		try {
//			remark = getRemarkToController(joinPoint);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//
//		String requestUri=request.getRequestURI();//请求的Uri
//
//		LogsVo logs = new LogsVo();
//		logs.setType(1);
//		logs.setTitle(remark);
//		logs.setRemoteAddr(GlobalUtil.getIpAddr(request));
//		logs.setRequestUri(requestUri);
//		logs.setMethod(request.getMethod());
//		logs.setMapToParams(request.getParameterMap());
//		logs.setOperateDate(beginTimes.get());
//		logs.setTimeout(currentTime + "ms");
//		logs.setUserId(users.getId());
//
//		taskExecutor.execute(new SaveLogThread(logs, logsService));
////		logsService.save(logs);
//		threadLogs.set(logs);
//
//	}
//
//	private String getRemarkToController(JoinPoint joinPoint) {
//		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//		Method method = signature.getMethod();
//		LogAnnotation controllerLog = method
//				.getAnnotation(LogAnnotation.class);
//		String discription = controllerLog.description();
//		return discription;
//	}
//
//	class SaveLogThread implements Runnable {
//
//		private LogsVo logsVo;
//		private LogsService logsService;
//
//		public SaveLogThread(LogsVo logsVo, LogsService logsService) {
//			this.logsVo = logsVo;
//			this.logsService = logsService;
//		}
//
//		@Override
//		public void run() {
//			logsService.save(logsVo);
//		}
//
//	}
//
//	class UpdateLogThread implements Runnable {
//
//		private LogsVo logsVo;
//		private LogsService logsService;
//
//		public UpdateLogThread(LogsVo logsVo, LogsService logsService) {
//			this.logsVo = logsVo;
//			this.logsService = logsService;
//		}
//
//		@Override
//		public void run() {
//			logsService.update(logsVo, logsVo.getLogId());
//		}
//
//	}

//    /**
//     * 设置请求参数
//     *
//     * @param paramMap
//     */
//    public void setMapToParams(Map<String, String[]> paramMap) {
//        if (paramMap == null) {
//            return;
//        }
//        StringBuilder params = new StringBuilder();
//        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap)
//                .entrySet()) {
//            params.append(("".equals(params.toString()) ? "" : "&")
//                    + param.getKey() + "=");
//            String paramValue = (param.getValue() != null
//                    && param.getValue().length > 0 ? param.getValue()[0] : "");
//            params.append(StringUtil.abbr(StringUtil.endsWithIgnoreCase(
//                    param.getKey(), "password") ? "" : paramValue, 100));
//        }
//        setParams(params.toString());
//    }

}
