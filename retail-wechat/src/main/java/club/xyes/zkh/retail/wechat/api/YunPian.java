package club.xyes.zkh.retail.wechat.api;

import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.commons.entity.Store;
import com.yunpian.sdk.model.SmsSingleSend;

/**
 * Create by 郭文梁 2019/6/15 0015 10:12
 * YunPian
 * 云片API
 *
 * @author 郭文梁
 * @data 2019/6/15 0015
 */
public interface YunPian {
    /**
     * 发送预约成功通知短信
     *
     * @param order 订单
     * @param store 店铺
     * @param stock 库存
     * @return SmsSingleSend
     */
    SmsSingleSend sendAppointmentNotifySms(Order order, Store store, Stock stock);

    /**
     * 发送支付成功通知短信
     *
     * @param order 订单
     * @param store 店铺
     * @return SmsSingleSend
     */
    SmsSingleSend sendPaymentSuccessNotifySms(Order order, Store store);
}
