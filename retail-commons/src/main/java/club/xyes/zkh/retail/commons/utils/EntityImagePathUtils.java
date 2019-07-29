package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.fn.GetterFunction;
import club.xyes.zkh.retail.commons.fn.SetterFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by 郭文梁 2019/4/24 0024 17:59
 * EntityImagePathUtils
 * 实体中一些URL路径处理的相关工具类
 *
 * @author 郭文梁
 * @data 2019/4/24 0024
 */
public class EntityImagePathUtils {
    /**
     * 静态路径正则
     */
    private static final Pattern STATIC_PATTERN = Pattern.compile("^(http|https)://.*$");

    /**
     * 处理路径
     *
     * @param staticPath 静态资源服务器前缀
     * @param filePath   文件路径
     * @param getter     文件名Getter
     * @param setter     文件名Setter
     */
    public static void pretty(String staticPath,
                              String filePath,
                              GetterFunction<String> getter,
                              SetterFunction<String> setter) {

        String filename = getter.apply();
        if (filename == null) {
            return;
        }
        if (isStatic(filename)) {
            return;
        }
        String url = staticPath + filePath + filename;
        setter.apply(url);
    }

    /**
     * 判断路径是否是静态路径
     *
     * @param path 路径
     * @return 是否为静态
     */
    private static boolean isStatic(String path) {
        Matcher matcher = STATIC_PATTERN.matcher(path);
        return matcher.find();
    }
}
