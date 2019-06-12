package com.sanq.product.config.utils.filter.security;

import com.alibaba.fastjson.JSON;
import com.sanq.product.config.utils.string.DigestUtil;
import com.sanq.product.config.utils.string.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * com.pymob.product.books.filters.TokenBag
 *
 * 对参数进行验签
 * @author sanq.Yan
 * @date 2019/6/11
 */
public class PostParams {
    private static final WeakHashMap<String, String> PARAMS = new WeakHashMap<>();
    public static final String KEY = "post_params";
    private PostParams() {
    }

    public static PostParams getInstance() {
        return HOLDER.INSTANCE;
    }

    static class HOLDER {
        private static final PostParams INSTANCE = new PostParams();
    }

    /**
     * 将从post中获取到的json 存放到map中
     * @param params
     */
    public void set(String params) {
        PARAMS.put(KEY, params);
    }

    /**
     * 得到json字符串
     * @return
     */
    public String get() {
        return PARAMS.get(KEY);
    }

    /**
     * 将json字符串转换成map对象
     * @param json
     * @return
     */
    public Map<String, Object> json2Map(String json) {
        return (Map<String, Object>) JSON.parse(json);
    }

    /**
     * 签名验证
     * @param map
     * @return
     */
    public String getSign(Map<String, Object> map) {
        return getSign2Map(map);
    }

    private String getSign2Map(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        ArrayList<String> list = new ArrayList<String>(map.keySet());
        Collections.sort(list);

        for(String key:list) {
            Object value = map.get(key);
            if(!key.equalsIgnoreCase("sign") && value != null && !StringUtil.isEmpty(value.toString()))
                sb.append(key).append("=").append(map.get(key)).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return DigestUtil.getInstance().md5(sb.toString());
    }

    public String getSign(String json){
        Map<String, Object> map = json2Map(json);
        return getSign2Map(map);
    }

    /**
     * 从get请求中获取到参数 封装成Map对象
     * @param request
     * @return
     */
    public Map<String, Object> getParam2Get(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();

        if(parameterMap != null && !parameterMap.isEmpty()) {
            for(Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String[] value = entry.getValue();
                if(value != null && value.length > 0) {
                    map.put(entry.getKey(), value[0]);
                }
            }
        }

        return map;
    }
}
