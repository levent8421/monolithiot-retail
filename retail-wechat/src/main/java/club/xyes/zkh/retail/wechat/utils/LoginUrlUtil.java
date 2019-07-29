package club.xyes.zkh.retail.wechat.utils;

/**
 * Create by 郭文梁 2019/6/5 0005 17:29
 * LoginUrlUtil
 * 微信登录URL相关工具类
 *
 * @author 郭文梁
 * @data 2019/6/5 0005
 */
public class LoginUrlUtil {
    /**
     * 获取分享二维码路径
     *
     * @param commodityId 商品ID
     * @param promoCode   推广码
     * @return 二维码内容（分享链接）
     */
    public static String getShareLoginUrl(Integer commodityId, String promoCode) {
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx45725d38c171c33f&redirect_uri=http%3a%2f%2fwz.jinguanjiazhifu.com%2fretail%2fservice%2fapi%2fopen%2fcommodity%2f"
                + commodityId
                + "%2fdetail-page&response_type=code&scope=snsapi_userinfo&state="
                + promoCode
                + "#wechat_redirect";
    }
}
