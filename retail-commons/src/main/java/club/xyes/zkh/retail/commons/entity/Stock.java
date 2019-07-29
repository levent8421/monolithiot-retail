package club.xyes.zkh.retail.commons.entity;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Create by 郭文梁 2019/6/12 0012 15:22
 * Stock
 * 库存实体对象
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_stock")
public class Stock extends AbstractEntity {
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
     * 商铺ID
     */
    @Column(name = "store_id", length = 10, nullable = false)
    private Integer storeId;
    /**
     * 关联的商铺对象
     */
    private Store store;
    /**
     * 作用日期
     */
    @Column(name = "action_date", nullable = false)
    @JsonFormat(pattern = ApplicationConstants.DATE_FORMAT)
    private Date actionDate;
    /**
     * 开始时间
     */
    @Column(name = "day_of_week", length = 1, nullable = false)
    private Integer dayOfWeek;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = ApplicationConstants.TIME_FORMAT_WITHOUT_SECOND)
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = ApplicationConstants.TIME_FORMAT_WITHOUT_SECOND)
    @Column(name = "end_time", nullable = false)
    private Date endTime;
    /**
     * 库存量
     */
    @Column(name = "stock_count", length = 5, nullable = false)
    private Integer stockCount;
    /**
     * 已预约数量
     */
    @Column(name = "booked_count", length = 5, nullable = false)
    private Integer bookedCount;
    /**
     * 已核销数量
     */
    @Column(name = "complete_count", length = 5, nullable = false)
    private Integer completeCount;
}
