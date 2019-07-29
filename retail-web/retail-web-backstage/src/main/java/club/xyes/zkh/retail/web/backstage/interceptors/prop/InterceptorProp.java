package club.xyes.zkh.retail.web.backstage.interceptors.prop;

import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Create by 郭文梁 2019/6/20 0020 11:21
 * InterceptorProp
 * 拦截器配置参数
 *
 * @author 郭文梁
 * @data 2019/6/20 0020
 */
@Data
@Component
@ConfigurationProperties(prefix = "interceptor")
public class InterceptorProp {
    /**
     * 需要拦截的路径前缀
     */
    @Setter
    private String interceptPrefix = "/api";
    /**
     * 开放访问的路径pattern s
     */
    @Setter
    private List<String> permittedPath;
}
