package club.xyes.zkh.retail.web.backstage.interceptors.conf;

import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.web.backstage.interceptors.AdminAccessTokenInterceptor;
import club.xyes.zkh.retail.web.backstage.interceptors.prop.InterceptorProp;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Create by 郭文梁 2019/6/20 0020 11:33
 * InterceptorConfig
 * 拦截器配置
 *
 * @author 郭文梁
 * @data 2019/6/20 0020
 */
@Configuration
@Component
public class InterceptorConfig implements WebMvcConfigurer, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private final AccessTokenEncoder accessTokenEncoder;
    private final InterceptorProp interceptorProp;

    public InterceptorConfig(AccessTokenEncoder accessTokenEncoder, InterceptorProp interceptorProp) {
        this.accessTokenEncoder = accessTokenEncoder;
        this.interceptorProp = interceptorProp;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final PathMatcher pathMatcher = applicationContext.getBean(PathMatcher.class);
        final AdminAccessTokenInterceptor interceptor = new AdminAccessTokenInterceptor(pathMatcher, accessTokenEncoder, interceptorProp);
        registry.addInterceptor(interceptor);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
