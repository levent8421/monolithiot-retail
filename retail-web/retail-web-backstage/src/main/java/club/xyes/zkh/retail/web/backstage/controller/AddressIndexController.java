package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.entity.AddressIndex;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.AddressIndexService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import com.github.pagehelper.PageInfo;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import static club.xyes.zkh.retail.commons.utils.ParamChecker.notEmpty;
import static club.xyes.zkh.retail.commons.utils.ParamChecker.notNull;

/**
 * Create by 郭文梁 2019/7/17 15:13
 * AddressIndexController
 * 地区索引相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/7/17 15:13
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
     * 分页查询所有地区索引
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR with pageInfo
     */
    @GetMapping("/")
    public GeneralResult<PageInfo<AddressIndex>> list(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        val addressIndexPageInfo = addressIndexService.list(page, rows);
        return GeneralResult.ok(addressIndexPageInfo);
    }

    /**
     * 创建地区索引
     *
     * @param param 参数
     * @return GR
     */
    @PutMapping("/")
    public GeneralResult<AddressIndex> create(@RequestBody AddressIndex param) {
        val addressIndex = new AddressIndex();
        checkAndCopyCreateParam(param, addressIndex);
        val res = addressIndexService.create(addressIndex);
        return GeneralResult.ok(res);
    }

    /**
     * 检查并拷贝创建参数
     *
     * @param param  参数
     * @param target 拷贝目标
     */
    private void checkAndCopyCreateParam(AddressIndex param, AddressIndex target) {
        val ex = BadRequestException.class;
        notNull(param, ex, "参数未传!");
        notEmpty(param.getProvince(), ex, "省 必填！");
        notEmpty(param.getCity(), ex, "市 必填！");
        notEmpty(param.getCityCode(), ex, "市编码 必填！");

        target.setProvince(param.getProvince());
        target.setCity(param.getCity());
        target.setCityCode(param.getCityCode());
        target.setDistrict(param.getDistrict());
        target.setDistrictCode(param.getDistrictCode());
    }
}
