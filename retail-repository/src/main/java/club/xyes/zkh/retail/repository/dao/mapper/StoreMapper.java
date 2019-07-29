package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by 郭文梁 2019/5/18 0018 18:13
 * StoreMapper
 * 商铺相关数据库范文组件
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@Repository
public interface StoreMapper extends AbstractMapper<Store> {
    /**
     * 设置指定openId的Store的OpenId为null
     *
     * @param openId openId
     * @return 影响的行数
     */
    int cleanOpenId(@Param("openId") String openId);

    /**
     * 更具已记录的登录OpenId查询书杭浦
     *
     * @param openId openId
     * @return Store
     */
    Store findLikeOpenId(@Param("openId") String openId);

    /**
     * 查询所有店铺 同属抓取出关联对象
     *
     * @return Store List
     */
    List<Store> selectFetchAll();
}
