package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.dto.CountAndAmount;
import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Create by 郭文梁 2019/5/22 0022 16:38
 * OrderMapper
 * 订单相关数据库访问组件
 *
 * @author 郭文梁
 * @data 2019/5/22 0022
 */
@Repository
public interface OrderMapper extends AbstractMapper<Order> {
    /**
     * 通过用户ID查询订单记录
     *
     * @param userId 用户ID
     * @return Order List
     */
    List<Order> selectByUserId(@Param("userId") Integer userId);

    /**
     * 通过用户ID】和状态查询订单
     *
     * @param userId 用户ID
     * @param status 状态
     * @return OrderList
     */
    List<Order> selectByUserIdAndStatus(@Param("userId") Integer userId,
                                        @Param("status") Integer status);

    /**
     * 总销售额和销售数量
     *
     * @param storeId 商铺ID
     * @return CAA
     */
    CountAndAmount totalSalesAndAmount(@Param("storeId") Integer storeId);

    /**
     * 通过日期查询商家的销售额和销售数量
     *
     * @param storeId 商铺ID
     * @param date    时间
     * @return CAA
     */
    CountAndAmount salesAndAmountByDate(@Param("storeId") Integer storeId,
                                        @Param("date") Date date);

    /**
     * 查询商家的总核销量和核销金额
     *
     * @param storeId 商铺ID
     * @return CAA
     */
    CountAndAmount totalCompleteCountAndAmount(@Param("storeId") Integer storeId);

    /**
     * 查询商家在某一天的核销量和核销金额
     *
     * @param storeId 商家ID
     * @param date    日期
     * @return CAA
     */
    CountAndAmount completeCountAndAmountByDate(@Param("storeId") Integer storeId,
                                                @Param("date") Date date);

    /**
     * 根据推广者ID查询订单
     *
     * @param promoterId 推广者ID
     * @return 订单列表
     */
    List<Order> selectByPromoterId(@Param("promoterId") Integer promoterId);

    /**
     * 通过商铺ID查询订单
     *
     * @param storeId 商铺ID
     * @return Order List
     */
    List<Order> selectByStoreId(@Param("storeId") Integer storeId);

    /**
     * 通过商铺ID查询商铺明天的预约订单
     *
     * @param storeId 商铺ID
     */
    List<Order> selectTomorrowAppointOrdersByStore(@Param("storeId") Integer storeId);

    /**
     * 查询店铺每日预约订单
     *
     * @param storeId 商铺ID
     * @param date    日期
     * @return Orders
     */
    List<Order> selectByStoreAndDate(@Param("storeId") Integer storeId,
                                     @Param("date") Date date);

    /**
     * 查询所有订单
     *
     * @return Order List
     */
    List<Order> selectFetchAll();

    /**
     * 搜索订单
     *
     * @param query 搜索字符串
     * @return Order List
     */
    List<Order> search(@Param("query") String query);

    /**
     * 通过ID查找订单 并抓取出关联对象
     *
     * @param id id
     * @return Order
     */
    Order selectByIdFetchAll(@Param("id") Integer id);
}
