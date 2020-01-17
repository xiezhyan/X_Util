package com.sanq.product.config.utils.db;

/**
 * com.sanq.product.config.utils.db.DynamicDataSourceHolder
 *
 * @author sanq.Yan
 * @date 2020/1/16
 */
public class DynamicDataSourceHolder {
    public static final ThreadLocal<String> holder = new ThreadLocal<String>();

    public static void putDataSource(String name) {
        holder.set(name);
    }

    public static String getDataSouce() {
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }
}
