package com.sanq.product.generate.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.BeanUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

public class DaoSupport {

	private static final String RESOURCE = "mybatis.xml";
	
	private DaoSupport() {}
	
	private static DaoSupport instance;
	
	public static DaoSupport getInstance() {
		if(null == instance) {
			synchronized (DaoSupport.class) {
				if(null == instance) 
					instance = new DaoSupport();
			}
		}
		return instance;
	}
	
	public SqlSession getSession(){
        SqlSession session = null;
        try{
        	SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder()	//
        			.build(Resources.getResourceAsReader(RESOURCE));
        	session = sessionFactory.openSession();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return session;
    }
	
	
	public String getPropVal(String key) {
		String value = "";
		Properties prop = new Properties();
		BufferedInputStream bufis = null;
		try {
			bufis = new BufferedInputStream(new FileInputStream(this.getClass().getClassLoader()	//
						.getResource("config.properties").getPath()));
			prop.load(bufis);
			value = prop.getProperty(key);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
