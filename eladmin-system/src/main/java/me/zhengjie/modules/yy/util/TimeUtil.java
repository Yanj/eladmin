package me.zhengjie.modules.yy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yanjun
 * @date 2021-01-11 14:43
 */
public class TimeUtil {

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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(cal.getTime());
    }

    /**
     * 获取日期的星期文本
     *
     * @param date .
     * @return .
     */
    public static String getWeekDayText(String date) {
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d = format.parse(date);
            cal.setTime(d);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式错误, 格式: yyyy-MM-dd");
        }
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return "星期日";
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
        }
        return null;
    }

}
