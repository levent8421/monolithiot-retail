package club.xyes.zkh.retail.web.commons.intercept;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.holder.RequestExtendParamHolder;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.web.commons.vo.StoreLoginCookie;
import club.xyes.zkh.retail.web.commons.vo.UserLoginCookie;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create by 郭文梁 2019/5/24 0024 11:12
 * AccessTokenInterceptor
 * 令牌拦截器
 *
 * @author 郭文梁
 * @data 2019/5/24 0024
 */
@Slf4j
public class AccessTokenInterceptor implements HandlerInterceptor {
    private final AccessTokenEncoder encoder;
    private final PathMatcher pathMatcher;

    public AccessTokenInterceptor(AccessTokenEncoder encoder,
                                  PathMatcher pathMatcher) {
        this.encoder = encoder;
        this.pathMatcher = pathMatcher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (pathMatcher.match(ApplicationConstants.Http.API_BASE_PATH, path)) {
            return doIntercept(request, response);
        } else {
            return true;
        }
    }

    /**
     * 执行拦截操作
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return 是否继续执行下一级拦截
     */
    private boolean doIntercept(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLoginCookie userCookie = UserLoginCookie.readFromRequest(request, encoder);
        StoreLoginCookie storeCookie = StoreLoginCookie.readFromRequest(request, encoder);
        if (userCookie == null && storeCookie == null) {
            final boolean noAuth = pathMatcher.match(ApplicationConstants.Http.WITHOUT_AUTH_PATH, request.getRequestURI());
            if (!noAuth) {
                response.setContentType(ApplicationConstants.Http.CONTENT_TYPE_JSON_UTF8);
                response.getWriter().write(JSON.toJSONString(GeneralResult.permissionDenied("未登录")));
            }
            return noAuth;
        }
        RequestExtendParamHolder.set(ApplicationConstants.Http.USER_TOKEN_EXTEND_PARAM_NAME, userCookie);
        RequestExtendParamHolder.set(ApplicationConstants.Http.STORE_TOKEN_EXTEND_PARAM_NAME, storeCookie);
        log.info("Resolve Request [{}] [{}], user:[{}], store:[{}]",
                request.getMethod(), request.getRequestURI(),
                getUserId(userCookie), getStoreId(storeCookie));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        RequestExtendParamHolder.clear();
    }

    private Integer getUserId(UserLoginCookie cookie) {
        return cookie == null ? null : cookie.getUserInfo().getUserId();
    }

    private Integer getStoreId(StoreLoginCookie cookie) {
        return cookie == null ? null : cookie.getStoreInfo().getStoreId();
    }
}
