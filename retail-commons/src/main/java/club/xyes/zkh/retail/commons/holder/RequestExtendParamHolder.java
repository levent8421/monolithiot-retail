package club.xyes.zkh.retail.commons.holder;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by 郭文梁 2019/4/19 0019 10:09
 * RequestExtendParamHolder
 * 请求扩展参数保持器
 *
 * @author 郭文梁
 * @data 2019/4/19 0019
 */
public class RequestExtendParamHolder {
    /**
     * 线程本地变量池
     */
    private static final ThreadLocal<Map<String, Object>> LOCAL = new ThreadLocal<>();

    /**
     * 获取全部保持参数
     *
     * @return Map参数
     */
    public static Map<String, Object> getAll() {
        return LOCAL.get();
    }

    /**
     * 获取指定名称的参数
     *
     * @param name 名称
     * @return 参数值
     */
    public static Object get(String name) {
        Map<String, Object> params = LOCAL.get();
        if (params == null) {
            return null;
        }
        return params.get(name);
    }

    /**
     * 获取指定类型的参数
     *
     * @param name 参数名称
     * @param type 参数类型
     * @param <T>  参数类型泛型
     * @return 参数值
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String name, Class<T> type) {
        Object res = get(name);
        if (res == null) {
            return null;
        }
        if (type.isInstance(res)) {
            return (T) res;
        } else {
            throw new IllegalArgumentException("Unmatched type, real type is " + res.getClass() + ", but expectant is " + type);
        }
    }

    /**
     * 设置参数到线程本地变量池
     *
     * @param name  参数名称
     * @param value 参数值
     */
    public static void set(String name, Object value) {
        if (name == null || value == null) {
            return;
        }
        Map<String, Object> params = LOCAL.get();
        if (params == null) {
            params = new HashMap<>(16);
            LOCAL.set(params);
        }
        params.put(name, value);
    }

    /**
     * 清除当前线程对应的参数
     */
    public static void clear() {
        LOCAL.remove();
    }
}
