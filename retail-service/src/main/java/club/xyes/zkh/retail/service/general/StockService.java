package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.vo.TimeRange;
import club.xyes.zkh.retail.service.basic.AbstractService;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * Create by 郭文梁 2019/6/12 0012 15:45
 * StockService
 * 库存相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
public interface StockService extends AbstractService<Stock> {
    /**
     * 批量创建库存
     *
     * @param store         商铺
     * @param commodity     商品
     * @param startDate     开始日期
     * @param endDate       结束日期
     * @param timeRangeList 时间段列表
     * @param daysOfWeek    星期列表
     * @param stockCount    库存
     * @return Stock List
     */
    List<Stock> create(Store store,
                       Commodity commodity,
                       Date startDate,
                       Date endDate,
                       List<TimeRange> timeRangeList,
                       List<Integer> daysOfWeek,
                       Integer stockCount);

    /**
     * 通过商铺ID和商品ID查询库存
     *
     * @param storeId     商铺ID
     * @param commodityId 商品ID
     * @param page        页码
     * @param rows        每页大小
     * @return PageInfo
     */
    PageInfo<Stock> findByStoreAndCommodity(Integer storeId, Integer commodityId, Integer page, Integer rows);

    /**
     * 递增核销完成数量
     *
     * @param stock 库存
     */
    void incrementCompleteCount(Stock stock);

    /**
     * 通过商铺ID和商品ID查找可用的预约库存
     *
     * @param storeId     商铺ID
     * @param commodityId 商品ID
     * @param page        页码
     * @param rows        每页大小
     * @return PageInfo
     */
    PageInfo<Stock> findAvailableByStoreAndCommodity(Integer storeId, Integer commodityId, Integer page, Integer rows);
}
