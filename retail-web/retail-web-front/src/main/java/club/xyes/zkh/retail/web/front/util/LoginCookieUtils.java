package club.xyes.zkh.retail.web.front.util;

import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.web.commons.vo.UserLoginCookie;

import javax.servlet.http.HttpServletResponse;

/**
 * Create by 郭文梁 2019/6/5 0005 19:10
 * LoginCookieUtils
 * 登录Cookie相关工具类
 *
 * @author 郭文梁
 * @data 2019/6/5 0005
 */
public class LoginCookieUtils {
    /**
     * 设置登录Token Cookie
     *
     * @param user               用户
     * @param response           响应对象
     * @param accessTokenEncoder token 编码器
     */
    public static void setLoginCookie(User user, HttpServletResponse response, AccessTokenEncoder accessTokenEncoder) {
        UserLoginCookie cookie = new UserLoginCookie(user, accessTokenEncoder);
        cookie.write2Response(response);
    }
}
