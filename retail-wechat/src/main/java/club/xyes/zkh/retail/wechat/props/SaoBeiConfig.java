package club.xyes.zkh.retail.wechat.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Create by 郭文梁 2019/5/28 0028 10:31
 * SaoBeiConfig
 * 扫呗配置参数
 *
 * @author 郭文梁
 * @data 2019/5/28 0028
 */
@Component
@ConfigurationProperties(prefix = "saobei")
@Data
public class SaoBeiConfig {
    /**
     * 微信支付类型常量
     */
    public static final String PAY_TYPE_WECHAT = "010";
    /**
     * api基础路径
     */
    private String baseUrl;
    /**
     * 公众号APPID
     */
    private String appId;
    /**
     * 接口版本
     */
    private String payVer = "100";
    /**
     * 接口类型
     */
    private String serviceId = "012";
    /**
     * 商户号
     */
    private String merchantNo;
    /**
     * 终端号
     */
    private String terminalId;
    /**
     * 支付结果通知地址
     */
    private String notifyUrl;
    /**
     * 接口令牌
     */
    private String token;
}
