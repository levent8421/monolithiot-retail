package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.AddressIndex;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.service.basic.AbstractService;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * Create by 郭文梁 2019/5/20 0020 11:13
 * StoreService
 * 商铺相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/5/20 0020
 */
public interface StoreService extends AbstractService<Store> {
    /**
     * 商家登录
     *
     * @param loginName 登录名
     * @param password  密码
     * @param openId    微信OpenId
     * @return Store
     */
    Store login(String loginName, String password, String openId);

    /**
     * 通过登录名查找商铺
     *
     * @param loginName 登录名
     * @return 商铺对象
     */
    Store findByLoginName(String loginName);

    /**
     * 通过OAuth code商铺自动登录
     *
     * @param code    OAuth2 Code
     * @param content 上下文 供方法内部向外界暴露结果
     * @return 商铺对象
     */
    Store loginByOAuthCode(String code, Map<String, Object> content);

    /**
     * 创建新的店铺信息
     *
     * @param store 店铺
     * @return 创建结果
     */
    Store create(Store store);

    /**
     * 清空商铺的openId
     *
     * @param store 商铺
     */
    void cleanOpenId(Store store);

    /**
     * 清空指定的OpenId
     *
     * @param store  商铺
     * @param openId openId
     */
    void cleanOpenId(Store store, String openId);

    /**
     * 设置店铺的地区索引
     *
     * @param store 店铺
     * @param index 索引
     */
    void setAddressIndex(Store store, AddressIndex index);

    /**
     * 设置商铺的排序号
     *
     * @param store    商铺
     * @param orderNum 排序号
     */
    void setOrderNum(Store store, Integer orderNum);

    /**
     * 查询所有店铺 并且抓取出关联对象
     *
     * @param page 页码
     * @param rows 每页大小
     * @return PageInfo
     */
    PageInfo<Store> listFetchAll(Integer page, Integer rows);
}
