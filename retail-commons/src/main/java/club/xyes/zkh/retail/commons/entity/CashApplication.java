package club.xyes.zkh.retail.commons.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Create by 郭文梁 2019/5/18 0018 10:07
 * CashApplication
 * 提现申请实体类
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_cash_application")
public class CashApplication extends AbstractEntity {
    /**
     * 状态：创建申请
     */
    public static final int STATUS_CREATE = 0x01;
    /**
     * 状态：提现完成
     */
    public static final int STATUS_COMPLETE = 0x02;
    /**
     * 状态：拒绝提现
     */
    public static final int STATUS_REFUSE = 0x03;
    /**
     * 用户ID
     */
    @Column(name = "user_id", length = 10, nullable = false)
    private Integer userId;
    /**
     * 关联的用户对象
     */
    private User user;
    @Column(name = "amount", length = 6, nullable = false)
    private Integer amount;
    /**
     * 订单号
     */
    @Column(name = "trade_no")
    private String tradeNo;
    /**
     * 状态
     */
    @Column(name = "status", length = 2, nullable = false)
    private Integer status;
}
