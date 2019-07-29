package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.map.MapService;
import club.xyes.zkh.retail.map.dto.LocationAddressConvertResult;
import club.xyes.zkh.retail.web.commons.controller.AbstractController;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by 郭文梁 2019/7/17 13:54
 * LocationController
 * 地址坐标相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/7/17 13:54
 */
@RestController
@RequestMapping("/api/location")
public class LocationController extends AbstractController {
    private final MapService mapService;

    public LocationController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * 经纬度和地址的转换
     *
     * @param lon 经度
     * @param lat 纬度
     * @return GR
     */
    @GetMapping("l2a")
    public GeneralResult<LocationAddressConvertResult> location2Address(@RequestParam("lon") Double lon,
                                                                        @RequestParam("lat") Double lat) {
        val res = mapService.location2AddressPretty(lon, lat);
        return GeneralResult.ok(res);
    }
}
