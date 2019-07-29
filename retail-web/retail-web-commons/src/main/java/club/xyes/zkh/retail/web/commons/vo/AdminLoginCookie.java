package club.xyes.zkh.retail.web.commons.vo;

import club.xyes.zkh.retail.commons.entity.Admin;
import club.xyes.zkh.retail.commons.utils.CookieUtil;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class AdminLoginCookie extends Cookie {
    /**
     * Cookie名称
     */
    private static final String COOKIE_NAME = "Admin-Access-Token";
    /**
     * Token前缀
     */
    private static final String TOKEN_PREFIX = "admin/";

    /**
     * 从请求中提取StoreLoginCookie
     *
     * @param request 请求对象
     * @param encoder 编解码器
     * @return Cookie对象 包含商铺基本信息
     */
    public static AdminLoginCookie readFromRequest(HttpServletRequest request, AccessTokenEncoder encoder) {
        Optional<Cookie> cookie = CookieUtil.findCookie(request, COOKIE_NAME);
        return cookie.map(value -> readFromCookie(value, encoder)).orElse(null);
    }

    /**
     * 从Cookie中读取StoreLoginCookie信息
     *
     * @param cookie  Cookie
     * @param encoder 编解码器
     * @return Cookie
     */
    public static AdminLoginCookie readFromCookie(Cookie cookie, AccessTokenEncoder encoder) {
        String cookieValue = cookie.getValue();
        String token = cookieValue.substring(TOKEN_PREFIX.length());
        try {
            AdminInfo adminInfo = encoder.decode(token, AdminInfo.class);
            return new AdminLoginCookie(adminInfo);
        } catch (Exception e) {
            log.debug("Could not unpack store token:[{}]", token);
            return null;
        }
    }

    /**
     * 商铺基本信息
     */
    @Data
    public static class AdminInfo {
        /**
         * 从Admin对象创建AdminInfo
         *
         * @param admin Admin
         * @return AdminInfo
         */
        public static AdminInfo fromStore(Admin admin) {
            final AdminInfo res = new AdminInfo();
            res.setAdminId(admin.getId());
            return res;
        }

        /**
         * Admin Id
         */
        private Integer adminId;

        /**
         * 转换为JSON字符串
         *
         * @return json
         */
        public String toJSON() {
            return JSON.toJSONString(this);
        }

        /**
         * 从JSON反序列化AdminInfo
         *
         * @param json json
         * @return AdminInfo
         */
        public AdminInfo fromJSON(String json) {
            return JSON.parseObject(json, AdminInfo.class);
        }
    }

    @Getter
    private AdminInfo adminInfo;

    /**
     * 从Admin对象创建Cookie
     *
     * @param admin   Admin
     * @param encoder 令牌编码器
     */
    public AdminLoginCookie(Admin admin, AccessTokenEncoder encoder) {
        this(AdminInfo.fromStore(admin), encoder);
    }

    /**
     * 从AdminInfo创建Cookie
     *
     * @param adminInfo AdminInfo
     * @param encoder   Token编码器
     */
    public AdminLoginCookie(AdminInfo adminInfo, AccessTokenEncoder encoder) {
        super(COOKIE_NAME, encoder.encode(adminInfo.toJSON(), TOKEN_PREFIX));
        setHttpOnly(false);
        setPath("/");
    }

    /**
     * 从商铺基本信息创建商铺登录Cookie， 不进行编解码操作
     *
     * @param adminInfo 商铺基本信息
     */
    public AdminLoginCookie(AdminInfo adminInfo) {
        super(COOKIE_NAME, adminInfo.toJSON());
        this.adminInfo = adminInfo;
    }

    /**
     * 将Cookie写入到响应中
     *
     * @param response 响应对象
     */
    public void write2Response(HttpServletResponse response) {
        log.debug("Write store login cookie to Response:[{}]=[{}]", getName(), getValue());
        response.addCookie(this);
    }

    @Override
    public String toString() {
        return "Admin(" + (getAdminInfo() == null ? "null" : getAdminInfo().getAdminId()) + ")";
    }
}
