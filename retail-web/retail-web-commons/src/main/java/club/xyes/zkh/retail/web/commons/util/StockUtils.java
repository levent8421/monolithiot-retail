package club.xyes.zkh.retail.web.commons.util;

import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.commons.utils.DateTimeUtils;
import club.xyes.zkh.retail.web.commons.vo.DateStock;
import com.github.pagehelper.PageInfo;

import java.util.*;

/**
 * Create by 郭文梁 2019/6/12 0012 18:07
 * StockUtils
 * 库存相关工具
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
public class StockUtils {
    /**
     * 转换为DateStock对象
     *
     * @param stocks 库存列表
     * @return dateStock List
     */
    public static List<DateStock> asDateStock(List<Stock> stocks) {
        Map<Date, List<Stock>> stockMap = new HashMap<>(30);
        stocks.forEach(stock -> {
            List<Stock> dataStocks = stockMap.computeIfAbsent(stock.getActionDate(), key -> new ArrayList<>());
            dataStocks.add(stock);
        });
        List<DateStock> res = new ArrayList<>();
        for (Map.Entry<Date, List<Stock>> entry : stockMap.entrySet()) {
            final DateStock dateStock = new DateStock();
            dateStock.setDate(entry.getKey());
            dateStock.setStocks(entry.getValue());
            res.add(dateStock);
        }
        res.sort((ds1, ds2) -> (int) (DateTimeUtils.compareDate(ds1.getDate(), ds2.getDate())));
        return res;
    }

    /**
     * 转换为DateStock的PageInfo
     *
     * @param stockPageInfo StockPageInfo
     * @return PageInfo
     */
    public static PageInfo<DateStock> asDateStock(PageInfo<Stock> stockPageInfo) {
        PageInfo<DateStock> res = new PageInfo<>();
        res.setTotal(stockPageInfo.getTotal());
        res.setList(asDateStock(stockPageInfo.getList()));
        return res;
    }
}
