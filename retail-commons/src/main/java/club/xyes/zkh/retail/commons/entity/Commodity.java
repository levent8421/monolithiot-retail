package club.xyes.zkh.retail.commons.entity;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.utils.TextUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Create by 郭文梁 2019/5/17 0017 17:37
 * Commodity
 * 商品实体类
 *
 * @author 郭文梁
 * @data 2019/5/17 0017
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_commodity")
public class Commodity extends AbstractEntity {
    public static final String DEFAULT_SHARE_IMAGE = "default.png";
    /**
     * 状态：上架
     */
    public static final int STATUS_AVAILABLE = 0x01;
    /**
     * 状态：下架
     */
    public static final int STATUS_DISABLE = 0x02;
    /**
     * 商家ID
     */
    @Column(name = "store_id", length = 10, nullable = false)
    private Integer storeId;
    /**
     * 关联的Store对象
     */
    private Store store;
    /**
     * 每人购买上限
     */
    @Column(name = "buy_limit", length = 4)
    private Integer buyLimit;
    /**
     * 商品名称
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * 商品描述
     */
    @Column(name = "description")
    private String description;
    /**
     * 头部轮播图
     */
    @Column(name = "images")
    private String images;
    /**
     * 分享二维码背景图片
     */
    @Column(name = "share_image", nullable = false)
    private String shareImage;
    /**
     * 头部轮播图（切分后）
     */
    private List<String> imageList;
    /**
     * 原价
     */
    @Column(name = "original_price", length = 6)
    private Integer originalPrice;
    /**
     * 现价
     */
    @Column(name = "current_price", length = 6)
    private Integer currentPrice;
    /**
     * 库存
     */
    @Column(name = "stock_count", length = 4)
    private Integer stockCount;
    /**
     * 已售
     */
    @Column(name = "sale_count", length = 4)
    private Integer saleCount;
    /**
     * 一级返佣金额
     */
    @Column(name = "commission_1", length = 6, nullable = false)
    private Integer commission1;
    /**
     * 二级返佣金额
     */
    @Column(name = "commission_2", length = 6, nullable = false)
    private Integer commission2;
    /**
     * 三级返佣金额
     */
    @Column(name = "commission_3", length = 6, nullable = false)
    private Integer commission3;
    /**
     * 是否需要预约
     */
    @Column(name = "need_appointment", nullable = false)
    private Boolean needAppointment;
    /**
     * 抢购结束时间
     */
    @Column(name = "buy_end_time")
    @JsonFormat(pattern = ApplicationConstants.DATE_TIME_FORMAT)
    private Date buyEndTime;
    /**
     * 预约开始时间
     */
    @Column(name = "appointment_start_time")
    @JsonFormat(pattern = ApplicationConstants.DATE_TIME_FORMAT)
    private Date appointmentStartTime;
    /**
     * 预约结束时间
     */
    @JsonFormat(pattern = ApplicationConstants.DATE_TIME_FORMAT)
    @Column(name = "appointment_end_time")
    private Date appointmentEndTime;
    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 详情HTML访问地址 不与数据库字段关联
     */
    @Transient
    private String detailHtmlUrl;

    /**
     * 解析图片列表
     */
    public void parseImageList() {
        if (images != null) {
            //由于Arrays.asList创建的容器不允许修改 这里重新new一个ArrayList
            imageList = Arrays.stream(images.split(ApplicationConstants.IMAGE_DELIMITER))
                    .filter(TextUtils::isTrimedNotEmpty)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 将图片列表重新组合为字符串
     */
    public void convertImageList2Images() {
        if (imageList == null || imageList.isEmpty()) {
            this.images = null;
            return;
        }
        images = imageList.stream()
                .filter(v -> !Objects.isNull(v))
                .reduce((img1, img2) -> img1 + ApplicationConstants.IMAGE_DELIMITER + img2)
                .orElse(null);
    }
}
