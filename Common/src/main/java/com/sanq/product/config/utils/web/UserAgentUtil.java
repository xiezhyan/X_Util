package com.sanq.product.config.utils.web;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;

import java.io.IOException;

/**
 * Created by Xiezhyan on 2018/8/30.
 */
public class UserAgentUtil {

    public static final ThreadLocal<UASparser> LOCAL = new ThreadLocal<UASparser>() {
        @Override
        protected UASparser initialValue() {
            try {
                return new UASparser(OnlineUpdater.getVendoredInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    public static String getOs (String useragent) throws IOException {
        UASparser sparser = LOCAL.get();
        if (sparser == null)
            return "";

        return LOCAL.get().parse(useragent).getOsFamily().toLowerCase();
    }
}
