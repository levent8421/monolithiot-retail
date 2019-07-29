package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.entity.AddressIndex;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.TextUtils;
import club.xyes.zkh.retail.map.MapService;
import club.xyes.zkh.retail.repository.dao.mapper.AddressIndexMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.general.AddressIndexService;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Create by 郭文梁 2019/7/17 15:11
 * AddressIndexServiceImpl
 * 地区索引相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/7/17 15:11
 */
@Service
public class AddressIndexServiceImpl extends AbstractServiceImpl<AddressIndex> implements AddressIndexService {
    private static final int DEFAULT_INDEX_ID = 1;
    private final AddressIndexMapper addressIndexMapper;
    private final MapService mapService;

    public AddressIndexServiceImpl(AddressIndexMapper mapper,
                                   MapService mapService) {
        super(mapper);
        this.addressIndexMapper = mapper;
        this.mapService = mapService;
    }

    @Override
    public AddressIndex create(AddressIndex addressIndex) {
        val existsAddressIndex = findByCityCodeAndDistrictCode(addressIndex.getCityCode(), addressIndex.getDistrictCode());
        if (existsAddressIndex != null) {
            throw new BadRequestException("地区[" + existsAddressIndex.getCity() + "|" + existsAddressIndex.getCityCode()
                    + "]市,[" + existsAddressIndex.getDistrict() + "|" + existsAddressIndex.getDistrictCode() + "]区已存在！");
        }
        return save(addressIndex);
    }

    /**
     * 通过市编码和地区编码查询索引
     *
     * @param cityCode     市编码
     * @param districtCode 地区编码
     * @return AddressIndex
     */
    private AddressIndex findByCityCodeAndDistrictCode(String cityCode, String districtCode) {
        return addressIndexMapper.selectByCityCodeAndDistrictCode(cityCode, districtCode);
    }

    @Override
    public AddressIndex match(Double lon, Double lat) {
        val address = mapService.location2AddressPretty(lon, lat);
        val cityCode = address.getCityCode();
        val districtCode = address.getDistrictCode();

        val res = addressIndexMapper.selectLikeCityCodeAndDistrictCode(cityCode, districtCode);
        if (res == null || res.isEmpty()) {
            return defaultIndex();
        }
        if (res.size() == 1) {
            return res.get(0);
        }
        val allMatchData = res.stream().filter(addressIndex -> Objects.equals(addressIndex.getCityCode(), cityCode) && Objects.equals(addressIndex.getDistrictCode(), districtCode)).collect(Collectors.toList());
        if (allMatchData.size() >= 1) {
            return allMatchData.get(0);
        }
        val onlyCityMatchData = res.stream().filter(addressIndex -> Objects.equals(addressIndex.getCityCode(), cityCode) && TextUtils.isTrimedEmpty(addressIndex.getDistrictCode())).collect(Collectors.toList());
        if (onlyCityMatchData.size() >= 1) {
            return onlyCityMatchData.get(0);
        }
        return defaultIndex();
    }

    /**
     * 获取未匹配的默认索引
     *
     * @return Index
     */
    @NotNull
    private AddressIndex defaultIndex() {
        return require(DEFAULT_INDEX_ID);
    }
}
