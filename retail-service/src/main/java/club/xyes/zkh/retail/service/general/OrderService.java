package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.dto.CountAndAmount;
import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.service.basic.AbstractService;
import club.xyes.zkh.retail.wechat.dto.WxPayNotifyVo;
import club.xyes.zkh.retail.wechat.dto.WxPayParams;
import com.github.pagehelper.PageInfo;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

/**
 * Create by 郭文梁 2019/5/22 0022 16:52
 * OrderService
 * 订单相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/5/22 0022
 */
public interface OrderService extends AbstractService<Order> {
    /**
     * 通过用户ID查询订单记录
     *
     * @param userId 用户ID
     * @param page   页码
     * @param rows   每页大小
     * @return PageInfo with Orders
     */
    PageInfo<Order> findByUserId(Integer userId, Integer page, Integer rows);

    /**
     * 根据用户ID和状态查询订单
     *
     * @param userId 用户ID
     * @param status 状态
     * @param page   页码
     * @param rows   每页大小
     * @return PageInfo with Order
     */
    PageInfo<Order> findByUserIdAndStatus(Integer userId, Integer status, Integer page, Integer rows);

    /**
     * 创建新的订单
     *
     * @param order 订单
     * @return 创建结果订单
     */
    Order create(Order order);

    /**
     * 刷新订单状态
     *
     * @param id       订单ID
     * @param listener 支付成功监听（回调）
     * @return 订单信息
     */
    Order refreshStatus(Integer id, PaySuccessListener listener);

    /**
     * 库存
     *
     * @param order    订单对象
     * @param stock    预约库存
     * @param listener 预约监听器
     * @return 订单对象
     */
    Order appoint(Order order, Stock stock, OrderAppointmentListener listener);

    /**
     * 执行核销订单逻辑
     *
     * @param order    订单
     * @param listener 核销监听器
     * @return 核销结果
     */
    Order complete(Order order, OrderCompleteListener listener);

    /**
     * 通过商铺ID统计核销和销售信息
     *
     * @param storeId 商铺ID
     * @return 统计结果
     */
    Map<String, CountAndAmount> analysisByStore(Integer storeId);

    /**
     * 订单支付
     *
     * @param order 订单
     * @param user  用户
     * @return 支付结果 (前端参数)
     */
    WxPayParams pay(Order order, User user);

    /**
     * 解决支付通知
     *
     * @param notifyVo 支付通知参数
     * @param listener 支付成功监听器
     * @return 订单对象
     */
    Order resolveTradeNotify(WxPayNotifyVo notifyVo, PaySuccessListener listener);

    /**
     * 根据推广者查询订单
     *
     * @param promoterId 推广者ID
     * @param page       页码
     * @param rows       每页大小
     * @return PageInfo
     */
    PageInfo<Order> findByPromoter(Integer promoterId, Integer page, Integer rows);

    /**
     * 通过SN查找订单 不存在时抛出异常
     *
     * @param sn SN
     * @return 订单
     */
    @NotNull
    Order requireBySn(String sn);

    /**
     * 通过商铺查询订单
     *
     * @param store 商铺
     * @param page  页码
     * @param rows  每页大小
     * @return PageInfo
     */
    PageInfo<Order> findByStore(Store store, Integer page, Integer rows);

    /**
     * 增加线下消费金额
     *
     * @param order                订单
     * @param offlinePaymentAmount 显现消费金额
     * @return Order
     */
    Order addOfflinePaymentAmount(Order order, Integer offlinePaymentAmount);

    /**
     * 取消订单预约
     *
     * @param order    订单
     * @param listener 订单预约监听
     * @return 订单
     */
    Order cancelAppointment(Order order, OrderAppointmentListener listener);

    /**
     * 获取商铺明天的预约订单信息
     *
     * @param storeId 商铺ID
     * @param page    页码
     * @param rows    每页大小
     * @return PageInfo
     */
    PageInfo<Order> findTomorrowOrdersByStore(Integer storeId, Integer page, Integer rows);

    /**
     * 查询店铺的每日预约订单
     *
     * @param storeId 店铺ID
     * @param date    日期
     * @param page    约吗
     * @param rows    每页大小
     * @return PageInfo
     */
    PageInfo<Order> findByStoreAndDate(Integer storeId, Date date, Integer page, Integer rows);

    /**
     * 分页查询所有订单 同时抓取出关联对象
     *
     * @param page 页码
     * @param rows 每页大小
     * @return PageInfo
     */
    PageInfo<Order> listFetchAll(Integer page, Integer rows);

    /**
     * 订单搜索
     *
     * @param query 搜索内容
     * @param page  页码
     * @param rows  每页大小
     * @return PageInfo
     */
    PageInfo<Order> search(String query, Integer page, Integer rows);

    /**
     * 通过ID查找订单 同时抓取出关联对象 当订单不存在时抛出异常
     *
     * @param id 订单ID
     * @return Order
     */
    @NotNull
    Order requireFetchAll(Integer id);

    /**
     * 支付成功监听器
     */
    interface PaySuccessListener {
        /**
         * 支付成功回调
         *
         * @param order 订单信息
         */
        void onPaySuccess(Order order);
    }

    /**
     * 订单核销完成监听器
     */
    interface OrderCompleteListener {
        /**
         * 订单核销前调用
         *
         * @param time  核销日期
         * @param order 订单
         */
        void beforeComplete(Order order, Date time);

        /**
         * 订单核销后调用
         *
         * @param order 订单
         */
        void afterComplete(Order order);
    }

    /**
     * 订单预约监听器
     */
    interface OrderAppointmentListener {
        /**
         * 订单预约时调用
         *
         * @param order 预约订单
         * @param stock 预约库存
         */
        void onAppointment(Order order, Stock stock);

        /**
         * 订单取消预约时调用
         *
         * @param order 订单
         */
        void onCancelAppointment(Order order);
    }
}
