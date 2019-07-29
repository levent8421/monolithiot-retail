package club.xyes.zkh.retail.commons.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Create by 郭文梁 2019/5/31 0031 14:38
 * CookieUtil
 * Cookie相关工具类
 *
 * @author 郭文梁
 * @data 2019/5/31 0031
 */
public class CookieUtil {
    /**
     * 从请求的所有Cookie寻找一个Cookie
     *
     * @param request    请求对象
     * @param cookieName Cookie名称
     * @return Cookie对象
     */
    public static Optional<Cookie> findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(c -> Objects.equals(c.getName(), cookieName)).reduce((v, ignore) -> v);
    }
}
