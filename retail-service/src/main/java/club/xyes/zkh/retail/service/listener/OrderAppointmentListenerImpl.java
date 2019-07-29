package club.xyes.zkh.retail.service.listener;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.DateTimeUtils;
import club.xyes.zkh.retail.service.general.OrderService;
import club.xyes.zkh.retail.service.general.StockService;
import club.xyes.zkh.retail.service.general.StoreService;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.wechat.api.Wechat;
import club.xyes.zkh.retail.wechat.api.YunPian;
import club.xyes.zkh.retail.wechat.dto.TemplateMsgData;
import club.xyes.zkh.retail.wechat.dto.WxTemplateMsgParam;
import club.xyes.zkh.retail.wechat.dto.WxTemplateMsgResult;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by 郭文梁 2019/6/12 0012 18:54
 * OrderAppointmentListenerImpl
 * 订单预约监听器
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
@Component
@Slf4j
public class OrderAppointmentListenerImpl implements OrderService.OrderAppointmentListener {
    private final StockService stockService;
    private final YunPian yunPian;
    private final StoreService storeService;
    private final UserService userService;
    private final Wechat wechat;

    public OrderAppointmentListenerImpl(StockService stockService,
                                        YunPian yunPian,
                                        StoreService storeService,
                                        UserService userService,
                                        Wechat wechat) {
        this.stockService = stockService;
        this.yunPian = yunPian;
        this.storeService = storeService;
        this.userService = userService;
        this.wechat = wechat;
    }

    @Override
    public void onAppointment(Order order, Stock stock) {
        if (stock.getBookedCount() + order.getQuantity() > stock.getStockCount()) {
            throw new BadRequestException("该时段已经约满！");
        }
        stock.setBookedCount(stock.getBookedCount() + order.getQuantity());
        final Stock updateRes = stockService.updateById(stock);
        log.debug("Update (increment) bookedCount for Stock [{}]", updateRes);
        sendAppointmentNotifySms(order, stock);
        try {
            sendWxTemplateNotifyMsg(order, stock);
        } catch (Exception e) {
            log.warn("Could not send template msg for order: {}", order);
        }
    }

    @Override
    public void onCancelAppointment(Order order) {
        @NotNull final Stock stock = stockService.require(order.getAppointmentStockId());
        stock.setBookedCount(stock.getBookedCount() - order.getQuantity());
        final Stock updateRes = stockService.updateById(stock);
        log.debug("Update (decrement) bookedCount for Stock [{}]", updateRes);
    }

    /**
     * 发送预约成功通知短信
     *
     * @param order 订单
     * @param stock 库存
     */
    private void sendAppointmentNotifySms(Order order, Stock stock) {
        @NotNull final Store store = storeService.require(order.getStoreId());
        final SmsSingleSend res = yunPian.sendAppointmentNotifySms(order, store, stock);
        log.debug("Sms send result: [{}]", res);
    }

    /**
     * 发送模板消息通知
     *
     * @param order 订单
     * @param stock 库存
     */
    private void sendWxTemplateNotifyMsg(Order order, Stock stock) {
        @NotNull final User user = userService.require(order.getUserId());
        final String appointmentTime = String.format("%s (%s 至 %s)",
                DateTimeUtils.format(stock.getActionDate(), ApplicationConstants.DATE_FORMAT),
                DateTimeUtils.format(stock.getStartTime(), ApplicationConstants.TIME_FORMAT_WITHOUT_SECOND),
                DateTimeUtils.format(stock.getEndTime(), ApplicationConstants.TIME_FORMAT_WITHOUT_SECOND));

        final Map<String, TemplateMsgData> data = new HashMap<>(16);
        data.put("first", new TemplateMsgData("请凭电子码[" + order.getSn() + "]到店消费", TemplateMsgData.DEFAULT_COLOR));
        data.put("remark", new TemplateMsgData("预约成功，请在预约时间到店消费", TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword1", new TemplateMsgData(order.getUsername(), TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword2", new TemplateMsgData(order.getPhone(), TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword3", new TemplateMsgData(appointmentTime, TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword4", new TemplateMsgData(order.getCommodityName(), TemplateMsgData.DEFAULT_COLOR));

        final WxTemplateMsgParam msgParam = new WxTemplateMsgParam();
        msgParam.setToUser(user.getWxOpenId());
        msgParam.setTemplateId(ApplicationConstants.WeChat.APPOINTMENT_NOTIFY_MSG_TEMPLATE_ID);
        msgParam.setData(data);
        final WxTemplateMsgResult sendRes = wechat.sendTemplateMsg(msgParam);
        log.debug("Send appointment success template msg result [{}]", sendRes);
    }
}
