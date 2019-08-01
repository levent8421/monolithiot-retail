package club.xyes.zkh.retail.commons.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Create by 郭文梁 2019/5/18 0018 10:12
 * Order
 * 订单实体类
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_order")
public class Order extends AbstractEntity {
    /**
     * 状态 创建
     */
    public static final int STATUS_CREATE = 0x00;
    /**
     * 已支付 无需预约
     */
    public static final int STATUS_PAID = 0x01;
    /**
     * 已支付 需要预约
     */
    public static final int STATUS_NEED_BOOKED = 0x02;
    /**
     * 已预约
     */
    public static final int STATUS_BOOKED = 0x03;
    /**
     * 已完成 已核销
     */
    public static final int STATUS_COMPLETE = 0x04;
    /**
     * 退款中
     */
    public static final int STATUS_IN_REFUND = 0x05;
    /**
     * 已退款
     */
    public static final int STATUS_REFUNDED = 0x06;
    /**
     * 退款失败
     */
    public static final int STATUS_REFUND_FAIL = 0x07;
    /**
     * 状态 待收货
     */
    public static final int STATUS_DELIVER = 0x08;
    /**
     * 用户ID
     */
    @Column(name = "user_id", length = 10, nullable = false)
    private Integer userId;
    /**
     * 关联的用户对象
     */
    private User user;
    /**
     * 推广者ID
     */
    @Column(name = "promoter_id", length = 10)
    private Integer promoterId;
    /**
     * 推广者用户对象
     */
    private User promoter;
    /**
     * 二级返现用户
     */
    @Column(name = "promoter_2_id", length = 10)
    private Integer promoter2Id;
    /**
     * 三级返现用户
     */
    @Column(name = "promoter_3_id", length = 10)
    private Integer promoter3Id;
    /**
     * 商品ID
     */
    @Column(name = "commodity_id", length = 10, nullable = false)
    private Integer commodityId;
    /**
     * 关联的商品对象
     */
    private Commodity commodity;
    /**
     * 购买数量
     */
    @Column(name = "quantity", length = 3, nullable = false)
    private Integer quantity;
    /**
     * 商铺ID
     */
    @Column(name = "store_id", length = 10, nullable = false)
    private Integer storeId;
    /**
     * 关联的商铺对象
     */
    private Store store;
    /**
     * 金额
     */
    @Column(name = "amount", length = 10, nullable = false)
    private Integer amount;
    /**
     * 线下消费金额
     */
    @Column(name = "offline_payment_amount", length = 10)
    private Integer offlinePaymentAmount;
    /**
     * 姓名
     */
    @Column(name = "username", nullable = false)
    private String username;
    /**
     * 送货地址
     */
    @Column(name = "address")
    private String address;
    /**
     * 电话号
     */
    @Column(name = "phone", length = 100, nullable = false)
    private String phone;
    /**
     * 电子码，序列号
     */
    @Column(name = "sn", nullable = false)
    private String sn;
    /**
     * 订单号
     */
    @Column(name = "trade_no", nullable = false)
    private String tradeNo;
    /**
     * 商品名称快照
     */
    @Column(name = "commodity_name", nullable = false)
    private String commodityName;
    /**
     * 商品描述快照
     */
    @Column(name = "commodity_description", nullable = false)
    private String commodityDescription;
    /**
     * 状态
     */
    @Column(name = "status", length = 2, nullable = false)
    private Integer status;
    /**
     * 预约库存ID
     */
    @Column(name = "appointment_stock_id", length = 10)
    private Integer appointmentStockId;
    /**
     * 愉悦的时段库存
     */
    private Stock appointmentStock;
    /**
     * 订单完成时间
     */
    @Column(name = "complete_time")
    private Date completeTime;
    /**
     * 二维码地址
     * 不与数据表字段关联
     */
    @Transient
    private String qrCodeUrl;
}
