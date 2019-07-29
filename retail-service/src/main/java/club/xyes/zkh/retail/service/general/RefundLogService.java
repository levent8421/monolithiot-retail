package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.RefundLog;
import club.xyes.zkh.retail.service.basic.AbstractService;

/**
 * Create by 郭文梁 2019/6/13 0013 13:07
 * RefundLogService
 * 退款记录相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
public interface RefundLogService extends AbstractService<RefundLog> {
    /**
     * 创建提现申请
     *
     * @param refundLog 提现记录参数
     * @param order     订单
     * @param listener  退款监听器
     * @return 提现记录
     */
    RefundLog create(RefundLog refundLog, Order order, RefundListener listener);

    /**
     * 处理微信退款通知
     *
     * @param reqInfo          通知加密信息
     * @param listener 退款监听器
     */
    void onNotify(String reqInfo, RefundListener listener);

    /**
     * 退款监听器
     */
    interface RefundListener {
        /**
         * 退款创建时调用
         *
         * @param refundLog 退款记录
         * @param order     订单
         */
        void onRefundCreate(RefundLog refundLog, Order order);

        /**
         * 退款成功时调用
         *
         * @param refundLog 退款记录
         */
        void onRefundSuccess(RefundLog refundLog);

        /**
         * 退款失败时调用
         *
         * @param refundLog 退款记录
         */
        void onRefundFail(RefundLog refundLog);
    }
}
