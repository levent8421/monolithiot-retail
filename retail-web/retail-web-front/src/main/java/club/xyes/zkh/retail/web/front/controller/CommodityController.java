package club.xyes.zkh.retail.web.front.controller;

import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.ParamChecker;
import club.xyes.zkh.retail.commons.vo.CommodityHtmlVo;
import club.xyes.zkh.retail.commons.vo.CommodityWithDistance;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.config.StaticConfigProp;
import club.xyes.zkh.retail.service.general.CommodityService;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by 郭文梁 2019/5/20 0020 11:17
 * CommodityController
 * 商品相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/20 0020
 */
@RestController
@RequestMapping("/api/commodity")
@Slf4j
public class CommodityController extends AbstractEntityController<Commodity> {
    private static final String SHARE_IMAGE_VIEW_NAME = "share-image";
    private final CommodityService commodityService;
    private final StaticConfigProp staticConfigProp;
    private final UserService userService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected CommodityController(CommodityService service,
                                  StaticConfigProp staticConfigProp,
                                  UserService userService) {
        super(service);
        this.commodityService = service;
        this.staticConfigProp = staticConfigProp;
        this.userService = userService;
    }

    /**
     * 分页获取全部商品
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR with Commodity list
     */
    @GetMapping("/all")
    public GeneralResult<PageInfo<Commodity>> all(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        PageInfo<Commodity> res = commodityService.list(page, rows);
        return GeneralResult.ok(prettyStaticPath(res));
    }

    /**
     * 获取商品详细信息
     *
     * @return GR
     */
    @GetMapping("/{id}")
    public GeneralResult<Commodity> detail(@PathVariable("id") Integer id) {
        @NotNull Commodity commodity = commodityService.requireFetchAll(id);
        prettyStaticPath(commodity);
        return GeneralResult.ok(commodity);
    }

    /**
     * 处理静态资源路径
     *
     * @param commodity 商品对象
     */
    private void prettyStaticPath(Commodity commodity) {
        String staticServer = staticConfigProp.getStaticServer();
        String commodityImagePath = staticConfigProp.getCommodityImagePath();
        commodity.parseImageList();
        commodity.setImageList(
                commodity.getImageList() == null ? null :
                        commodity.getImageList()
                                .stream()
                                .map(file -> staticServer + commodityImagePath + file)
                                .collect(Collectors.toList()));
    }

    /**
     * 处理静态资源路径
     *
     * @param commodities 商品对象列表
     * @return 处理结果
     */
    private <T extends Commodity> List<T> prettyStaticPath(List<T> commodities) {
        return commodities == null ? null :
                commodities.stream()
                        .peek(this::prettyStaticPath)
                        .collect(Collectors.toList());
    }

    /**
     * 处理静态资源路径
     *
     * @param commodityPageInfo 商品分页对象
     * @return 分页对象
     */
    private <T extends Commodity> PageInfo<T> prettyStaticPath(PageInfo<T> commodityPageInfo) {
        if (commodityPageInfo == null) {
            return null;
        }
        commodityPageInfo.setList(prettyStaticPath(commodityPageInfo.getList()));
        return commodityPageInfo;
    }

    /**
     * 查看善品的详情HTML
     *
     * @param id 商品ID
     * @return GR
     * @throws IOException IO异常
     */
    @GetMapping("/{id}/html")
    public GeneralResult<CommodityHtmlVo> detailHtml(@PathVariable("id") Integer id) throws IOException {
        CommodityHtmlVo res = commodityService.detailHtml(id);
        return GeneralResult.ok(res);
    }

    /**
     * 通过名称搜索商品
     *
     * @param name 搜索名称
     * @param page 页码
     * @param rows 每页大小
     * @return GR
     */
    @GetMapping("/search")
    public GeneralResult<PageInfo<Commodity>> search(@RequestParam("name") String name,
                                                     Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        ParamChecker.notEmpty(name, BadRequestException.class, "请输入搜索内容");
        PageInfo<Commodity> commodities = commodityService.searchByName(name, page, rows);
        return GeneralResult.ok(commodities);
    }

    /**
     * 通过地区索引查找商品
     *
     * @param addressIndexId 地区索引ID
     * @param page           页码
     * @param rows           每页大小
     * @return GR with PageInfo
     */
    @GetMapping("/by-address")
    public GeneralResult<PageInfo<Commodity>> findByAddressIndex(@RequestParam("addressIndexId") Integer addressIndexId,
                                                                 Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        val commodityPageInfo = commodityService.findByAddressIndex(addressIndexId, page, rows);
        return GeneralResult.ok(prettyStaticPath(commodityPageInfo));
    }

    /**
     * 查询附近的商品
     *
     * @param lon   经度
     * @param lat   纬度
     * @param range 查询方位 单位米
     * @param page  页码
     * @param rows  每页大小
     * @return GR
     */
    @GetMapping("/by-distance")
    public GeneralResult<PageInfo<CommodityWithDistance>> findByDistance(@RequestParam("lon") Double lon,
                                                                         @RequestParam("lat") Double lat,
                                                                         @RequestParam("range") Long range,
                                                                         Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        val res = commodityService.findByDistance(lon, lat, range, page, rows);
        return GeneralResult.ok(prettyStaticPath(res));
    }
}
