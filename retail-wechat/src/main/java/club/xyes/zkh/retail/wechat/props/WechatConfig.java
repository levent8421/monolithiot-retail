package club.xyes.zkh.retail.wechat.props;

import club.xyes.zkh.retail.wechat.utils.WxSignUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Create by 郭文梁 2019/5/18 0018 15:35
 * WechatConfig
 * 微信相关配置参数
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WechatConfig {
    /**
     * 微信公众号AppId
     */
    private String appId;
    /**
     * 微信公众号Secret
     */
    private String secret;
    /**
     * 商户号
     */
    private String mchId;
    /**
     * 微信支付相关签名方式
     */
    private String paySignType = WxSignUtil.SIGN_TYPE_MD5;
    /**
     * 本机IP
     */
    private String ip;
    /**
     * 支付通知地址
     */
    private String notifyUrl;
    /**
     * 退款通知地址
     */
    private String refundNotifyUrl;
    /**
     * 交易类型
     */
    private String tradeType = "JSAPI";
    /**
     * 签名秘钥
     */
    private String signKey;
    /**
     * 证书文件地址
     */
    private String keyFile;
    /**
     * 证书密码
     */
    private String keyPassword;
}
