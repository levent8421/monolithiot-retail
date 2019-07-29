package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.CommissionLog;
import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.service.basic.AbstractService;

import java.util.List;

/**
 * Create by 郭文梁 2019/6/13 0013 18:01
 * CommissionLogService
 * 返佣记录相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
public interface CommissionLogService extends AbstractService<CommissionLog> {
    /**
     * 记录返现日志
     *
     * @param promoter 推手
     * @param order    订单
     * @param amount   返现金额
     * @param reason   返现原因
     */
    void log(User promoter, Order order, Integer amount, int reason);

    /**
     * 通过订单查询返现记录
     *
     * @param order 订单
     * @return 返现记录
     */
    List<CommissionLog> findByOrder(Order order);

    /**
     * 根据订单ID查询返现记录 同时抓取出关联对象
     *
     * @param orderId 订单ID
     * @return 返现记录
     */
    List<CommissionLog> findByOrderFetchAll(Integer orderId);
}
