package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.entity.Stock;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.utils.DateTimeUtils;
import club.xyes.zkh.retail.commons.vo.TimeRange;
import club.xyes.zkh.retail.repository.dao.mapper.StockMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.general.StockService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Create by 郭文梁 2019/6/12 0012 15:46
 * StockServiceImpl
 * 库存相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/6/12 0012
 */
@Service
@Slf4j
public class StockServiceImpl extends AbstractServiceImpl<Stock> implements StockService {
    private final StockMapper stockMapper;

    public StockServiceImpl(StockMapper mapper) {
        super(mapper);
        this.stockMapper = mapper;
    }

    @Override
    public List<Stock> create(Store store,
                              Commodity commodity,
                              Date startDate,
                              Date endDate,
                              List<TimeRange> timeRangeList,
                              List<Integer> daysOfWeek,
                              Integer stockCount) {
        if (!commodity.getNeedAppointment()) {
            throw new BadRequestException("该商品无需预约！");
        }
        if (!DateTimeUtils.between(startDate, commodity.getAppointmentStartTime(), commodity.getAppointmentEndTime())
                ||
                !DateTimeUtils.between(endDate, commodity.getAppointmentStartTime(), commodity.getAppointmentEndTime())) {
            throw new BadRequestException("请选择预约日期[" + DateTimeUtils.format(commodity.getAppointmentStartTime(), ApplicationConstants.DATE_FORMAT)
                    + "到" + DateTimeUtils.format(commodity.getAppointmentEndTime(), ApplicationConstants.DATE_FORMAT) + "]内的日期");
        }
        List<List<Stock>> generatedList = new ArrayList<>();
        DateTimeUtils.forEachDate(startDate, endDate, 1, Calendar.DAY_OF_YEAR, date -> {
            final int dayOfWeek = DateTimeUtils.getDayOfWeek(date);
            if (daysOfWeek.contains(dayOfWeek)) {
                final List<Stock> stocks = createStocks(store, commodity, date, timeRangeList, dayOfWeek, stockCount);
                generatedList.add(stocks);
            }
            return null;
        });
        final List<Stock> stocks = generatedList.stream().flatMap(Collection::stream).collect(Collectors.toList());
        return save(stocks);
    }

    /**
     * 创建库存
     *
     * @param store      商铺
     * @param commodity  商品
     * @param date       日期
     * @param ranges     时间段列表
     * @param dayOfWeek  星期
     * @param stockCount 库存
     * @return StockList
     */
    private List<Stock> createStocks(Store store, Commodity commodity,
                                     Date date, List<TimeRange> ranges, int dayOfWeek, int stockCount) {
        return ranges.stream().map(range -> {
            final Date startTime = DateTimeUtils.cleanDate(range.getStartTime());
            final Date endTime = DateTimeUtils.cleanDate(range.getEndTime());
            final Stock stock = new Stock();
            stock.setActionDate(date);
            stock.setCommodityId(commodity.getId());
            stock.setStoreId(store.getId());
            stock.setDayOfWeek(dayOfWeek);
            stock.setStartTime(startTime);
            stock.setEndTime(endTime);
            stock.setStockCount(stockCount);
            stock.setBookedCount(0);
            stock.setCompleteCount(0);
            return stock;
        }).collect(Collectors.toList());
    }

    @Override
    public PageInfo<Stock> findByStoreAndCommodity(Integer storeId, Integer commodityId, Integer page, Integer rows) {
        return PageHelper.startPage(page, rows, "action_date desc")
                .doSelectPageInfo(() -> stockMapper.selectByStoreAndCommodity(storeId, commodityId));
    }

    @Override
    public void incrementCompleteCount(Stock stock) {
        int rows = stockMapper.incrementCompleteCount(stock.getId());
        if (rows != 1) {
            throw new InternalServerErrorException("Unable to update(increment) completeCount for Stock:" + stock);
        }
    }

    @Override
    public PageInfo<Stock> findAvailableByStoreAndCommodity(Integer storeId, Integer commodityId, Integer page, Integer rows) {
        final PageInfo<Stock> stockPageInfo = PageHelper.startPage(page, rows)
                .doSelectPageInfo(() -> stockMapper.selectAvailableByStoreAndCommodity(storeId, commodityId));
        final Date now = DateTimeUtils.now();
        final List<Stock> stocks = stockPageInfo.getList().stream().filter(stock -> {
            final Date actionDate = stock.getActionDate();
            if (DateTimeUtils.compareDate(actionDate, now) == 0) {
                return DateTimeUtils.compareTime(stock.getEndTime(), now) > 0;
            } else {
                return true;
            }
        }).collect(Collectors.toList());
        stockPageInfo.setList(stocks);
        return stockPageInfo;
    }
}
