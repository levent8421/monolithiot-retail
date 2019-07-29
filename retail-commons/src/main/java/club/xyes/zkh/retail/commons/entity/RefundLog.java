package club.xyes.zkh.retail.commons.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Create by 郭文梁 2019/6/13 0013 11:06
 * RefundLog
 * 退款记录
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_refund_log")
public class RefundLog extends AbstractEntity {
    /**
     * 状态：申请中 未完成
     */
    public static final int STATUS_CREATE = 0x00;
    /**
     * 退款成功
     */
    public static final int STATUS_SUCCESS = 0x01;
    /**
     * 退款失败
     */
    public static final int STATUS_FAIL = 0x02;
    /**
     * 交易号 退款单号
     */
    @Column(name = "trade_no", nullable = false)
    private String tradeNo;
    /**
     * 订单ID
     */
    @Column(name = "order_id", length = 10, nullable = false)
    private Integer orderId;
    private Order order;
    /**
     * 订单金额
     */
    @Column(name = "order_amount")
    private Integer orderAmount;
    /**
     * 退款金额
     */
    @Column(name = "refund_amount", length = 10, nullable = false)
    private Integer refundAmount;
    /**
     * 完成时间
     */
    @Column(name = "complete_time")
    private Date completeTime;
    /**
     * 状态
     */
    @Column(name = "status", length = 2, nullable = false)
    private Integer status;
}
