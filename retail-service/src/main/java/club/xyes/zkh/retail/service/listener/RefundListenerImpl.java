package club.xyes.zkh.retail.service.listener;

import club.xyes.zkh.retail.commons.entity.*;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.utils.CommissionUtil;
import club.xyes.zkh.retail.service.general.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Create by 郭文梁 2019/6/13 0013 15:47
 * RefundListenerImpl
 * 退款监听器
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@Component
@Slf4j
public class RefundListenerImpl implements RefundLogService.RefundListener {
    private final UserService userService;
    private final CommissionLogService commissionLogService;
    private final OrderService orderService;
    private final StockService stockService;

    public RefundListenerImpl(CommissionLogService commissionLogService,
                              UserService userService,
                              OrderService orderService,
                              StockService stockService) {
        this.commissionLogService = commissionLogService;
        this.userService = userService;
        this.orderService = orderService;
        this.stockService = stockService;
    }

    @Override
    public void onRefundCreate(RefundLog refundLog, Order order) {
        //取消返现记录
        doCancelCommission(order);
        updateOrderStatus(order, Order.STATUS_IN_REFUND);
    }

    @Override
    public void onRefundSuccess(RefundLog refundLog) {
        @NotNull final Order order = orderService.require(refundLog.getOrderId());
        updateStock(order);
        order.setAppointmentStockId(null);
        updateOrderStatus(order, Order.STATUS_REFUNDED);
    }

    @Override
    public void onRefundFail(RefundLog refundLog) {
        @NotNull final Order order = orderService.require(refundLog.getOrderId());
        updateOrderStatus(order, Order.STATUS_REFUND_FAIL);
    }

    /**
     * 更新库存
     *
     * @param order 订单
     */
    private void updateStock(Order order) {
        if (order.getAppointmentStockId() != null) {
            @NotNull final Stock stock = stockService.require(order.getAppointmentStockId());
            final Integer bookedCount = stock.getBookedCount();
            stock.setBookedCount(bookedCount - order.getQuantity());
            stockService.updateById(stock);
        }
    }

    /**
     * 取消订单的返现
     *
     * @param order 订单
     */
    private void doCancelCommission(Order order) {
        List<CommissionLog> commissionLogs = commissionLogService.findByOrder(order);
        for (CommissionLog commissionLog : commissionLogs) {
            @NotNull final User promoter = userService.require(commissionLog.getPromoterId());
            switch (commissionLog.getReason()) {
                case CommissionLog.REASON_DIRECT_PROMOTE:
                    promoter.setDirectIncome(CommissionUtil.subtract(promoter.getDirectIncome(), commissionLog.getAmount()));
                    break;
                case CommissionLog.REASON_TEAM_PROMOTE:
                    promoter.setTeamIncome(CommissionUtil.subtract(promoter.getTeamIncome(), commissionLog.getAmount()));
                    break;
                default:
                    throw new InternalServerErrorException("Unknown commission log reason[" + commissionLog.getReason() + "]");
            }
            userService.updateById(promoter);
            commissionLog.setStatus(CommissionLog.STATUS_REFUNDED);
            commissionLogService.updateById(commissionLog);
            log.debug("Cancel Commission for user [{}], commission[{}]", promoter, commissionLog.getAmount());
        }
    }

    /**
     * 更新订单状态
     *
     * @param order  订单
     * @param status 状态
     */
    private void updateOrderStatus(Order order, Integer status) {
        order.setStatus(status);
        orderService.updateById(order);
    }
}
