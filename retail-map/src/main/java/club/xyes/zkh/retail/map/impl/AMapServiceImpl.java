package club.xyes.zkh.retail.map.impl;

import club.xyes.zkh.retail.commons.dto.CommonHttpResponse;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.utils.CommonHttpUtils;
import club.xyes.zkh.retail.commons.utils.TextUtils;
import club.xyes.zkh.retail.map.MapService;
import club.xyes.zkh.retail.map.dto.Location2AddressResult;
import club.xyes.zkh.retail.map.dto.LocationAddressConvertResult;
import club.xyes.zkh.retail.map.prop.MapConfigProp;
import club.xyes.zkh.retail.map.util.MapSignUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Create by 郭文梁 2019/7/17 9:11
 * AMapServiceImpl
 * 搞得地图服务实现
 *
 * @author 郭文梁
 * @data 2019/7/17 9:11
 */
@Component
@Slf4j
public class AMapServiceImpl implements MapService {
    private static final String LOCATION_TO_ADDRESS_API_URL = "https://restapi.amap.com/v3/geocode/regeo";
    private static final String SEARCH_RADIUS = "3000";
    private final MapConfigProp mapConfigProp;

    public AMapServiceImpl(MapConfigProp mapConfigProp) {
        this.mapConfigProp = mapConfigProp;
    }

    @Override
    public Location2AddressResult location2Address(String locations) {
        final Map<String, String> params = new HashMap<>(16);
        params.put("key", mapConfigProp.getKey());
        params.put("location", locations);
        params.put("radius", SEARCH_RADIUS);
        params.put("output", "JSON");
        params.put("sig", MapSignUtils.md5Sign(params, mapConfigProp.getKey()));
        try {
            CommonHttpResponse response = CommonHttpUtils.get(LOCATION_TO_ADDRESS_API_URL, params);
            if (response.getCode() != HttpStatus.SC_OK) {
                throw new InternalServerErrorException("Location address convert api request fail!" + response.getCode() + response.getMsg());
            }
            return JSON.parseObject(response.getBody(), Location2AddressResult.class);
        } catch (IOException e) {
            throw new InternalServerErrorException("Location convert fail, " + e.getMessage(), e);
        }
    }

    @Override
    public LocationAddressConvertResult location2AddressPretty(Double lon, Double lat) {
        val address = location2Address(lon.toString() + "," + lat.toString());
        if (!Objects.equals(address.getStatus(), 1)) {
            throw new InternalServerErrorException("Convert location to address error, api status=" + address.getStatus() + ",info=" + address.getInfo());
        }
        val addressComponent = address.getRegeocode().getAddressComponent();
        val country = addressComponent.getCountry();
        val province = addressComponent.getProvince();
        val city = addressComponent.getCity();
        val cityCode = addressComponent.getCitycode();
        val district = addressComponent.getDistrict();
        val districtCode = addressComponent.getAdcode();
        val detailAddress = address.getRegeocode().getFormatted_address();

        val res = new LocationAddressConvertResult();
        res.setCountry(country);
        res.setProvince(province);
        res.setCity((city == null || city.isEmpty()) ? null : city.get(0));
        res.setCityCode(cityCode);
        res.setDistrict(district);
        res.setDistrictCode(districtCode);
        res.setDetailAddress(detailAddress);
        res.setLon(lon);
        res.setLat(lat);

        if (TextUtils.isTrimedEmpty(res.getCity())
                && TextUtils.isTrimedNotEmpty(res.getCityCode())
                && TextUtils.isTrimedNotEmpty(res.getProvince())
                && TextUtils.isTrimedNotEmpty(res.getDistrictCode())) {
            res.setCity(res.getProvince());
        }
        return res;
    }
}
