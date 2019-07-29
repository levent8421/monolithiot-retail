package club.xyes.zkh.retail.web.front.vo;

import lombok.Data;

/**
 * Create by 郭文梁 2019/5/27 0027 15:07
 * OrderCreateVo
 * 创建订单需要的参数
 *
 * @author 郭文梁
 * @data 2019/5/27 0027
 */
@Data
public class OrderCreateVo {
    /**
     * 姓名
     */
    private String username;
    /**
     * 电话
     */
    private String phone;
    /**
     * 备注
     */
    private String description;
    /**
     * 商品ID
     */
    private Integer commodityId;
    /**
     * 购买数量
     */
    private Integer quantity;
    /**
     * 推广码
     */
    private String promoCode;
}
