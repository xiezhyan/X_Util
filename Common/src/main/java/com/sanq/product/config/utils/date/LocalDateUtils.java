package com.sanq.product.config.utils.date;

import com.sanq.product.config.utils.string.StringUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * Created by Xiezhyan on 2018/9/11.
 */
public class LocalDateUtils {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String nowTimeStr() {
        return formatter.format(LocalDateTime.now().withNano(0));
    }

    public static Date nowTime() {
        return localDateTimeToDate(LocalDateTime.now());
    }

    public static String dayStartTimeStr(int days) {
        return formatter.format(LocalDate.now().minusDays(days).atTime(0, 0, 0).withNano(0));
    }

    public static Date dayStartTime(int days) {
        return localDateTimeToDate(LocalDate.now().minusDays(days).atTime(0, 0, 0).withNano(0));
    }

    public static String dayEndTimeStr(int days) {
        return formatter.format(LocalDate.now().minusDays(days).atTime(23, 59, 59).withNano(0));
    }

    public static Date dayEndTime(int days) {
        return localDateTimeToDate(LocalDate.now().minusDays(days).atTime(23, 59, 59).withNano(0));
    }

    public static String dayStartTimeStr() {
        return formatter.format(LocalDate.now().atTime(0, 0, 0).withNano(0));
    }

    public static Date dayStartTime() {
        return localDateTimeToDate(LocalDate.now().atTime(0, 0, 0).withNano(0));
    }

    public static String dayEndTimeStr() {
        return formatter.format(LocalDate.now().atTime(23, 59, 59).withNano(0));
    }

    public static Date dayEndTime() {
        return localDateTimeToDate(LocalDate.now().atTime(23, 59, 59).withNano(0));
    }

    public static String lastDayStartTimeStr() {
        return formatter.format(LocalDate.now().atTime(0, 0, 0).minusDays(1L).withNano(0));
    }

    public static Date lastDayStartTime() {
        return localDateTimeToDate(LocalDate.now().atTime(0, 0, 0).minusDays(1L).withNano(0));
    }

    public static String lastDayEndTimeStr() {
        return formatter.format(LocalDate.now().atTime(23, 59, 59).minusDays(1L).withNano(0));
    }

    public static Date lastDayEndTime() {
        return localDateTimeToDate(LocalDate.now().atTime(23, 59, 59).minusDays(1L).withNano(0));
    }

    public static String weekStartTimeStr() {
        LocalDate date = LocalDate.now();
        int week = date.getDayOfWeek().getValue();
        return formatter.format(date.atTime(0, 0, 0).minusDays(week - 1).withNano(0));
    }

    public static Date weekStartTime() {
        LocalDate date = LocalDate.now();
        int week = date.getDayOfWeek().getValue();
        return localDateTimeToDate(date.atTime(0, 0, 0).minusDays(week - 1).withNano(0));
    }

    public static String weekEndTimeStr() {
        LocalDate date = LocalDate.now();
        int week = date.getDayOfWeek().getValue();
        return formatter.format(date.atTime(23, 59, 59).plusDays(7 - week).withNano(0));
    }

    public static Date weekEndTime() {
        LocalDate date = LocalDate.now();
        int week = date.getDayOfWeek().getValue();
        return localDateTimeToDate(date.atTime(23, 59, 59).plusDays(7 - week).withNano(0));
    }

    public static String lastWeekStartTimeStr() {
        LocalDate date = LocalDate.now();
        int week = date.getDayOfWeek().getValue();
        return formatter.format(date.atTime(0, 0, 0).minusWeeks(1L).minusDays(week - 1).withNano(0));
    }

    public static Date lastWeekStartTime() {
        LocalDate date = LocalDate.now();
        int week = date.getDayOfWeek().getValue();
        return localDateTimeToDate(date.atTime(0, 0, 0).minusWeeks(1L).minusDays(week - 1).withNano(0));
    }

    public static String lastWeekEndTimeStr() {
        LocalDate date = LocalDate.now();
        int week = date.getDayOfWeek().getValue();
        return formatter.format(date.atTime(23, 59, 59).minusWeeks(1L).plusDays(7 - week).withNano(0));
    }

