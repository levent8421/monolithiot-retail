package club.xyes.zkh.retail.web.front.controller;

import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.StockService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.web.commons.util.StockUtils;
import club.xyes.zkh.retail.web.commons.vo.DateStock;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by 郭文梁 2019/6/12 0012 18:02
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

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected StockController(StockService service) {
        super(service);
        this.stockService = service;
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
    public GeneralResult<PageInfo<DateStock>> findByStoreAndCommodity(@PathVariable("storeId") Integer storeId,
                                                                      @PathVariable("commodityId") Integer commodityId,
                                                                      Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        PageInfo<Stock> stockPageInfo = stockService.findAvailableByStoreAndCommodity(storeId, commodityId, page, rows);
        return GeneralResult.ok(StockUtils.asDateStock(stockPageInfo));
    }
}
