package club.xyes.zkh.retail.commons.vo;

import club.xyes.zkh.retail.commons.entity.Commodity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Create by 郭文梁 2019/7/19 10:56
 * CommodityWithDistance
 * 商品信息 携带距离信息
 *
 * @author 郭文梁
 * @data 2019/7/19 10:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommodityWithDistance extends Commodity {
    /**
     * 距离信息
     */
    private Long distance;
}
