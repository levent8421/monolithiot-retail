package club.xyes.zkh.retail.web.commons.controller;

import club.xyes.zkh.retail.commons.entity.AbstractEntity;
import club.xyes.zkh.retail.commons.entity.Admin;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.service.basic.AbstractService;
import club.xyes.zkh.retail.service.general.AdminService;
import club.xyes.zkh.retail.service.general.StoreService;
import club.xyes.zkh.retail.service.general.UserService;

import javax.validation.constraints.NotNull;

/**
 * Create by 郭文梁 2019/5/18 0018 12:39
 * AbstractEntityController
 * 数据对象控制器
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
public abstract class AbstractEntityController<Entity extends AbstractEntity> extends AbstractController {
    /**
     * 业务组件
     */
    private final AbstractService<Entity> service;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected AbstractEntityController(AbstractService<Entity> service) {
        if (service == null) {
            throw new NullPointerException("The service for entity could not be null!");
        }
        this.service = service;
    }

    /**
     * 获取当前登录的用户
     *
     * @param userService 用户业务组件
     * @return 用户信息
     */
    @NotNull
    protected User requireCurrentUser(UserService userService) {
        return userService.require(requireUserLoginCookie().getUserInfo().getUserId());
    }

    /**
     * 获取当前登录的商铺对象
     *
     * @param storeService 商铺相关业务服务组件
     * @return 商铺对象
     */
    @NotNull
    protected Store requireCurrentStore(StoreService storeService) {
        return storeService.require(requireStoreLoginCookie().getStoreInfo().getStoreId());
    }

    /**
     * 获取当前登录的管理员账户
     *
     * @param adminService 管理员业务组件
     * @return Admin
     */
    protected Admin requireCurrentAdmin(AdminService adminService) {
        return adminService.require(requireAdminInfo().getAdminId());
    }
}
