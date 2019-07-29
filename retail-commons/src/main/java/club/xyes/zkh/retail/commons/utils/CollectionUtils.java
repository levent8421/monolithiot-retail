package club.xyes.zkh.retail.commons.utils;

import java.util.stream.Stream;

/**
 * Create by 郭文梁 2019/7/1 0001 08:57
 * CollectionUtils
 * 容器相关操作工具类
 *
 * @author 郭文梁
 * @data 2019/7/1 0001
 */
public class CollectionUtils {
    /**
     * 将容器内容以delimiter分隔拼接成字符串
     *
     * @param source    容器的stream
     * @param delimiter 分隔符
     * @return 字符串
     */
    public static String join(Stream<?> source, String delimiter) {
        return source.map(Object::toString).reduce((v1, v2) -> v1 + delimiter + v2).orElse("");
    }
}
