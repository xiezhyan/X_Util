package com.sanq.product.config.utils.date;

import com.sanq.product.config.utils.string.StringUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil{

    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME = "HH:mm:ss";
    public static final String DATE = "yyyy-MM-dd";


    private DateUtil() {
    }

    /**
     * date 转 字符串
     */
    public static String date2Str(Date date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if (date != null)
            return formatter.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));

        return "";
    }

    /**
     * version: String转换成Date
     */
    public static Date str2Date(String dateStr, String format) {
        if (StringUtil.isEmpty(dateStr))
            return LocalDateUtils.localDateTimeToDate(LocalDateTime.now());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateUtils.localDateTimeToDate(LocalDateTime.from(LocalDateTime.parse(dateStr, formatter)));
    }


    /**
     * 24小时 单位 ：s
     */
    public static int to24() {
        LocalTime time = LocalTime.now().withNano(0);
        LocalTime time1 = LocalTime.of(23, 59, 59);
        return time1.toSecondOfDay() - time.toSecondOfDay();
    }
}
