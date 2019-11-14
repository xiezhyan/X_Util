package com.sanq.product.config.utils.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * version:
 * --------------------------
 * author:sijun
 * date:2017/7/23
 */

public class MatcherUtil {
    public static final String EMAIL_REGEX = "[\\\\w!#$%&'*+/=?^_`{|}~-]+(?:\\\\.[\\\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\\\w](?:[\\\\w-]*[\\\\w])?\\\\.)+[\\\\w](?:[\\\\w-]*[\\\\w])?";
    
    private static final String IP_NUM = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    public static final String IP_REGEX = "^" + IP_NUM + "\\." + IP_NUM + "\\." + IP_NUM + "\\." + IP_NUM + "$";
    
    public static final String URL_REGEX = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    
    public static final String PHONE_REGEX = "^(\\d{3,4}-)?\\d{6,8}$";

    public static final String PASSWORD_REGEX = "(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,}";

    public static final String POSTAL_CODE_REGEX = "^\\d{6}$";

    public static final String TEL_REGEX = "^[1](([3][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";

    public static final String NUMBER_REGEX = "^[0-9]*$";

    public static final String INT_REGEX = "^\\+?[1-9][0-9]*$";

    public static final String UP_CHAR_REGEX = "^[A-Z]+$";

    public static final String LOW_CHAR_REGEX = "^[a-z]+$";

    public static final String LETTER_REGEX = "^[A-Za-z]+$";

    public static final String CHINESE_REGEX = "^[\u4e00-\u9fa5],{0,}$";



    
    /**
     * 验证邮箱
     *
     * @return 如果是符合的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isEmail(String str) {
        String regex = EMAIL_REGEX;
        return match(regex, str);
    }

    /**
     * 验证IP地址
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIP(String str) {
        String regex = IP_REGEX;
        return match(regex, str);
    }

    /**
     * 验证网址Url
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUrl(String str) {
        String regex = URL_REGEX;
        return match(regex, str);
    }

    /**
     * 验证电话号码
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isPhone(String str) {
        String regex = PHONE_REGEX;
        return match(regex, str);
    }

    /**
     * 验证输入密码条件(字符与数据同时出现)
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isPassword(String str) {
        String regex = PASSWORD_REGEX;
        return match(regex, str);
    }

    /**
     * 验证输入邮政编号
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isPostalcode(String str) {
        String regex = POSTAL_CODE_REGEX;
        return match(regex, str);
    }

    /**
     * 验证输入手机号码
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isTelephone(String str) {
        String regex = TEL_REGEX;
        return match(regex, str);
    }

    public static void main(String[] args) {
        System.out.println(isTelephone("15110148609"));
    }

    /**
     * 替换特殊字符
     * @param str
     * @return
     */
    public static String replaceMark(String str) {
        String reg = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 验证数字输入
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isNumber(String str) {
        String regex = NUMBER_REGEX;
        return match(regex, str);
    }

    /**
     * 验证非零的正整数
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIntNumber(String str) {
        String regex = INT_REGEX;
        return match(regex, str);
    }

    /**
     * 验证大写字母
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isUpChar(String str) {
        String regex = UP_CHAR_REGEX;
        return match(regex, str);
    }

    /**
     * 验证小写字母
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isLowChar(String str) {
        String regex = LOW_CHAR_REGEX;
        return match(regex, str);
    }

    /**
     * 验证输入字母
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isLetter(String str) {
        String regex = LETTER_REGEX;
        return match(regex, str);
    }

    /**
     * 验证验证输入汉字
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isChinese(String str) {
        String regex = CHINESE_REGEX;
        return match(regex, str);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 是否包含
     *
     * @param regex
     * @param str
     * @return
     */
    public static boolean find(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 得到匹配到的内容
     *
     * @param regex
     * @param str
     * @param group
     * @return
     */
    public static String group(String regex, String str, int group) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            return matcher.group(group);
        }
        return "";
    }
}
