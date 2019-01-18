package com.sanq.product.config.utils.ip;

/**
 * Created by XieZhyan on 2018/8/6.
 */
public class IPEntry {

    public String beginIp;
    public String endIp;
    public String country;
    public String area;

    /**
     * 构造函数
     */
    public IPEntry() {
        beginIp = endIp = country = area = "";
    }
}
