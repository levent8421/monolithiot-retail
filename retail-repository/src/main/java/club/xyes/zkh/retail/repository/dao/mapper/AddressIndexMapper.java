package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.entity.AddressIndex;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by 郭文梁 2019/7/17 15:07
 * AddressIndexMapper
 * 地区索引相关数据库访问组件
 *
 * @author 郭文梁
 * @data 2019/7/17 15:07
 */
@Repository
public interface AddressIndexMapper extends AbstractMapper<AddressIndex> {
    /**
     * 通过市编码和地区编码查询索引
     *
     * @param cityCode     市编码
     * @param districtCode 地区编码
     * @return AddressIndex
     */
    AddressIndex selectByCityCodeAndDistrictCode(@Param("cityCode") String cityCode,
                                                 @Param("districtCode") String districtCode);

    /**
     * 查询与城市与地区相匹配的索引
     *
     * @param cityCode     市编码
     * @param districtCode 地区编码
     * @return AddressIndex
     */
    List<AddressIndex> selectLikeCityCodeAndDistrictCode(@Param("cityCode") String cityCode,
                                           @Param("districtCode") String districtCode);
}
