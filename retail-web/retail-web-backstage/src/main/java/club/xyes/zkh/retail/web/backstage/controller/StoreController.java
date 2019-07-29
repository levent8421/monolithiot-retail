package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.ParamChecker;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.AddressIndexService;
import club.xyes.zkh.retail.service.general.StoreService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import com.github.pagehelper.PageInfo;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import static club.xyes.zkh.retail.commons.utils.ParamChecker.notEmpty;
import static club.xyes.zkh.retail.commons.utils.ParamChecker.notNull;

/**
 * Create by 郭文梁 2019/5/29 0029 13:03
 * StoreController
 * 商铺相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/29 0029
 */
@RestController
@RequestMapping("/api/store")
public class StoreController extends AbstractEntityController<Store> {
    private final StoreService storeService;
    private final AddressIndexService addressIndexService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected StoreController(StoreService service, AddressIndexService addressIndexService) {
        super(service);
        this.storeService = service;
        this.addressIndexService = addressIndexService;
    }

    /**
     * 分页查询所有商铺
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR with PageInfo
     */
    @GetMapping("/all")
    public GeneralResult<PageInfo<Store>> all(Integer page,
                                              Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        PageInfo<Store> storePageInfo = storeService.listFetchAll(page, rows);
        return GeneralResult.ok(storePageInfo);
    }

    /**
     * 创建新的商铺信息
     *
     * @param param 商铺参数
     * @return GR
     */
    @PostMapping("/create")
    public GeneralResult<Store> create(@RequestBody Store param) {
        Store store = new Store();
        checkAndCopy(param, store);
        Store res = storeService.create(store);
        return GeneralResult.ok(res);
    }

    /**
     * 检查并拷贝请求参数
     *
     * @param param  参数
     * @param target 拷贝目标
     */
    private void checkAndCopy(Store param, Store target) {
        Class<BadRequestException> exKls = BadRequestException.class;
        notNull(param, exKls, "参数未传");
        notEmpty(param.getName(), exKls, "名称未传");
        notEmpty(param.getKeeperName(), exKls, "管理者信命必填");
        notEmpty(param.getPhone(), exKls, "电话必填");
        notEmpty(param.getProvince(), exKls, "省必填");
        notEmpty(param.getCity(), exKls, "市必填");
        notEmpty(param.getRegion(), exKls, "区必填");
        notEmpty(param.getAddress(), exKls, "详细地址必填");
        notEmpty(param.getLoginName(), exKls, "登录名必填");
        notEmpty(param.getPassword(), exKls, "密码必填");
        target.setName(param.getName());
        target.setKeeperName(param.getKeeperName());
        target.setPhone(param.getPhone());
        target.setProvince(param.getProvince());
        target.setCity(param.getCity());
        target.setRegion(param.getRegion());
        target.setAddress(param.getAddress());
        target.setLoginName(param.getLoginName());
        target.setPassword(param.getPassword());
        target.setLongitude(param.getLongitude());
        target.setLatitude(param.getLatitude());
    }

    /**
     * 设置店铺的地区索引
     *
     * @param id    店铺ID
     * @param param 参数
     * @return GR
     */
    @PostMapping("/{id}/set-address-index")
    public GeneralResult<Store> setAddressIndex(@PathVariable("id") Integer id,
                                                @RequestBody Store param) {
        ParamChecker.notNull(param, BadRequestException.class, "参数未传！");
        ParamChecker.notNull(param.getAddressIndexId(), BadRequestException.class, "索引ID必填！");
        val indexId = param.getAddressIndexId();
        val index = addressIndexService.require(indexId);
        val store = storeService.require(id);

        storeService.setAddressIndex(store, index);
        return GeneralResult.ok(store);
    }

    /**
     * 设置商铺的排序号
     *
     * @param id    id
     * @param param param
     * @return GR
     */
    @PostMapping("/{id}/set-order-num")
    public GeneralResult<Store> setOrderNum(@PathVariable("id") Integer id,
                                            @RequestBody Store param) {
        ParamChecker.notNull(param, BadRequestException.class, "参数未传！");
        ParamChecker.notNull(param.getOrderNum(), BadRequestException.class, "排序号必填！");
        val store = storeService.require(id);
        storeService.setOrderNum(store, param.getOrderNum());

        return GeneralResult.ok(store);
    }

    /**
     * 设置定位
     *
     * @param id    商铺ID
     * @param param 参数
     * @return GR
     */
    @PostMapping("/{id}/set-position")
    public GeneralResult<Store> setPosition(@PathVariable("id") Integer id,
                                            @RequestBody Store param) {
        val ex = BadRequestException.class;
        ParamChecker.notNull(param, ex, "参数未传！");
        ParamChecker.notNull(param.getLongitude(), ex, "经度未传！");
        ParamChecker.notNull(param.getLatitude(), ex, "纬度未传！");

        val store = storeService.require(id);
        store.setLongitude(param.getLongitude());
        store.setLatitude(param.getLatitude());
        val res = storeService.updateById(store);
        return GeneralResult.ok(res);
    }
}
