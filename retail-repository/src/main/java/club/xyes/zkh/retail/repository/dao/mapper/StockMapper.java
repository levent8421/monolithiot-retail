package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by 郭文梁 2019/6/12 0012 15:29
 * StockMapper
 * 库存现金相关数据库访问组件
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
@Repository
public interface StockMapper extends AbstractMapper<Stock> {
    /**
     * 递增库存的核销数量
     *
     * @param id 可寻ID
     * @return 影响的行数
     */
    int incrementCompleteCount(@Param("id") Integer id);

    /**
     * 通过商品ID和商铺ID查询库存
     *
     * @param storeId     商铺ID
     * @param commodityId 商品ID
     * @return Stock list
     */
    List<Stock> selectByStoreAndCommodity(@Param("storeId") Integer storeId,
                                          @Param("commodityId") Integer commodityId);

    /**
     * 通过商铺ID和商品ID查询可用的预约库存
     *
     * @param storeId     商铺ID
     * @param commodityId 商品ID
     * @return StockList
     */
    List<Stock> selectAvailableByStoreAndCommodity(@Param("storeId") Integer storeId,
                                                   @Param("commodityId") Integer commodityId);
}
