package com.sanq.product.config.utils.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {


    private DateUtil() {
    }

    /**
     * date 转 字符串
     */
    public static String date2Str(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if (date != null)
            return formatter.format(date);
        return "";
    }

    /**
     * version: String转换成Date
     */
    public static LocalDate str2Date(String dateStr, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.from(LocalDateTime.parse(dateStr, formatter));
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
