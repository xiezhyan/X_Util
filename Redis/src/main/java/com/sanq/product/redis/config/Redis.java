package com.sanq.product.redis.config;

import static com.sanq.product.redis.config.Redis.RedisKey.MEMBER_TOKEN;
import static com.sanq.product.redis.config.Redis.RedisKey.SMS_CODE_KEY;

/**
 * com.sanq.product.redis.service.Redis
 *
 * @author sanq.Yan
 * @date 2019/12/16
 */
public class Redis {

    public static class RedisKey {
        /**
         * 用户登录
         */
        public static final String MEMBER_TOKEN = "USER:TOKEN:{TOKEN}";

        /**
         * 验证码
         */
        public static final String SMS_CODE_KEY = "SMS:CODE:{TEL}";

        /***
         *  url 拦截
         */
        public static final String CHECK_IP_KEY = "check:incr:{ip}:{url}";

        /**
         * ip黑名单
         */
        public static final String BLOCK_IP_SET = "BLOCK_IP_SET";

        /***
         *  菜单权限
         */
        public static final String MEMBER_TREE_PERMISSION = "permission:tree:{memberId}";

        /**
         * 按钮权限
         */
        public static final String MEMBER_BUTTON_PERMISSION = "permission:button:{memberId}";
    }

    public static class ReplaceKey {
        /**
         * 得到替换后的key
         *
         * @param token token
         * @return String
         */
        public static final String getTokenKey(String token) {
            return MEMBER_TOKEN.replace("{TOKEN}", token);
        }

        /**
         * 手机验证码
         *
         * @param tel 手机号
         * @return String
         */
        public static final String getSmsCodeKey(String tel) {
            return SMS_CODE_KEY.replace("{TEL}", tel);
        }

        /**
         * 统计该ip访问url的时长
         *
         * @param ip  ip
         * @param url url
         * @return String
         */
        public static String getCheckIpKey(String ip, String url) {
            return RedisKey.CHECK_IP_KEY.replace("{ip}", ip).replace("{url}", url);
        }

        /**
         * 获取tree的权限
         *
         * @param memberId  会员ID
         * @return  String
         */
        public static String getTreePermissionKey(Integer memberId) {
            String replace = "";
            if (memberId == -1)
                replace = "*";
            else
                replace = memberId.toString();

            return RedisKey.MEMBER_TREE_PERMISSION.replace("{memberId}", replace);
        }

        /**
         * 获取tree的权限
         *
         * @param memberId  会员ID
         * @return  String
         */
        public static String getButtonPermissionKey(Integer memberId) {
            String replace = "";
            if (memberId == -1)
                replace = "*";
            else replace = memberId.toString();

            return RedisKey.MEMBER_BUTTON_PERMISSION.replace("{memberId}", replace);
        }
    }

}
