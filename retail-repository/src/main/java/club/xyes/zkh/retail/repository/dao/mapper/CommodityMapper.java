package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.vo.CommodityWithDistance;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by 郭文梁 2019/5/20 0020 11:08
 * CommodityMapper
 * 商品相关数据库访问组件
 *
 * @author 郭文梁
 * @data 2019/5/20 0020
 */
@Repository
public interface CommodityMapper extends AbstractMapper<Commodity> {
    /**
     * 查询可用的商品，同时以创建时间降序排序
     *
     * @return Commodity List
     */
    List<Commodity> selectAvailableOrderByCreateTimeDesc();

    /**
     * 通过ID查询商铺 同时抓取出关联对象
     *
     * @param id ID
     * @return Commodity
     */
    Commodity selectByIdFetchAll(@Param("id") Integer id);

    /**
     * 通过名称搜索商品
     *
     * @param name 搜索名称
     * @return Commodities
     */
    List<Commodity> searchByName(@Param("name") String name);

    /**
     * 向t_commodity的images字段后追加字符串 即增加图片列表
     *
     * @param id       商品ID
     * @param filename 文件名
     * @return 影响行数
     */
    int appendImage(@Param("id") Integer id,
                    @Param("filename") String filename);

    /**
     * 更新商品库存
     *
     * @param id     商品ID
     * @param amount 更新数量（递增火递减数量）
     * @return 影响的行数
     */
    int updateStock(@Param("id") Integer id,
                    @Param("amount") int amount);

    /**
     * 通过地区索引查找商品
     *
     * @param addressIndexId 地区索引
     * @return 商品list
     */
    List<Commodity> selectByAddressIndex(@Param("addressIndexId") Integer addressIndexId);

    /**
     * 通过距离查询
     *
     * @param lon   经度
     * @param lat   纬度
     * @param range 搜索范围
     * @return CommodityWithDistance List
     */
    List<CommodityWithDistance> selectByDistance(@Param("lon") Double lon,
                                                 @Param("lat") Double lat,
                                                 @Param("range") Long range);
}
