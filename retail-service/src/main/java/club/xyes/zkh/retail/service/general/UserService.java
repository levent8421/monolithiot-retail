package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.service.basic.AbstractService;
import club.xyes.zkh.retail.wechat.dto.WxOAuth2AccessToken;

import java.util.List;

/**
 * Create by 郭文梁 2019/5/18 0018 11:37
 * UserService
 * 用户相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
public interface UserService extends AbstractService<User> {
    /**
     * 通过web oauth2 code登录
     *
     * @param code code
     * @return 用户
     */
    User loginByOAuthCode(String code);

    /**
     * 通过微信OpenId查找用户
     *
     * @param openId OpenId
     * @return 用户
     */
    User findByOpenId(String openId);

    /**
     * 从微信刷新用户信息
     *
     * @param user        用户对象
     * @param accessToken 微信令牌
     * @return 刷新结果
     */
    User refreshUserInfo(User user, WxOAuth2AccessToken accessToken);

    /**
     * 通过推广码查找用户
     *
     * @param promoCode 推广码
     * @return 用户
     */
    User findByPromoCode(String promoCode);

    /**
     * 设置用户角色为推广者
     *
     * @param user 用户
     * @return 设置结果
     */
    User toPromoter(User user);

    /**
     * 设置用户角色为队长
     *
     * @param user 用户
     * @return 设置结果
     */
    User toCaptain(User user);

    /**
     * 通过用户名或昵称搜索
     *
     * @param name 搜索内容
     * @return user list
     */
    List<User> searchByName(String name);

    /**
     * 设置用户为普通用户
     *
     * @param user 用户
     * @return 设置结果
     */
    User toPrimaryUser(User user);

    /**
     * 用户加入团队
     *
     * @param user   用户
     * @param leader 团队所有者ID
     */
    void join(User user, User leader);

    /**
     * 根据上级ID查询用户
     *
     * @param leader 队长
     * @return UserList
     */
    List<User> findByLeader(User leader);
}
