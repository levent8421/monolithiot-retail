package club.xyes.zkh.retail.commons.dto;

import club.xyes.zkh.retail.commons.xml.annotation.XmlProperty;
import lombok.Data;

/**
 * Create by 郭文梁 2019/6/1 0001 17:20
 * CountAndAmount
 * 数量和金额的封装对象
 *
 * @author 郭文梁
 * @data 2019/6/1 0001
 */
@Data
public class CountAndAmount {
    /**
     * 数量
     */
    @XmlProperty(name = "count")
    private Integer count;
    /**
     * 金额
     */
    private Integer amount;
}
