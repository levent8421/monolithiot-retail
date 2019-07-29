package club.xyes.zkh.retail.wechat.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Create by 郭文梁 2019/6/15 0015 13:09
 * YunPianConfig
 * 云片短信相关配置
 *
 * @author 郭文梁
 * @data 2019/6/15 0015
 */
@Component
@ConfigurationProperties(prefix = "sms")
@Data
public class YunPianConfig {
    /**
     * ApiKey
     * 39c8673e0f6be3d248c6443205684a54
     */
    private String apiKey;
}
