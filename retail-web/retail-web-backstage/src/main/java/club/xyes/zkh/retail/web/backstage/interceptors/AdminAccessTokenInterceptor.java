package club.xyes.zkh.retail.web.backstage.interceptors;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.holder.RequestExtendParamHolder;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.web.backstage.interceptors.prop.InterceptorProp;
import club.xyes.zkh.retail.web.commons.vo.AdminLoginCookie;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by 郭文梁 2019/6/20 0020 09:18
 * AdminAccessTokenInterceptor
 * 后台访问权限拦截器
 *
 * @author 郭文梁
 * @data 2019/6/20 0020
 */
@Slf4j
public class AdminAccessTokenInterceptor implements HandlerInterceptor {
    private final PathMatcher pathMatcher;
    private final AccessTokenEncoder encoder;
    private final InterceptorProp interceptorProp;

    public AdminAccessTokenInterceptor(PathMatcher pathMatcher,
                                       AccessTokenEncoder encoder,
                                       InterceptorProp interceptorProp) {
        this.pathMatcher = pathMatcher;
        this.encoder = encoder;
        this.interceptorProp = interceptorProp;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String path = request.getRequestURI();
        if (!needIntercept(path)) {
            log.debug("Resolve request [{}] without permission check!", path);
            return true;
        }
        final AdminLoginCookie loginCookie = AdminLoginCookie.readFromRequest(request, encoder);
        log.debug("Resolve request [{}], admin [{}]", path, loginCookie);
        if (loginCookie == null) {
            response.setContentType(ApplicationConstants.Http.CONTENT_TYPE_JSON_UTF8);
            response.getWriter().write(JSON.toJSONString(GeneralResult.permissionDenied("未登录")));
            return false;
        }
        RequestExtendParamHolder.set(ApplicationConstants.Http.ADMIN_TOKEN_EXTEND_PARAM_NAME, loginCookie);
        return true;
    }

    /**
     * 检查是否需要拦截
     *
     * @param path 路径
     * @return 是否需拦截
     */
    private boolean needIntercept(String path) {
        if (!path.startsWith(interceptorProp.getInterceptPrefix())) {
            return false;
        }
        if (interceptorProp.getPermittedPath() == null || interceptorProp.getPermittedPath().size() <= 0) {
            return true;
        }
        for (String pattern : interceptorProp.getPermittedPath()) {
            if (pathMatcher.match(pattern, path)) {
                return false;
            }
        }
        return true;
    }
}
