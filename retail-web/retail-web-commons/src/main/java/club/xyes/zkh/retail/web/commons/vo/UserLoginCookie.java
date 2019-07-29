package club.xyes.zkh.retail.web.commons.vo;

import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.utils.CookieUtil;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by 郭文梁 2019/5/23 0023 09:35
 * UserLoginCookie
 * 用户登录Cookie对象
 *
 * @author 郭文梁
 * @data 2019/5/23 0023
 */
@Slf4j
public class UserLoginCookie extends Cookie {
    /**
     * Cookie名称
     */
    private static final String COOKIE_NAME = "User-Access-Token";
    /**
     * 前缀
     */
    private static final String TOKEN_PREFIX = "user/";

    /**
     * 从请求中提取UserLoginCookie
     *
     * @param request 请求对象
     * @param encoder 编解码器
     * @return Cookie对象 包含用户基本信息
     */
    public static UserLoginCookie readFromRequest(HttpServletRequest request, AccessTokenEncoder encoder) {
        return CookieUtil
                .findCookie(request, COOKIE_NAME)
                .map(value -> readFromCookie(value, encoder))
                .orElse(null);
    }

    /**
     * 从Cookie中
     *
     * @param cookie  Cookie
     * @param encoder 编解码器
     * @return Cookie
     */
    public static UserLoginCookie readFromCookie(Cookie cookie, AccessTokenEncoder encoder) {
        String cookieValue = cookie.getValue();
        String token = cookieValue.substring(TOKEN_PREFIX.length());
        try {
            UserInfo userInfo = encoder.decode(token, UserInfo.class);
            return new UserLoginCookie(userInfo);
        } catch (Exception e) {
            log.debug("Could not unpack user token:[{}]", token);
            return null;
        }
    }

    /**
     * 基本用户信息
     */
    @Data
    public static class UserInfo {
        /**
         * 从用户对象创建
         *
         * @param user 用户
         * @return 用户基本信息
         */
        static UserInfo fromUser(User user) {
            UserInfo res = new UserInfo();
            res.setUserId(user.getId());
            return res;
        }

        /**
         * 从JSON字符串创建
         *
         * @param json json
         * @return UserInfo
         */
        static UserInfo fromJSON(String json) {
            return JSON.parseObject(json, UserInfo.class);
        }

        /**
         * 用户ID
         */
        private Integer userId;

        /**
         * 转换为JSON字符串
         *
         * @return json字符串
         */
        String toJSON() {
            return JSON.toJSONString(this);
        }
    }

    /**
     * 从JSON对象创建
     *
     * @param json JSON
     * @return cookie
     */
    public UserLoginCookie fromJSON(String json) {
        UserInfo userInfo = UserInfo.fromJSON(json);
        return new UserLoginCookie(userInfo);
    }

    /**
     * 从Token创建对象
     *
     * @param token   token
     * @param encoder 编码器
     * @return cookie
     */
    public UserLoginCookie fromToken(String token, AccessTokenEncoder encoder) {
        UserInfo userInfo = encoder.decode(token, UserInfo.class);
        return new UserLoginCookie(userInfo);
    }

    /**
     * 用户基本信息
     */
    @Getter
    private UserInfo userInfo;

    /**
     * 构造器
     *
     * @param user    用户
     * @param encoder 编码器
     */
    public UserLoginCookie(User user, AccessTokenEncoder encoder) {
        this(UserInfo.fromUser(user), encoder);
    }

    /**
     * 构造器
     *
     * @param user 用户
     */
    public UserLoginCookie(User user) {
        this(UserInfo.fromUser(user));
    }

    /**
     * 构造器
     *
     * @param userInfo 用户基本信息
     * @param encoder  编码器
     */
    public UserLoginCookie(UserInfo userInfo, AccessTokenEncoder encoder) {
        super(COOKIE_NAME, encoder.encode(userInfo.toJSON(), TOKEN_PREFIX));
        this.userInfo = userInfo;
        setHttpOnly(false);
        setPath("/");
    }

    /**
     * 构造器
     *
     * @param userInfo 用户基本信息
     */
    public UserLoginCookie(UserInfo userInfo) {
        super(COOKIE_NAME, userInfo.toJSON());
        this.userInfo = userInfo;
        setHttpOnly(false);
        setPath("/");
    }

    /**
     * 转换为JSON字符串
     *
     * @return JSON字符串
     */
    public String toJSON() {
        return userInfo.toJSON();
    }

    /**
     * 将Cookie写入到响应中
     *
     * @param response 响应对象
     */
    public void write2Response(HttpServletResponse response) {
        log.debug("Write user login cookie to response:[{}]=[{}]", getName(), getValue());
        response.addCookie(this);
    }

    @Override
    public String toString() {
        return "UserLoginCookie(userId=" + (userInfo == null ? "null" : userInfo.getUserId()) + ")";
    }
}
