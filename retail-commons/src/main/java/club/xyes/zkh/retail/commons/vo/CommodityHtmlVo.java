package club.xyes.zkh.retail.commons.vo;

import club.xyes.zkh.retail.commons.entity.Commodity;
import lombok.Data;

/**
 * Create by 郭文梁 2019/6/1 0001 14:23
 * CommodityHtmlVo
 * 商品Html参数
 *
 * @author 郭文梁
 * @data 2019/6/1 0001
 */
@Data
public class CommodityHtmlVo {
    private Commodity commodity;
    private String html;
}
