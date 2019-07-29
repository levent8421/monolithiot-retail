package club.xyes.zkh.retail.commons.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Create by 郭文梁 2019/5/27 0027 15:11
 * ParamChecker
 * 参数检查相关工具
 *
 * @author 郭文梁
 * @data 2019/5/27 0027
 */
public class ParamChecker {
    /**
     * 检查参数是否为空 为空则抛出异常
     *
     * @param o              参数
     * @param exceptionClass 异常类
     * @param msg            附加信息
     */
    public static void notNull(Object o, Class<? extends RuntimeException> exceptionClass, String msg) {
        if (o == null) {
            throwException(exceptionClass, msg);
        }
    }

    /**
     * 检查参数是否为空 为空则抛出异常
     *
     * @param str            字符串
     * @param exceptionClass 异常类
     * @param msg            异常信息
     */
    public static void notEmpty(String str, Class<? extends RuntimeException> exceptionClass, String msg) {
        if (TextUtils.isTrimedEmpty(str)) {
            throwException(exceptionClass, msg);
        }
    }

    /**
     * 检查容器不为空
     *
     * @param collection     容器
     * @param exceptionClass 异常类
     * @param msg            异常信息
     */
    public static void notEmpty(Collection<?> collection, Class<? extends RuntimeException> exceptionClass, String msg) {
        notNull(collection, exceptionClass, msg);
        if (collection.size() <= 0) {
            throwException(exceptionClass, msg);
        }
    }

    /**
     * 抛出异常
     *
     * @param exceptionClass 异常类
     * @param msg            异常信息
     */
    private static void throwException(Class<? extends RuntimeException> exceptionClass, String msg) {
        try {
            Constructor<? extends RuntimeException> constructor = exceptionClass.getConstructor(String.class);
            throw constructor.newInstance(msg);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
