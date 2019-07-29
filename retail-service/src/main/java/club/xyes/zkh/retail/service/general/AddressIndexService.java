package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.AddressIndex;
import club.xyes.zkh.retail.service.basic.AbstractService;

/**
 * Create by 郭文梁 2019/7/17 15:10
 * AddressIndexService
 * 地区索引相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/7/17 15:10
 */
public interface AddressIndexService extends AbstractService<AddressIndex> {
    /**
     * 创建地区索引
     *
     * @param addressIndex 地区索引参数
     * @return AddressIndex result
     */
    AddressIndex create(AddressIndex addressIndex);

    /**
     * 匹配索引地区
     *
     * @param lon 经度
     * @param lat 纬度
     * @return 地址
     */
    AddressIndex match(Double lon, Double lat);
}
