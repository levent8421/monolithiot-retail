package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.Admin;
import club.xyes.zkh.retail.service.basic.AbstractService;

/**
 * Create by 郭文梁 2019/6/20 0020 10:38
 * AdminService
 * 管理员账户相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/6/20 0020
 */
public interface AdminService extends AbstractService<Admin> {
    /**
     * 管理员登录
     *
     * @param name     登录名
     * @param password 密码
     * @return Admin Entity
     */
    Admin login(String name, String password);

    /**
     * 创建管理员账户
     *
     * @param admin 创建参数
     * @return Admin
     */
    Admin create(Admin admin);

    /**
     * 更新账户信息
     *
     * @param admin admin
     * @return admin
     */
    Admin update(Admin admin);
}
