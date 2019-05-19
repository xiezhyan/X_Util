package com.sanq.product.config.utils.web;

import java.io.*;
import java.util.Properties;

/**
 * Created by Xiezhyan on 2018/9/25.
 */
public class PropUtil {

    private static Properties properties;

    static {
        //loadProp();
    }

    @Deprecated
    synchronized static private void loadProp() {
        properties = new Properties();
        InputStream in = null;
        try {
            in = PropUtil.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized static private void loadProp(String path) {
        properties = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(path);
            properties.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public static String getProperty(String key){
        if(null == properties) {
            loadProp();
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String path, String key){
        if(null == properties) {
            loadProp(path);
        }
        return properties.getProperty(key);
    }

	public static void saveOrUpdate(String path, String key, String value) {
        if(null == properties) {
            loadProp(path);
        }

		properties.setProperty(key, value);
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream(path);
			properties.store(oFile, "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
