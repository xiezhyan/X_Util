package com.sanq.product.generate.utils;

import com.sanq.product.config.utils.web.LogUtil;
import freemarker.template.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

public class FreemarkerUtil {

	private FreemarkerUtil() {}
	
	private static FreemarkerUtil instance;
	
	public static FreemarkerUtil getInstance() {
		if(instance == null) {
			synchronized (FreemarkerUtil.class) {
				if(instance == null) 
					instance = new FreemarkerUtil();
			}
		}
		
		return instance;
	}
	
	public Configuration getConfig(String ftlPath) throws Exception {
		Configuration cfg = new Configuration();
		 // 从哪里加载模板文件  
        cfg.setDirectoryForTemplateLoading(new File(ftlPath));  
          
        // 定义模版的位置，从类路径中，相对于FreemarkerManager所在的路径加载模版  
        // cfg.setTemplateLoader(new ClassTemplateLoader(FreemarkerManager.class, "templates"))  

        // 设置对象包装器  
        cfg.setObjectWrapper(new DefaultObjectWrapper());  

        // 设置异常处理器  
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);  

        return cfg;
	}
	
	/**
	 * 
	 *	version:将数据写到ftl文件中并生成文件
	 *	@param ftlName	要加载的ftl模板
	 *	@param path	文件生成的路径
	 *	@param root	要加载的数据
	 *	@param ftlPath	ftl模板所在的位置
	 *	@return
	 *	@throws Exception
	 *----------------------
	 * 	author:xiezhyan
	 *	date:2017-6-5
	 */
	public String tempWriter(String ftlPath , String ftlName , String path , String fileName , Map<String,Object> root) throws Exception {
		 // 通过freemarker解释模板，首先需要获得Template对象  
        Template template = getConfig(ftlPath).getTemplate(ftlName);  
        // 定义模板解释完成之后的输出 
        // com/xiezhyan/entity/Member.java
        
        File file = new File(path);
        if(!file.exists())
        	file.mkdirs();
        
        String fileStr = path + File.separator + fileName;
        
        PrintWriter out = new PrintWriter(new BufferedWriter(  
                new FileWriter(fileStr)));
        
        try {  
            // 解释模板  
            template.process(root, out); 
            LogUtil.getInstance(FreemarkerUtil.class).i("生成：" + fileStr);
        } catch (TemplateException e) {  
            e.printStackTrace();
        }
        
		return getParentPath(path);
	}
	
	public String getParentPath(String path) {
		
		return path.substring(0, path.indexOf(File.separator));
	}
}
