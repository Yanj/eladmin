package me.zhengjie.modules.yy.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author yanjun
 * @date 2021-01-11 14:43
 */
public class TimeUtil {

    /**
     * 获取日期范围
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<String> getDateRange(String beginDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate beginDateTime = LocalDate.parse(beginDate, formatter);
        LocalDate endDateTime = LocalDate.parse(endDate, formatter);
        List<String> list = new ArrayList<>();
        while(true) {
            if (beginDateTime.compareTo(endDateTime) > 0) {
                break;
            }
            list.add(beginDateTime.format(formatter));
            beginDateTime = beginDateTime.plus(1, ChronoUnit.DAYS);
        }
        return list;
    }

    /**
     * 获取从周一到周日的日期
     *
     * @param offset 周的偏移
     * @return
     */
    public static List<String> getWeekDateRange(int offset) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        localDate = localDate.minus(dayOfWeek.ordinal(), ChronoUnit.DAYS);
        localDate = localDate.plus(offset * 7, ChronoUnit.DAYS);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(localDate.format(formatter));
            localDate = localDate.plus(1, ChronoUnit.DAYS);
        }
        return list;
    }

    /**
     * 获取当前日期
     *
     * @return .
     */
    public static String getCurrentDate() {
        return getCurrentDate(0);
    }

    /**
     * 获取当前日期
     *
     * @param offset .
     * @return .
     */
    public static String getCurrentDate(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, offset);

        LocalDate localDate = LocalDate.now();
        localDate = localDate.plus(offset, ChronoUnit.DAYS);

        return DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .format(localDate);
    }

    /**
     * 获取日期的星期文本
     *
     * @param date .
     * @return .
     */
    public static String getWeekDayText(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return "星期日";
        } else if (dayOfWeek == DayOfWeek.MONDAY) {
            return "星期一";
        } else if (dayOfWeek == DayOfWeek.TUESDAY) {
            return "星期二";
        } else if (dayOfWeek == DayOfWeek.WEDNESDAY) {
            return "星期三";
        } else if (dayOfWeek == DayOfWeek.THURSDAY) {
            return "星期四";
        } else if (dayOfWeek == DayOfWeek.FRIDAY) {
            return "星期五";
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            return "星期六";
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getCurrentDate());
        System.out.println(getCurrentDate(-1));
        System.out.println(getCurrentDate(-2));
        System.out.println(getCurrentDate(1));
        System.out.println(getWeekDayText("2020-01-01"));
        System.out.println(getDateRange("2020-01-01", "2020-01-10"));
        System.out.println(getWeekDateRange(0));
        System.out.println(getWeekDateRange(1));
        System.out.println(getWeekDateRange(-2));
    }

}
