package club.xyes.zkh.retail.wechat.api.impl;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.utils.DateTimeUtils;
import club.xyes.zkh.retail.wechat.api.YunPian;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Create by 郭文梁 2019/6/15 0015 13:17
 * YunPianImpl
 * 云片客户端实现
 *
 * @author 郭文梁
 * @data 2019/6/15 0015
 */
@Component
@Slf4j
public class YunPianImpl implements YunPian {
    /**
     * 预约成功短信通知模板
     */
    private static final String APPOINTMENT_NOTIFY_SMS_TEMPLATE =
            "【江苏智客汇】您已成功购买%s，预约时间%s %s到%s，电子码%s";
    /**
     * 支付成功通知短信
     */
    private static final String PAYMENT_SUCCESS_NOTIFY_SMS_TEMPLATE =
            "【江苏智客汇】您已成功购买%s，电子码%s，详情请关注小龙侠优品公总号";
    private final YunpianClient yunpianClient;

    public YunPianImpl(YunpianClient yunpianClient) {
        this.yunpianClient = yunpianClient;
    }

    @Override
    public SmsSingleSend sendAppointmentNotifySms(Order order, Store store, Stock stock) {
        String smsContent = getAppointmentNotifySmsContent(order, stock);
        log.info("Send appointment notify sms[{}] to [{}]", smsContent, order.getPhone());
        return singleSend(order.getPhone(), smsContent);
    }

    @Override
    public SmsSingleSend sendPaymentSuccessNotifySms(Order order, Store store) {
        final String smsContent = getPaymentSuccessNotifySmsContent(order);
        log.info("Send Payment Success notify sms [{}] to [{}]", smsContent, order.getPhone());
        return singleSend(order.getPhone(), smsContent);
    }

    /**
     * 发送一条短信
     *
     * @param to  发送到
     * @param msg 消息
     * @return 发送结果
     */
    private SmsSingleSend singleSend(String to, String msg) {
        final Map<String, String> requestParams = yunpianClient.newParam(2);
        requestParams.put(YunpianClient.MOBILE, to);
        requestParams.put(YunpianClient.TEXT, msg);
        final Result<SmsSingleSend> res = yunpianClient.sms().single_send(requestParams);
        if (!res.isSucc()) {
            throw new InternalServerErrorException(res.getMsg());
        }
        return res.getData();
    }

    /**
     * 获取支付成功的通知信息
     *
     * @param order 订单
     * @return sms content
     */
    private String getPaymentSuccessNotifySmsContent(Order order) {
        return String.format(PAYMENT_SUCCESS_NOTIFY_SMS_TEMPLATE,
                prettySmsParam(order.getCommodityName()),
                prettySmsParam(order.getSn())
        );
    }

    /**
     * 获取（拼接）预约通知短信内容
     *
     * @param order 订单
     * @param stock 库存
     * @return SmsContent
     */
    private String getAppointmentNotifySmsContent(Order order, Stock stock) {
        final String date = DateTimeUtils.format(stock.getActionDate(), ApplicationConstants.DATE_FORMAT_WITHOUT_YEAR);
        final String startTime = DateTimeUtils.format(stock.getStartTime(), ApplicationConstants.TIME_FORMAT_WITHOUT_SECOND);
        final String endTime = DateTimeUtils.format(stock.getEndTime(), ApplicationConstants.TIME_FORMAT_WITHOUT_SECOND);
        return String.format(APPOINTMENT_NOTIFY_SMS_TEMPLATE,
                prettySmsParam(order.getCommodityName()),
                prettySmsParam(date), prettySmsParam(startTime), prettySmsParam(endTime),
                prettySmsParam(order.getSn()));
    }

    /**
     * 处理短信内容参数
     *
     * @param param 参数
     * @return 处理结果
     */
    private String prettySmsParam(String param) {
        return param;
    }
}