    public static Date lastWeekEndTime() {
        LocalDate date = LocalDate.now();
        int week = date.getDayOfWeek().getValue();
        return localDateTimeToDate(date.atTime(23, 59, 59).minusWeeks(1L).plusDays(7 - week).withNano(0));
    }

    public static String monthStartTimeStr() {
        return formatter.format(LocalDate.now().atTime(0, 0, 0).with(TemporalAdjusters.firstDayOfMonth()).withNano(0));
    }

    public static Date monthStartTime() {
        return localDateTimeToDate(LocalDate.now().atTime(0, 0, 0).with(TemporalAdjusters.firstDayOfMonth()).withNano(0));
    }

    public static String monthEndTimeStr() {
        return formatter.format(LocalDate.now().atTime(23, 59, 59).with(TemporalAdjusters.lastDayOfMonth()).withNano(0));
    }

    public static Date monthEndTime() {
        return localDateTimeToDate(LocalDate.now().atTime(23, 59, 59).with(TemporalAdjusters.lastDayOfMonth()).withNano(0));
    }

    public static String lastMonthStartTimeStr() {
        return formatter.format(LocalDate.now().atTime(0, 0, 0).minusMonths(1L).with(TemporalAdjusters.firstDayOfMonth()).withNano(0));
    }

    public static Date lastMonthStartTime() {
        return localDateTimeToDate(LocalDate.now().atTime(0, 0, 0).minusMonths(1L).with(TemporalAdjusters.firstDayOfMonth()).withNano(0));
    }

    public static String lastMonthEndTimeStr() {
        return formatter.format(LocalDate.now().atTime(23, 59, 59).minusMonths(1L).with(TemporalAdjusters.lastDayOfMonth()).withNano(0));
    }

    public static Date lastMonthEndTime() {
        return localDateTimeToDate(LocalDate.now().atTime(23, 59, 59).minusMonths(1L).with(TemporalAdjusters.lastDayOfMonth()).withNano(0));
    }

    public static Date localDateToDate(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = date.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static Date localDateTimeToDate(LocalDateTime date) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    public static int get2ZeroHour() {
        long nowTime = nowTime().getTime();
        long endTime = dayEndTime().getTime();
        long currentTime = endTime - nowTime;
        return StringUtil.toInteger(currentTime / 1000);
    }

    public static void main(String[] args) {
        System.out.println("获取当前时间:" + nowTimeStr());
        System.out.println("获取当前时间:" + nowTime().getTime());
        System.out.println("当天开始时间:" + dayStartTimeStr());
        System.out.println("当天开始时间:" + dayStartTime());
        System.out.println("当天结束时间:" + dayEndTimeStr());
        System.out.println("当天结束时间:" + dayEndTime());
        System.out.println("昨天开始时间:" + lastDayStartTimeStr());
        System.out.println("昨天开始时间" + lastDayStartTime());
        System.out.println("昨天结束时间" + lastDayEndTimeStr());
        System.out.println("昨天结束时间" + lastDayEndTime());
        System.out.println("本周开始时间" + weekStartTimeStr());
        System.out.println("本周开始时间" + weekStartTime());
        System.out.println("本周结束时间" + weekEndTimeStr());
        System.out.println("本周结束时间" + weekEndTime());
        System.out.println("上周开始时间" + lastWeekStartTimeStr());
        System.out.println("上周开始时间" + lastWeekStartTime());
        System.out.println("上周结束时间" + lastWeekEndTimeStr());
        System.out.println("上周结束时间" + lastWeekEndTime());
        System.out.println("本月开始时间" + monthStartTimeStr());
        System.out.println("本月开始时间" + monthStartTime());
        System.out.println("本月结束时间" + monthEndTimeStr());
        System.out.println("本月结束时间" + monthEndTime());
        System.out.println("上月开始时间" + lastMonthStartTimeStr());
        System.out.println("上月开始时间" + lastMonthStartTime());
        System.out.println("上月结束时间" + lastMonthEndTimeStr());
        System.out.println("上月结束时间" + lastMonthEndTime());
    }
}
