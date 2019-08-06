package club.xyes.zkh.retail.wechat.utils;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import lombok.val;

import java.net.URLEncoder;

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
        try {
            val host = URLEncoder.encode(ApplicationConstants.BASE_URL, ApplicationConstants.DEFAULT_CHARSET);
            return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx62f498b5f0e523fb&redirect_uri="
                    + host
                    + "%2fservice%2fapi%2fopen%2fcommodity%2f"
                    + commodityId
                    + "%2fdetail-page&response_type=code&scope=snsapi_userinfo&state="
                    + promoCode
                    + "#wechat_redirect";
        } catch (Exception e) {
            throw new InternalServerErrorException("Encode host " + ApplicationConstants.BASE_URL + " Error!");
        }
    }
}
