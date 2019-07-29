package club.xyes.zkh.retail.wechat.dto;

import club.xyes.zkh.retail.commons.entity.Commodity;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Create by 郭文梁 2019/5/28 0028 10:24
 * GoodsDetail
 * 商品详情 用户和扫呗接口的数据交互
 *
 * @author 郭文梁
 * @data 2019/5/28 0028
 */
@Data
public class GoodsDetail {
    /**
     * 从商品对象创建
     *
     * @param commodity 商品对象
     * @param quantity  数量
     * @return 传给扫呗接口的商品详情
     */
    public static GoodsDetail fromCommodity(Commodity commodity, int quantity) {
        GoodsDetail res = new GoodsDetail();
        res.setGoodsId(String.valueOf(commodity.getId()));
        res.setGoodsName(commodity.getName());
        res.setPrice(String.valueOf(commodity.getCurrentPrice()));
        res.setQuantity(String.valueOf(quantity));
        return res;
    }

    /**
     * 商品编号
     */
    @JSONField(name = "goods_id")
    private String goodsId;
    /**
     * 商品名称
     */
    @JSONField(name = "goods_name")
    private String goodsName;
    /**
     * 数量
     */
    private String quantity;
    /**
     * 单价
     */
    private String price;
}
