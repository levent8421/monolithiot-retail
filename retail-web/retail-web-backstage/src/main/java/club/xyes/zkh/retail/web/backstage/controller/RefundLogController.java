package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.RefundLog;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.OrderService;
import club.xyes.zkh.retail.service.general.RefundLogService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static club.xyes.zkh.retail.commons.utils.ParamChecker.notNull;

/**
 * Create by 郭文梁 2019/6/13 0013 13:10
 * RefundLogController
 * 退款记录相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@RestController
@RequestMapping("/api/refund")
public class RefundLogController extends AbstractEntityController<RefundLog> {
    private final RefundLogService refundLogService;
    private final OrderService orderService;
    private final RefundLogService.RefundListener refundListener;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    public RefundLogController(RefundLogService service,
                               OrderService orderService,
                               RefundLogService.RefundListener refundListener) {
        super(service);
        this.refundLogService = service;
        this.orderService = orderService;
        this.refundListener = refundListener;
    }

    /**
     * 创建退款记录
     *
     * @param param 参数
     * @return GR
     */
    @PostMapping("/create")
    public GeneralResult<RefundLog> create(@RequestBody RefundLog param) {
        final RefundLog refundLog = new RefundLog();
        checkAndCopyParams(param, refundLog);
        Order order = orderService.require(refundLog.getOrderId());
        RefundLog res = refundLogService.create(refundLog, order, refundListener);
        return GeneralResult.ok(res);
    }

    /**
     * 检查并拷贝请求参数
     *
     * @param param     参数
     * @param refundLog 拷贝目标
     */
    private void checkAndCopyParams(RefundLog param, RefundLog refundLog) {
        final Class<BadRequestException> ex = BadRequestException.class;
        notNull(param, ex, "未传参数");
        notNull(param.getOrderId(), ex, "订单ID必填");
        notNull(param.getRefundAmount(), ex, "退款金额必填");

        refundLog.setOrderId(param.getOrderId());
        refundLog.setRefundAmount(param.getRefundAmount());
    }
}
