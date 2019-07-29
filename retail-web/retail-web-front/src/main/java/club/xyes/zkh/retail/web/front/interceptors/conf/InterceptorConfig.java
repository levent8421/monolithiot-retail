package club.xyes.zkh.retail.web.front.interceptors.conf;

import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.web.commons.intercept.AccessTokenInterceptor;
import club.xyes.zkh.retail.web.front.interceptors.ApiInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Create by 郭文梁 2019/5/20 0020 19:03
 * InterceptorConfig
 * 拦截器配置
 *
 * @author 郭文梁
 * @data 2019/5/20 0020
 */
@Configuration
@Component
public class InterceptorConfig implements WebMvcConfigurer, ApplicationContextAware {
    private final ApiInterceptor apiInterceptor;
    private ApplicationContext applicationContext;

    public InterceptorConfig(ApiInterceptor apiInterceptor) {
        this.apiInterceptor = apiInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiInterceptor);

        AccessTokenEncoder tokenEncoder = applicationContext.getBean(AccessTokenEncoder.class);
        PathMatcher pathMatcher = applicationContext.getBean(PathMatcher.class);
        registry.addInterceptor(new AccessTokenInterceptor(tokenEncoder, pathMatcher));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
