package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.dto.DatetimeRange;
import club.xyes.zkh.retail.commons.fn.Function;
import lombok.val;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Create by 郭文梁 2019/4/17 0017 14:11
 * DateTimeUtils
 * 时间日期工具类
 *
 * @author 郭文梁
 * @data 2019/4/17 0017
 */
public class DateTimeUtils {
    /**
     * 周一到周日
     */
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;
    public static final List<Integer> DAY_OF_WEEK_LIST = Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY);

    /**
     * 一天的开始时间
     */
    public static final Date TIME_OF_DAY_START = parseTime("00:00:00");
    /**
     * 一天的结束时间
     */
    public static final Date TIME_OF_DAY_END = parseTime("23:59:59");

    /**
     * 获取日历对象
     *
     * @param date 日期
     * @return 日历对象
     */
    public static Calendar calendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 获取当前时间
     *
     * @return 时间
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 格式化日期
     *
     * @param date   日期
     * @param format 格式
     * @return 格式结果
     */
    public static String format(Date date, String format) {
        FastDateFormat dateFormat = FastDateFormat.getInstance(format);
        return dateFormat.format(date);
    }

    /**
     * 获取时间日期的星期
     * (1,2,3,4,5,6,7)
     *
     * @param date 日期
     * @return 星期几
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = calendar(date);
        int res = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (res == 0) {
            return 7;
        }
        return res;
    }

    /**
     * 清除日期数据 保留时间数据
     *
     * @param date 日期
     * @return 清理后的日期
     */
    public static Date cleanDate(Date date) {
        Calendar calendar = calendar(date);
        calendar.set(0, Calendar.JANUARY, 0);
        return calendar.getTime();
    }

    /**
     * 清除时间数据
     *
     * @param calendar 日历对象
     */
    public static void cleanTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 日期区间
     *
     * @param offsetDay 开始时间距离当前时间的差
     * @param daySize   区间大小
     * @return DatetimeRange
     */
    public static DatetimeRange range(int offsetDay, int daySize) {
        Date now = now();
        Calendar calendar = calendar(now);
        cleanTime(calendar);
        calendar.add(Calendar.DAY_OF_YEAR, offsetDay);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, daySize);
        Date end = calendar.getTime();
        return new DatetimeRange(start, end);
    }

    /**
     * 解析时间日期字符串
     *
     * @param dateString 字符串
     * @param style      格式
     * @return date
     */
    public static Date parse(String dateString, String style) {
        try {
            return FastDateFormat.getInstance(style).parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 解析日期字符串
     *
     * @param dateStr 字符串
     * @return 日期对象
     */
    public static Date parseDate(String dateStr) {
        return parse(dateStr, ApplicationConstants.DATE_FORMAT);
    }

    /**
     * 解析时间字符串
     *
     * @param timeStr 字符串
     * @return 日期对象
     */
    public static Date parseTime(String timeStr) {
        return parse(timeStr, ApplicationConstants.TIME_FORMAT);
    }

    /**
     * 判断书剑是否在区间内
     *
     * @param date  时间
     * @param start 开始时间
     * @param end   结束时间
     * @return 是否在区间内
     */
    public static boolean between(Date date, Date start, Date end) {
        if (date == null) {
            return false;
        }
        long time = date.getTime();
        if (start != null) {
            if (time < start.getTime()) {
                return false;
            }
        }
        if (end != null) {
            return time <= end.getTime();
        }
        return true;
    }

    /**
     * 比较日期
     *
     * @param aDate     日期1
     * @param otherDate 日期2
     * @return 比较结果
     */
    public static long compareDate(Date aDate, Date otherDate) {
        final Calendar c1 = calendar(aDate);
        final Calendar c2 = calendar(otherDate);
        cleanTime(c1);
        cleanTime(c2);
        return c1.getTimeInMillis() - c2.getTimeInMillis();
    }

    /**
     * 比较时间
     *
     * @param aDate     日期1
     * @param otherDate 日期2
     * @return 比较结果
     */
    public static long compareTime(Date aDate, Date otherDate) {
        final Date a = cleanDate(aDate);
        final Date b = cleanDate(otherDate);
        return a.getTime() - b.getTime();
    }

    /**
     * 遍历日期
     *
     * @param begin    开始
     * @param end      结束
     * @param step     步长
     * @param field    递增字段 年月日
     * @param callback 回调
     */
    public static void forEachDate(Date begin, Date end, int step, int field, Function<Date, Void> callback) {
        final Calendar beginCalendar = calendar(begin);
        final Calendar endCalendar = calendar(end);
        cleanTime(beginCalendar);
        cleanTime(endCalendar);
        end = endCalendar.getTime();
        while (true) {
            Date tmp = beginCalendar.getTime();
            if (compareDate(tmp, end) > 0) {
                break;
            }
            callback.exec(tmp);
            beginCalendar.add(field, step);
        }
    }

    /**
     * 获取当前月的时间范围
     *
     * @return range
     */
    public static DatetimeRange currentMonthRange() {
        val calendar = calendar(now());
        cleanTime(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        val start = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        val end = calendar.getTime();
        return new DatetimeRange(start, end);
    }
}
