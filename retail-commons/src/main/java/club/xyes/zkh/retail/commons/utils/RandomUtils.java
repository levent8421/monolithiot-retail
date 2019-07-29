package club.xyes.zkh.retail.commons.utils;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Create by 郭文梁 2019/4/17 0017 13:15
 * RandomUtil
 * 随机数工具类
 *
 * @author 郭文梁
 * @data 2019/4/17 0017
 */
public class RandomUtils {
    /**
     * 十进制基数
     */
    private static final int DECIMAL_RADIX = 10;
    /**
     * 时间日期格式化
     */
    private static final String DATETIME_FORMATTER = "yyyyMMddHHmmssSSS";
    public static final String NUMBERS = "0123456789";
    public static final String LOWER_ALPHAS = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_ALPHAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALL_VISIBLE_CHARS = UPPER_ALPHAS + LOWER_ALPHAS + NUMBERS;
    /**
     * 原子Long值生成器
     */
    private static final AtomicLong NEXT_LONG_GENERATOR = new AtomicLong();

    /**
     * 随机UUID字符串
     *
     * @return UUID字符串
     */
    public static String randomUUIDString() {
        return UUID.randomUUID().toString();
    }

    /**
     * 随机UUID字符串 不带"-"
     *
     * @return UUID字符串
     */
    public static String randomPrettyUUIDString() {
        return randomUUIDString().replace("-", "");
    }

    /**
     * 随机UUID 内容截取为指定长度
     *
     * @param len 长度
     * @return 字符串
     */
    public static String randomPrettyUUIDString(int len) {
        return randomPrettyUUIDString().substring(0, len);
    }

    /**
     * 当前时间日期字符串 yyyyMMddHHmmssSSS
     *
     * @return 字符串
     */
    public static String currentDateTimeString() {
        return DateTimeUtils.format(DateTimeUtils.now(), DATETIME_FORMATTER);
    }

    /**
     * 当前时间日期字符串 yyyyMMddHHmmssSSS
     *
     * @return 字符串
     */
    public static String currentDateTimeString(String prefix, String suffix) {
        return String.format("%s%s%s", prefix, currentDateTimeString(), suffix);
    }

    /**
     * 获取当前时间日期字符串 并在末尾拼接唯一约束
     *
     * @param incLen 约束长度
     * @return String
     */
    public static String currentDateTimeStringWithIncrement(int incLen) {
        final String formatter = "%0" + incLen + "d";
        String suffix = String.format(formatter, nextLong(incLen));
        return String.format("%s%s", currentDateTimeString(), suffix);
    }

    /**
     * 获取当前时间日期字符串 并在末尾拼接唯一约束
     *
     * @param incLen 约束长度
     * @return String
     */
    public static String currentDateTimeStringWithIncrement(String prefix, String suffix, int incLen) {
        String incString = nextIncString(incLen);
        return String.format("%s%s%s%s", prefix, currentDateTimeString(), incString, suffix);
    }

    /**
     * 下一个递增字符串
     *
     * @param incLen 递增长度
     * @return 字符串
     */
    private static String nextIncString(int incLen) {
        final String formatter = "%0" + incLen + "d";
        return String.format(formatter, nextLong(incLen));
    }

    /**
     * 当前毫秒数组成的字符串
     *
     * @param len 允许长度
     * @return String
     */
    public static String currentTimeMillis(int len) {
        final String str = String.valueOf(System.currentTimeMillis());
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    /**
     * 当前毫秒时间字符串 携带递增尾部
     *
     * @param len    时间字符串长度
     * @param incLen 递增字符串长度
     * @return 字符串
     */
    public static String currentTimeMillisWithIncrement(int len, int incLen) {
        final String incString = nextIncString(incLen);
        final String timeString = currentTimeMillis(len);
        return timeString + incString;
    }

    /**
     * 获取下一个递增值
     *
     * @param len 最大长度
     * @return long
     */
    public static Long nextLong(int len) {
        synchronized (NEXT_LONG_GENERATOR) {
            long num = NEXT_LONG_GENERATOR.incrementAndGet();
            double max = Math.pow(DECIMAL_RADIX, len);
            if (num >= max) {
                num = NEXT_LONG_GENERATOR.get();
                if (num >= max) {
                    NEXT_LONG_GENERATOR.set(0);
                }
                return NEXT_LONG_GENERATOR.incrementAndGet();
            } else {
                return num;
            }
        }
    }

    /**
     * 随机字符串
     *
     * @param len    长度
     * @param source 源
     * @return 字符串
     */
    public static String randomString(int len, String source) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            char c = source.charAt(random.nextInt(source.length()));
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 从A-Z a-z 0-9宠生成随机字符串
     *
     * @param len 长度
     * @return 生成结果
     */
    public static String randomString(int len) {
        return randomString(len, ALL_VISIBLE_CHARS);
    }
}
