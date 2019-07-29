package club.xyes.zkh.retail.map.dto;

import lombok.Data;

/**
 * Create by 郭文梁 2019/7/17 13:55
 * LocationAddressConvertResult
 * 坐标地址转换结果
 *
 * @author 郭文梁
 * @data 2019/7/17 13:55
 */
@Data
public class LocationAddressConvertResult {
    /**
     * 经度
     */
    private Double lon;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 国家
     */
    private String country;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 市编码
     */
    private String cityCode;
    /**
     * 区
     */
    private String district;
    /**
     * 区编码
     */
    private String districtCode;
    /**
     * 详细地址
     */
    private String detailAddress;
}
