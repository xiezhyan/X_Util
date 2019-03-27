package com.sanq.product.config.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private DateUtil() {
    }

    /**
     * version: Date转换成String
     *
     * @param date
     * @param format
     * @return
     * @author:xiezhyan
     * @date:2017-10-14
     */
    public static String date2Str(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null)
            return sdf.format(date);
        return "";
    }

    /**
     * version: String转换成Date
     *
     * @param dateVal
     * @param format
     * @return
     * @author:xiezhyan
     * @date:2017-10-14
     */
    public static Date str2Date(String dateVal, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateVal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * version: 得到年
     *
     * @param date
     * @return
     * @author:xiezhyan
     * @date:2017-10-14
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * version: 得到月份
     *
     * @param date
     * @return
     * @author:xiezhyan
     * @date:2017-10-14
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * version: 得到日期
     *
     * @param date
     * @return
     * @author:xiezhyan
     * @date:2017-10-14
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 24小时 单位 ：s
     *
     * @return
     */
    public static int to24() {
        LocalTime time = LocalTime.now().withNano(0);
        LocalTime time1 = LocalTime.of(23, 59, 59);
        return time1.toSecondOfDay() - time.toSecondOfDay();
    }

    /**
     * 得到当前时间的星期
     *
     * @return
     */
    public static int getWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0) week = 0;
        return week;
    }

    /**
     * 当前时
     *
     * @return
     */
    public static int getHour() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

}
