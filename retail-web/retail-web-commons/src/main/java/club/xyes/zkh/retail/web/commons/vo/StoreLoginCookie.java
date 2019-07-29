package club.xyes.zkh.retail.web.commons.vo;

import club.xyes.zkh.retail.commons.entity.Store;
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

/**
 * Create by 郭文梁 2019/5/31 0031 14:17
 * StoreLoginCookie
 * 商铺登录Cookie
 *
 * @author 郭文梁
 * @data 2019/5/31 0031
 */
@Slf4j
public class StoreLoginCookie extends Cookie {
    /**
     * Cookie名称
     */
    private static final String COOKIE_NAME = "Store-Access-Token";
    /**
     * Token前缀
     */
    private static final String TOKEN_PREFIX = "store/";

    /**
     * 从请求中提取StoreLoginCookie
     *
     * @param request 请求对象
     * @param encoder 编解码器
     * @return Cookie对象 包含商铺基本信息
     */
    public static StoreLoginCookie readFromRequest(HttpServletRequest request, AccessTokenEncoder encoder) {
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
    public static StoreLoginCookie readFromCookie(Cookie cookie, AccessTokenEncoder encoder) {
        String cookieValue = cookie.getValue();
        String token = cookieValue.substring(TOKEN_PREFIX.length());
        try {
            StoreInfo storeInfo = encoder.decode(token, StoreInfo.class);
            return new StoreLoginCookie(storeInfo);
        } catch (Exception e) {
            log.debug("Could not unpack store token:[{}]", token);
            return null;
        }
    }

    /**
     * 商铺基本信息
     */
    @Data
    public static class StoreInfo {
        /**
         * 从Store对象创建StoreInfo
         *
         * @param store  Store对象
         * @param openId 本次登录的OpenId
         * @return StoreInfo
         */
        public static StoreInfo fromStore(Store store, String openId) {
            StoreInfo res = new StoreInfo();
            res.setStoreId(store.getId());
            res.setOpenId(openId);
            return res;
        }

        /**
         * s商铺ID
         */
        private Integer storeId;
        /**
         * 微信OpenId
         */
        private String openId;

        /**
         * 转换为JSON字符串
         *
         * @return json
         */
        public String toJSON() {
            return JSON.toJSONString(this);
        }

        /**
         * 从JSON反序列化StoreInfop
         *
         * @param json json
         * @return StoreInfo
         */
        public StoreInfo fromJSON(String json) {
            return JSON.parseObject(json, StoreInfo.class);
        }
    }

    @Getter
    private StoreInfo storeInfo;

    /**
     * 从Store对象创建Cookie
     *
     * @param store   Store
     * @param openId  本次登录的openId
     * @param encoder 令牌编码器
     */
    public StoreLoginCookie(Store store, String openId, AccessTokenEncoder encoder) {
        this(StoreInfo.fromStore(store, openId), encoder);
    }

    /**
     * 从StoreInfo创建Cookie
     *
     * @param storeInfo StoreInfo
     * @param encoder   Token编码器
     */
    public StoreLoginCookie(StoreInfo storeInfo, AccessTokenEncoder encoder) {
        super(COOKIE_NAME, encoder.encode(storeInfo.toJSON(), TOKEN_PREFIX));
        this.storeInfo = storeInfo;
        setHttpOnly(false);
        setPath("/");
    }

    /**
     * 从商铺基本信息创建商铺登录Cookie， 不进行编解码操作
     *
     * @param storeInfo 商铺基本信息
     */
    public StoreLoginCookie(StoreInfo storeInfo) {
        super(COOKIE_NAME, storeInfo.toJSON());
        this.storeInfo = storeInfo;
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
        return "Store(" + (getStoreInfo() == null ? "null" : getStoreInfo().getStoreId()) + ")";
    }
}
