package club.xyes.zkh.retail.map;

import club.xyes.zkh.retail.map.dto.Location2AddressResult;
import club.xyes.zkh.retail.map.dto.LocationAddressConvertResult;

/**
 * Create by 郭文梁 2019/7/17 9:06
 * MapService
 * 地图服务
 *
 * @author 郭文梁
 * @data 2019/7/17 9:06
 */
public interface MapService {
    /**
     * 坐标转地区
     *
     * @param locations 坐标列表
     * @return 转换结果
     */
    Location2AddressResult location2Address(String locations);

    /**
     * 经纬度和地区转换 只保留行政区信息
     *
     * @param lon 经度
     * @param lat 纬度
     * @return 转换结果
     */
    LocationAddressConvertResult location2AddressPretty(Double lon, Double lat);
}
