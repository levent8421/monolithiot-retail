package club.xyes.zkh.retail.wechat.config;

import club.xyes.zkh.retail.wechat.props.YunPianConfig;
import com.yunpian.sdk.YunpianClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Create by 郭文梁 2019/6/15 0015 13:11
 * SmsConfiguration
 * 短信配置类
 *
 * @author 郭文梁
 * @data 2019/6/15 0015
 */
@SpringBootConfiguration
public class SmsConfiguration {
    private final YunPianConfig yunPianConfig;

    public SmsConfiguration(YunPianConfig yunPianConfig) {
        this.yunPianConfig = yunPianConfig;
    }

    /**
     * 构建云片客户端
     *
     * @return 云片客户端
     */
    @Bean
    public YunpianClient yunpianClient() {
        final String apiKey = yunPianConfig.getApiKey();
        YunpianClient client = new YunpianClient(apiKey);
        return client.init();
    }
}
