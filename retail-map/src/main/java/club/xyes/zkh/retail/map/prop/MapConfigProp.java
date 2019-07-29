package club.xyes.zkh.retail.map.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Create by 郭文梁 2019/7/17 9:53
 * MapConfigProp
 * 地图服务相关配置
 *
 * @author 郭文梁
 * @data 2019/7/17 9:53
 */
@Data
@Component
@ConfigurationProperties(prefix = "map")
public class MapConfigProp {
    /**
     * 签名密钥
     */
    private String key = "7846f3a42e13f711aaf8cac519adf7bf";
}
