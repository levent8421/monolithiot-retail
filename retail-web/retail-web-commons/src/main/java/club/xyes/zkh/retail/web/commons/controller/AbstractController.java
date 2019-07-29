package club.xyes.zkh.retail.web.commons.controller;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.exception.PermissionDeniedException;
import club.xyes.zkh.retail.commons.holder.RequestExtendParamHolder;
import club.xyes.zkh.retail.web.commons.vo.AdminLoginCookie;
import club.xyes.zkh.retail.web.commons.vo.StoreLoginCookie;
import club.xyes.zkh.retail.web.commons.vo.UserLoginCookie;

import javax.validation.constraints.NotNull;

/**
 * Create by 郭文梁 2019/5/18 0018 12:38
 * AbstractController
 * 控制器基类
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
public abstract class AbstractController {
    /**
     * 默认页码
     */
    private static final int DEFAULT_PAGE = 1;
    /**
     * 默认每页大小
     */
    private static final int DEFAULT_ROWS = 10;

    /**
     * 当传入的page=null时返回默认页码，否则返回page
     *
     * @param page 页码
     * @return 页码
     */
    @NotNull
    protected int defaultPage(Integer page) {
        return page == null ? DEFAULT_PAGE : page;
    }

    /**
     * 当传入的rows==null时返回默认每页大小，否则反回rows
     *
     * @param rows 每页大小
     * @return 每页大小
     */
    @NotNull
    protected int defaultRows(Integer rows) {
        return rows == null ? DEFAULT_ROWS : rows;
    }

    /**
     * 获取当前用户登录Cookie
     *
     * @return 用户登录Cookie
     */
    UserLoginCookie getUserLoginCookie() {
        return RequestExtendParamHolder.get(ApplicationConstants.Http.USER_TOKEN_EXTEND_PARAM_NAME, UserLoginCookie.class);
    }

    /**
     * 获取用户登录Cookie 若为空则抛出异常
     *
     * @return UserLoginCookie
     */
    UserLoginCookie requireUserLoginCookie() {
        UserLoginCookie userLoginCookie = getUserLoginCookie();
        if (userLoginCookie == null) {
            throw new PermissionDeniedException("当前未登录任何用户");
        }
        return userLoginCookie;
    }

    /**
     * 获取当前商铺登录的Cookie
     *
     * @return StoreLoginCookie
     */
    protected StoreLoginCookie getStoreLoginCookie() {
        return RequestExtendParamHolder.get(ApplicationConstants.Http.STORE_TOKEN_EXTEND_PARAM_NAME, StoreLoginCookie.class);
    }

    /**
     * 获取当前商铺登录Cookie 若为空则抛出异常
     *
     * @return cookie
     */
    @NotNull
    protected StoreLoginCookie requireStoreLoginCookie() {
        StoreLoginCookie cookie = getStoreLoginCookie();
        if (cookie == null) {
            throw new PermissionDeniedException("当前未登录商铺");
        }
        return cookie;
    }

    /**
     * 获取用户基本信息 若不存在 则抛出异常
     *
     * @return 用户基本信息
     */
    protected UserLoginCookie.UserInfo requireUserInfo() {
        return requireUserLoginCookie().getUserInfo();
    }

    /**
     * 获取管理员的令牌Cookie信息
     *
     * @return AdminLoginCookie
     */
    protected AdminLoginCookie getAdminLoginCookie() {
        return RequestExtendParamHolder.get(ApplicationConstants.Http.ADMIN_TOKEN_EXTEND_PARAM_NAME, AdminLoginCookie.class);
    }

    /**
     * 获取当前登录的管理员令牌cookie信息， 不存在时抛出异常
     *
     * @return cookie
     */
    @NotNull
    protected AdminLoginCookie requireAdminLoginCookie() {
        final AdminLoginCookie cookie = getAdminLoginCookie();
        if (cookie == null) {
            throw new PermissionDeniedException("管理员未登录");
        }
        return cookie;
    }

    /**
     * 获取当前登录的管理员信息，
     *
     * @return AdminInfo
     */
    protected AdminLoginCookie.AdminInfo requireAdminInfo() {
        return requireAdminLoginCookie().getAdminInfo();
    }
}
