package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.fn.GetterFunction;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * Create by 郭文梁 2019/4/20 0020 15:30
 * TextUtils
 * 文本相关工具类
 *
 * @author 郭文梁
 * @data 2019/4/20 0020
 */
@Slf4j
public class TextUtils {
    /**
     * 去除两边空格后字符串是否为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isTrimedEmpty(String str) {
        return str == null || "".equals(str) || "".equals(str.trim());
    }

    /**
     * 去除两边空格后字符串是否不为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isTrimedNotEmpty(String str) {
        return !(str == null || "".equals(str) || "".equals(str.trim()));
    }

    /**
     * 字符串非空检查
     *
     * @param getter Getter
     * @param errMsg 异常信息
     */
    public static void notEmpty(GetterFunction<String> getter, String errMsg) {
        if (isTrimedEmpty(getter.apply())) {
            throw new BadRequestException(errMsg);
        }
    }

    /**
     * 将ISO8859-1的字符串转换为UTF-8字符串
     *
     * @param str 被转换字符串
     * @return 转换结果
     */
    public static String iso2Utf8(String str) {
        if (str == null) {
            return null;
        }
        String result = new String(str.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        log.debug("Convert result = [{}]", result);
        return result;
    }

    /**
     * 将字符串转换为UTF-8字符串
     *
     * @param str 被转换字符串
     * @return 转换结果
     */
    public static String toUtf8(String str) {
        if (str == null) {
            return null;
        }
        return new String(str.getBytes(), StandardCharsets.UTF_8);
    }
}
