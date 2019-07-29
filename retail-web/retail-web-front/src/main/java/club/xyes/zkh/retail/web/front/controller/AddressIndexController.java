package club.xyes.zkh.retail.web.front.controller;

import club.xyes.zkh.retail.commons.entity.AddressIndex;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.AddressIndexService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by 郭文梁 2019/7/17 15:53
 * AddressIndexController
 * 地区索引相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/7/17 15:53
 */
@RestController
@RequestMapping("/api/address-index")
public class AddressIndexController extends AbstractEntityController<AddressIndex> {
    private final AddressIndexService addressIndexService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    public AddressIndexController(AddressIndexService service) {
        super(service);
        this.addressIndexService = service;
    }

    /**
     * 匹配地区索引
     *
     * @param lon 经度
     * @param lat 纬度
     * @return GR
     */
    @GetMapping("/match")
    public GeneralResult<AddressIndex> matchIndex(@RequestParam("lon") Double lon,
                                                  @RequestParam("lat") Double lat) {
        val addressIndex = addressIndexService.match(lon, lat);
        return GeneralResult.ok(addressIndex);
    }

    /**
     * 获取全部地区
     *
     * @return GR
     */
    @GetMapping("/all")
    public GeneralResult<Map<String, List<AddressIndex>>> all() {
        List<AddressIndex> all = addressIndexService.all();
        Map<String, List<AddressIndex>> res = new HashMap<>(16);
        all.forEach(ai -> {
            val districts = res.computeIfAbsent(ai.getCity(), key -> new ArrayList<>());
            districts.add(ai);
        });
        return GeneralResult.ok(res);
    }
}
