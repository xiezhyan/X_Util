package com.sanq.product.generate;


import com.sanq.product.config.utils.web.LogUtil;
import com.sanq.product.generate.entity.Fields;
import com.sanq.product.generate.entity.Tables;
import com.sanq.product.generate.mappers.FieldMapper;
import com.sanq.product.generate.mappers.TableMapper;
import com.sanq.product.generate.utils.DaoSupport;
import com.sanq.product.generate.utils.FreemarkerUtil;
import com.sanq.product.generate.utils.StringUtil;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static DaoSupport mDaoInstance = DaoSupport.getInstance();
	private static FreemarkerUtil mFreemarker = FreemarkerUtil.getInstance();
	private static String mTemplatePath;
	private static Map<String,Object> mRoot;
	
	public static List<Tables> getTables() {
		
		
		String tableSchema = mDaoInstance.getPropVal("table_schema");
		
		SqlSession session = mDaoInstance.getSession();
		
		/**获取所有的表*/
    	TableMapper tm = session.getMapper(TableMapper.class);
    	List<Tables> tables = tm.findAllTables();

    	if(tables != null && tables.size() > 0) {
    		/**获取表中所有的字段*/
    		FieldMapper fm = session.getMapper(FieldMapper.class);
    		Map<String,String> map = null;
    		for(Tables table : tables) {
    			map = new HashMap<String,String>();
    			map.put("tableSchema", tableSchema);
    			map.put("tableName", table.getName());
    			
    			List<Fields> fields = fm.findFieldByTable(map);
    			table.setFields(fields);
    		}
    	}
    	return tables;
	}
	
    public static void main( String[] args )
    {
    	List<Tables> tables = getTables();
    	
    	//生成代码的包结构
    	String packageName = mDaoInstance.getPropVal("packageName").replace(".", File.separator);

		genymationTableEntities(tables, packageName);

		genymationResources(tables, packageName);

		genymationViews(tables, packageName);

		//FileUtil.getInstance().compressExe(FreemarkerUtil.getInstance().getParentPath(packageName), FreemarkerUtil.getInstance().getParentPath(packageName) + ".zip");
		LogUtil.getInstance(App.class).i("全部完成");
    }

	private static void genymationViews(List<Tables> tables, String packageName) {

		String base = packageName.substring(packageName.lastIndexOf("\\") + 1, packageName.length()) + File.separator + "views";

		String tableJavaName = "";
		for(Tables table : tables) {

			tableJavaName = StringUtil.firstUpperCase(table.getJavaName());

			mRoot.put("table", table);

			try {
				//entity
				write("views/api/Api.ftl",base + File.separator + "api",tableJavaName + "Api.js");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void genymationResources(List<Tables> tables, String packageName) {

		String base = packageName.substring(packageName.lastIndexOf("\\") + 1, packageName.length()) + File.separator + "resources";

		String springPackage = base + File.separator + "spring";

		String mybatisPackage = base + File.separator + "mybatis";

		String threadPackage = base + File.separator + "thread";

//		String redisPackage = base + File.separator + "redis";

		String dubboPackage = base + File.separator + "dubbo";
		String configPackage = base;

		String controllerPackage = packageName + File.separator + "controller";
		String entityVoPackage = packageName + File.separator + "entity" + File.separator + "vo";
		String mapperPackage = packageName + File.separator + "mapper";
		String serviceImplPackage = packageName + File.separator + "service" + File.separator + "impl";
		String servicePackage = packageName + File.separator + "service";


		mRoot = new HashMap<String,Object>();
		mRoot.put("tables", tables);
		mRoot.put("basePackage", packageName.replace(File.separator,"."));
		mRoot.put("controllerPackage", controllerPackage.replace(File.separator,"."));
		mRoot.put("entityVoPackage", entityVoPackage.replace(File.separator,"."));
		mRoot.put("fileMapperPackage", mapperPackage.replace(File.separator,"/"));
		mRoot.put("mapperPackage", mapperPackage.replace(File.separator,"."));
		mRoot.put("servicePackage", servicePackage.replace(File.separator,"."));
		mRoot.put("serviceImplPackage", serviceImplPackage.replace(File.separator,"."));

		mTemplatePath = App.class.getClassLoader().getResource("template").getPath();

		try {
			write("resources/spring/spring-application.ftl", springPackage, "spring-application.xml");
			write("resources/spring/spring-mvc.ftl", springPackage, "spring-mvc.xml");
			write("resources/mybatis/spring-mybatis.ftl", mybatisPackage, "spring-mybatis.xml");
			write("resources/mybatis/mybatis-config.ftl", mybatisPackage, "mybatis-config.xml");
			write("resources/dubbo/dubbo-provide.ftl", dubboPackage, "dubbo-provide.xml");
			write("resources/dubbo/dubbo-consume.ftl", dubboPackage, "dubbo-consume.xml");
			write("resources/config.ftl", configPackage, "config.properties");
			write("webapp/web.ftl", configPackage, "web.xml");

			LogUtil.getInstance(App.class).i("配置文件已经生成");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	private static void genymationTableEntities(List<Tables> tables, String packageName) {

		String base = packageName.substring(packageName.lastIndexOf("\\") + 1, packageName.length());

		//不同包名的文件类型
		String controllerPackage = packageName + File.separator + "controller";
		String entityPackage = packageName + File.separator + "entity";
		String entityVoPackage = entityPackage + File.separator + "vo";
		String servicePackage = packageName + File.separator + "service";
		String serviceImplPackage = packageName + File.separator + "service" + File.separator + "impl";
		String mapperPackage = packageName + File.separator + "mapper";

		mRoot = new HashMap<String,Object>();
		mRoot.put("tables", tables);
		mRoot.put("packageName", packageName.replace(File.separator,"."));
		mRoot.put("controllerPackage", controllerPackage.replace(File.separator,"."));
		mRoot.put("entityPackage", entityPackage.replace(File.separator,"."));
		mRoot.put("entityVoPackage", entityVoPackage.replace(File.separator,"."));
		mRoot.put("servicePackage", servicePackage.replace(File.separator,"."));
		mRoot.put("serviceImplPackage", serviceImplPackage.replace(File.separator,"."));
		mRoot.put("mapperPackage", mapperPackage.replace(File.separator,"."));
		mRoot.put("nowDate", new Date());

		mTemplatePath = App.class.getClassLoader().getResource("template").getPath();
		String tableJavaName = null;

		for(Tables table : tables) {

			tableJavaName = StringUtil.firstUpperCase(table.getJavaName());

			mRoot.put("table", table);

			try {
				//entity
				write("java/entity.ftl",base + File.separator +entityPackage,tableJavaName + ".java");

				//entityVo
				write("java/entityVo.ftl",base + File.separator +entityVoPackage,tableJavaName + "Vo.java");

				//controller
				write("java/controller.ftl",base + File.separator +controllerPackage,tableJavaName + "Controller.java");

				//controller
				write("java/service.ftl",base + File.separator +servicePackage,tableJavaName + "Service.java");

				//serviceImpl
				write("java/service_impl.ftl",base + File.separator +serviceImplPackage,tableJavaName + "ServiceImpl.java");

				//mapper
				write("java/mapper.ftl",base + File.separator +mapperPackage,tableJavaName + "Mapper.java");

				//mapper.xml
				write("java/mapper_xml.ftl",base + File.separator +mapperPackage,tableJavaName + "Mapper.xml");


			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		LogUtil.getInstance(App.class).i("Java类生成完成");

	}

	private static void write(String ftlName,String packageName,String fileName) throws Exception {
    	mFreemarker.tempWriter(mTemplatePath, 
    			ftlName, packageName, fileName, mRoot);
    }
}
