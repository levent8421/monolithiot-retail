package club.xyes.zkh.retail.web.front.controller.open;

import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.map.MapService;
import club.xyes.zkh.retail.map.dto.Location2AddressResult;
import club.xyes.zkh.retail.web.commons.controller.AbstractController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by 郭文梁 2019/7/17 10:00
 * OpenLocationController
 * 开放访问的地区访问控制器
 *
 * @author 郭文梁
 * @data 2019/7/17 10:00
 */
@RestController
@RequestMapping("/open/location")
public class OpenLocationController extends AbstractController {
    private final MapService mapService;

    public OpenLocationController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * 坐标地址转换
     *
     * @param locations 坐标
     * @return GR
     */
    @GetMapping("/l2a")
    public GeneralResult<Location2AddressResult> location2Address(@RequestParam("locations") String locations) {
        Location2AddressResult result = mapService.location2Address(locations);
        return GeneralResult.ok(result);
    }
}
