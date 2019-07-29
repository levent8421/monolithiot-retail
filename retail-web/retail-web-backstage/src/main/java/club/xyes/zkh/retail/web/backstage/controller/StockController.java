package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.CommodityService;
import club.xyes.zkh.retail.service.general.StockService;
import club.xyes.zkh.retail.service.general.StoreService;
import club.xyes.zkh.retail.web.backstage.vo.CreateStockParam;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static club.xyes.zkh.retail.commons.utils.ParamChecker.notEmpty;
import static club.xyes.zkh.retail.commons.utils.ParamChecker.notNull;

/**
 * Create by 郭文梁 2019/6/12 0012 15:50
 * StockController
 * 库存相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
@RestController
@RequestMapping("/api/stock")
public class StockController extends AbstractEntityController<Stock> {
    private final StockService stockService;
    private final StoreService storeService;
    private final CommodityService commodityService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected StockController(StockService service,
                              StoreService storeService,
                              CommodityService commodityService) {
        super(service);
        this.stockService = service;
        this.storeService = storeService;
        this.commodityService = commodityService;
    }

    /**
     * 批量创建库存
     *
     * @param storeId     商铺ID
     * @param commodityId 商品ID
     * @param param       创建库存的参数
     * @return GR
     */
    @PostMapping("/store/{storeId}/{commodityId}")
    public GeneralResult<List<Stock>> create(@PathVariable("storeId") Integer storeId,
                                             @PathVariable("commodityId") Integer commodityId,
                                             @RequestBody CreateStockParam param) {
        notNull(storeId, BadRequestException.class, "商铺ID必填");
        notNull(commodityId, BadRequestException.class, "商品ID必填");
        checkCreateParam(param);
        @NotNull final Store store = storeService.require(storeId);
        @NotNull final Commodity commodity = commodityService.require(commodityId);
        List<Stock> res = stockService.create(store, commodity,
                param.getStartDate(), param.getEndDate(), param.getTimeRangeList(), param.getDaysOfWeek(), param.getStockCount());
        return GeneralResult.ok(res);
    }

    /**
     * 检查创建参数
     *
     * @param param 参数
     */
    private void checkCreateParam(CreateStockParam param) {
        final Class<BadRequestException> ex = BadRequestException.class;
        notNull(param, ex, "参数未传");
        notNull(param.getStartDate(), ex, "开始日期必填");
        notNull(param.getEndDate(), ex, "结束日期必填");
        notNull(param.getStockCount(), ex, "库存必填");
        notEmpty(param.getTimeRangeList(), ex, "时间区间为空");
        notEmpty(param.getDaysOfWeek(), ex, "星期为空");
        if (param.getTimeRangeList().stream()
                .anyMatch(range -> (range == null) || (range.getStartTime() == null) || (range.getEndTime() == null))) {
            throw new BadRequestException("时间区间内存在空值");
        }
        if (param.getDaysOfWeek().stream().anyMatch(Objects::isNull)) {
            throw new BadRequestException("星期列表存在空值");
        }
    }

    /**
     * 查询商铺 商品的库存
     *
     * @param storeId     商铺ID
     * @param commodityId 商品ID
     * @param page        页码
     * @param rows        每页大小
     * @return GR
     */
    @GetMapping("/store/{storeId}/{commodityId}")
    public GeneralResult<PageInfo<Stock>> findByStoreAndCommodity(@PathVariable("storeId") Integer storeId,
                                                                  @PathVariable("commodityId") Integer commodityId,
                                                                  Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        PageInfo<Stock> stockPageInfo = stockService.findByStoreAndCommodity(storeId, commodityId, page, rows);
        return GeneralResult.ok(stockPageInfo);
    }

    /**
     * 更新库存信息
     *
     * @param id    库存ID
     * @param param 更新参数
     * @return GR
     */
    @PostMapping("/{id}")
    public GeneralResult<Stock> update(@PathVariable("id") Integer id, @RequestBody Stock param) {
        @NotNull final Stock stock = stockService.require(id);
        checkAndCopy(param, stock);
        final Stock res = stockService.updateById(stock);
        return GeneralResult.ok(res);
    }

    /**
     * 检查并复制参数
     *
     * @param param 参数
     * @param stock 库存
     */
    private void checkAndCopy(Stock param, Stock stock) {
        final Class<BadRequestException> ex = BadRequestException.class;
        notNull(param, ex, "参数未传");
        notNull(param.getStartTime(), ex, "开始时间必填");
        notNull(param.getEndTime(), ex, "结束时间必填");
        notNull(param.getStockCount(), ex, "库存必填");
        stock.setStartTime(param.getStartTime());
        stock.setEndTime(param.getEndTime());
        stock.setStockCount(param.getStockCount());
    }
}
