package club.xyes.zkh.retail.commons.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Create by 郭文梁 2019/6/13 0013 17:49
 * CommissionLog
 * 返佣记录实体类
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_commission_log")
public class CommissionLog extends AbstractEntity {
    /**
     * 返现原因 直接推广
     */
    public static final int REASON_DIRECT_PROMOTE = 0x01;
    /**
     * 返现原因 团队推广
     */
    public static final int REASON_TEAM_PROMOTE = 0x02;
    /**
     * 状态 已到账
     */
    public static final int STATUS_SUCCESS = 0x00;
    /**
     * 状态 已退款
     */
    public static final int STATUS_REFUNDED = 0x01;
    /**
     * 订单ID
     */
    @Column(name = "order_id", length = 10, nullable = false)
    private Integer orderId;
    /**
     * 推手ID
     */
    @Column(name = "promoter_id", length = 10, nullable = false)
    private Integer promoterId;
    /**
     * 关联的推手对象
     */
    private User promoter;
    /**
     * 返佣金额
     */
    @Column(name = "amount", length = 10, nullable = false)
    private Integer amount;
    /**
     * 返佣原因
     */
    @Column(name = "reason", length = 2, nullable = false)
    private Integer reason;
    /**
     * 状态
     */
    @Column(name = "status", length = 2, nullable = false)
    private Integer status;
}
