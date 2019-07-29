package club.xyes.zkh.retail.service.listener;

import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.service.general.OrderService;
import club.xyes.zkh.retail.service.general.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Create by 郭文梁 2019/6/12 0012 18:41
 * OrderCompleteListenerImpl
 * 订单完成监听器
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
@Component
@Slf4j
public class OrderCompleteListenerImpl implements OrderService.OrderCompleteListener {
    private final StockService stockService;

    public OrderCompleteListenerImpl(StockService stockService) {
        this.stockService = stockService;
    }

    @Override
    public void beforeComplete(Order order, Date time) {
        log.debug("Before Order Complete [{}]", order);
    }

    @Override
    public void afterComplete(Order order) {
        log.debug("Completed Order:[{}]", order);
    }
}
