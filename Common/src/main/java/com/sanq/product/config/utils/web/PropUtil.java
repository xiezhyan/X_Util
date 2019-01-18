package com.sanq.product.config.utils.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Xiezhyan on 2018/9/25.
 */
public class PropUtil {

    private static Properties properties;

    static {
        loadProp();
    }

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

    public static String getProperty(String key){
        if(null == properties) {
            loadProp();
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == properties) {
            loadProp();
        }
        return properties.getProperty(key, defaultValue);
    }

}
