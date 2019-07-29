package club.xyes.zkh.retail.commons.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Create by 郭文梁 2019/7/17 14:59
 * AddressIndex
 * 地区索引实体类
 *
 * @author 郭文梁
 * @data 2019/7/17 14:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "t_address_index")
public class AddressIndex extends AbstractEntity {
    /**
     * 省
     */
    @Column(name = "province", nullable = false)
    private String province;
    /**
     * 市
     */
    @Column(name = "city", nullable = false)
    private String city;
    /**
     * 市编码
     */
    @Column(name = "city_code", length = 100, nullable = false)
    private String cityCode;
    /**
     * 区
     */
    @Column(name = "district")
    private String district;
    /**
     * 区编码
     */
    @Column(name = "district_code", length = 100)
    private String districtCode;
}
