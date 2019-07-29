package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.dto.CountAndAmount;
import club.xyes.zkh.retail.commons.entity.*;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.exception.ResourceNotFoundException;
import club.xyes.zkh.retail.commons.utils.DateTimeUtils;
import club.xyes.zkh.retail.commons.utils.OrderUtils;
import club.xyes.zkh.retail.commons.utils.QrCodeUtil;
import club.xyes.zkh.retail.commons.utils.RandomUtils;
import club.xyes.zkh.retail.repository.dao.mapper.OrderMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.config.StaticConfigProp;
import club.xyes.zkh.retail.service.general.OrderService;
import club.xyes.zkh.retail.wechat.api.Wechat;
import club.xyes.zkh.retail.wechat.dto.WxPayNotifyVo;
import club.xyes.zkh.retail.wechat.dto.WxPayParams;
import club.xyes.zkh.retail.wechat.dto.WxPayResult;
import club.xyes.zkh.retail.wechat.dto.WxTradeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/22 0022 16:53
 * OrderServiceImpl
 * 订单相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/5/22 0022
 */
@Service
@Slf4j
public class OrderServiceImpl extends AbstractServiceImpl<Order> implements OrderService {
    /**
     * 电子码后缀长度 3 = 极限环境下 每毫秒允许 1000 (10^3)条订单
     */
    private static final int SN_SUFFIX_LENGTH = 3;
    /**
     * 电子码时间前缀长度3 即每110天出现一次重复的时间字符串前缀
     */
    private static final int SN_TIME_LEN = 10;
    /**
     * 二维码的宽高度
     */
    private static final int QR_CODE_WIDTH = 500;
    private static final int QR_CODE_HEIGHT = 500;
    private final OrderMapper orderMapper;
    private final Wechat wechat;
    private final StaticConfigProp staticConfigProp;

    public OrderServiceImpl(OrderMapper mapper,
                            Wechat wechat,
                            StaticConfigProp staticConfigPropg) {
        super(mapper);
        this.orderMapper = mapper;
        this.wechat = wechat;
        this.staticConfigProp = staticConfigPropg;
    }

    @Override
    public PageInfo<Order> findByUserId(Integer userId, Integer page, Integer rows) {
        return PageHelper.startPage(page, rows).doSelectPageInfo(() -> orderMapper.selectByUserId(userId));
    }

    @Override
    public PageInfo<Order> findByUserIdAndStatus(Integer userId, Integer status, Integer page, Integer rows) {
        return PageHelper.startPage(page, rows).doSelectPageInfo(() -> orderMapper.selectByUserIdAndStatus(userId, status));
    }

    @Override
    public Order create(Order order) {
        checkCreateParam(order, order.getCommodity());
        snapshot(order.getCommodity(), order);
        identify(order);
        calculatePrice(order);
        order.setStatus(Order.STATUS_CREATE);
        Order res = save(order);
        try {
            createQrCode(order);
        } catch (IOException e) {
            throw new InternalServerErrorException("Error on create qrcode for order!", e);
        }
        return res;
    }

    /**
     * 检查创建订单的参数
     *
     * @param order     订单
     * @param commodity 商品
     */
    private void checkCreateParam(Order order, Commodity commodity) {
        final Date now = DateTimeUtils.now();

        if (commodity.getBuyEndTime() != null && DateTimeUtils.compareDate(now, commodity.getBuyEndTime()) > 0) {
            //说明当前时间已经超出购买日期
            throw new BadRequestException("该商品已超时下架！");
        }
        if (order.getQuantity() > commodity.getBuyLimit()) {
            throw new BadRequestException("每人限购" + commodity.getBuyLimit() + "个");
        }
    }

    @Override
    public Order refreshStatus(Integer id, PaySuccessListener listener) {
        @NotNull Order order = require(id);
        if (OrderUtils.isPaid(order)) {
            //若已支付成功 不再执行后续流程
            return order;
        }
        WxTradeInfo wxTradeInfo = wechat.queryTrade(order);
        log.info("Query trade [{}] for order {}", wxTradeInfo, order);
        if (wxTradeInfo.isPaid()) {
            if (listener != null) {
                listener.onPaySuccess(order);
            }
        }
        return order;
    }

    @Override
    public Order appoint(Order order, Stock stock, OrderAppointmentListener listener) {
        checkAppointmentParam(order, stock);
        listener.onAppointment(order, stock);
        order.setAppointmentStockId(stock.getId());
        order.setStatus(Order.STATUS_BOOKED);
        return updateById(order);
    }

    /**
     * 检查预约参数
     *
     * @param order 订单
     * @param stock 库存
     */
    private void checkAppointmentParam(Order order, Stock stock) {
        if (!Objects.equals(order.getStoreId(), stock.getStoreId())) {
            throw new BadRequestException("时段与商家不匹配");
        }
        if (!Objects.equals(order.getCommodityId(), stock.getCommodityId())) {
            throw new BadRequestException("时段与商品不匹配");
        }
        final Date now = DateTimeUtils.now();
        final long dateOffset = DateTimeUtils.compareDate(now, stock.getActionDate());
        if (dateOffset > 0) {
            throw new BadRequestException("只能预约今天或今天以后的时间");
        }
        if (dateOffset == 0 && DateTimeUtils.compareTime(stock.getEndTime(), now) <= 0) {
            throw new BadRequestException("该时段已经结束");
        }
    }

    @Override
    public Order complete(Order order, OrderCompleteListener listener) {
        OrderUtils.check4Complete(order);
        final Date now = DateTimeUtils.now();
        listener.beforeComplete(order, now);
        order.setStatus(Order.STATUS_COMPLETE);
        order.setCompleteTime(now);
        final Order res = updateById(order);
        listener.afterComplete(res);
        return res;
    }


    @Override
    public Map<String, CountAndAmount> analysisByStore(Integer storeId) {
        Date now = DateTimeUtils.now();
        CountAndAmount sales = orderMapper.totalSalesAndAmount(storeId);
        CountAndAmount todaySales = orderMapper.salesAndAmountByDate(storeId, now);
        CountAndAmount complete = orderMapper.totalCompleteCountAndAmount(storeId);
        CountAndAmount todayComplete = orderMapper.completeCountAndAmountByDate(storeId, now);

        Map<String, CountAndAmount> res = new HashMap<>(4);
        res.put("sales", sales);
        res.put("todaySales", todaySales);
        res.put("complete", complete);
        res.put("todayComplete", todayComplete);
        return res;
    }

    @Override
    public WxPayParams pay(Order order, User user) {
        if (!OrderUtils.canPay(order)) {
            throw new BadRequestException("订单不允许支付");
        }
        final WxPayResult result = wechat.pay(order, user);
        return buildWxPayParams(result.getPrepayId());
    }

    /**
     * 生成前端支付参数
     *
     * @param prepayId 预下单ID
     * @return 支付参数
     */
    private WxPayParams buildWxPayParams(String prepayId) {
        return wechat.toJsPayParams(prepayId);
    }

    /**
     * 计算价格
     *
     * @param order 订单对象
     */
    private void calculatePrice(Order order) {
        Commodity commodity = order.getCommodity();
        Integer price = commodity.getCurrentPrice();
        Integer quantity = order.getQuantity();
        int amount = price * quantity;
        order.setAmount(amount);
    }

    /**
     * 为订单生成唯一标识
     *
     * @param order 订单对象
     */
    private void identify(Order order) {
        String sn = RandomUtils.currentTimeMillisWithIncrement(SN_TIME_LEN, SN_SUFFIX_LENGTH);
        String tradeNo = RandomUtils.randomPrettyUUIDString();
        order.setSn(sn);
        order.setTradeNo(tradeNo);
    }

    /**
     * 商品信息快照
     *
     * @param commodity 商品对象
     * @param order     订单对象
     */
    private void snapshot(Commodity commodity, Order order) {
        order.setCommodityName(commodity.getName());
        order.setCommodityDescription(commodity.getDescription());
    }

    /**
     * 为订单生成二维码
     *
     * @param order 订单对象
     * @throws IOException IO异常
     */
    private void createQrCode(Order order) throws IOException {
        String sn = order.getSn();
        String path = staticConfigProp.getStaticFilePath();
        String qrCodePath = staticConfigProp.getOrderQrCodePath();
        String filePath = path + qrCodePath;
        File pathFile = new File(filePath);
        if (!pathFile.exists()) {
            if (!pathFile.mkdirs()) {
                throw new IOException("Error on create qrcode parent path!");
            }
        }

        File file = new File(pathFile, sn);
        try (FileOutputStream out = new FileOutputStream(file)) {
            QrCodeUtil.createQrCode(sn, QR_CODE_WIDTH, QR_CODE_HEIGHT, out);
            out.flush();
        }
    }

    @Override
    public Order resolveTradeNotify(WxPayNotifyVo notifyVo, PaySuccessListener listener) {
        @NotNull Order order = findByTradeNo(notifyVo.getOutTradeNo());
        if (OrderUtils.isPaid(order)) {
            //若状态为已支付 则不在继续执行下面逻辑
            return order;
        }
        if (!notifyVo.isPaid()) {
            throw new BadRequestException("Order not paid!");
        }
        checkNotifyAmount(order, notifyVo);
        if (listener != null) {
            listener.onPaySuccess(order);
        }
        return order;
    }

    @Override
    public PageInfo<Order> findByPromoter(Integer promoterId, Integer page, Integer rows) {
        return PageHelper.startPage(page, rows).doSelectPageInfo(() -> orderMapper.selectByPromoterId(promoterId));
    }

    /**
     * 检查试剂支付金额
     *
     * @param order    订单
     * @param notifyVo 通知对象
     */
    private void checkNotifyAmount(Order order, WxPayNotifyVo notifyVo) {
        int fee = Integer.parseInt(notifyVo.getTotalFee());
        if (!Objects.equals(order.getAmount(), fee)) {
            throw new BadRequestException("Amount of order invalidate!");
        }
    }

    /**
     * t通过订单号查询订单
     *
     * @param tradeNo 订单号
     * @return 订单
     */
    @NotNull
    private Order findByTradeNo(String tradeNo) {
        Order query = new Order();
        query.setTradeNo(tradeNo);
        Order order = findOneByQuery(query);
        if (order == null) {
            throw new ResourceNotFoundException("Order for tradeNo[" + tradeNo + "] could not be found!");
        }
        return order;
    }

    @Override
    public @NotNull Order requireBySn(String sn) {
        Order query = new Order();
        query.setSn(sn);
        Order order = findOneByQuery(query);
        if (order == null) {
            throw new ResourceNotFoundException("Could not find Order for sn [" + sn + "]");
        }
        return order;
    }

    @Override
    public PageInfo<Order> findByStore(Store store, Integer page, Integer rows) {
        return PageHelper.startPage(page, rows, "o_create_time desc")
                .doSelectPageInfo(() -> orderMapper.selectByStoreId(store.getId()));
    }

    @Override
    public Order addOfflinePaymentAmount(Order order, Integer offlinePaymentAmount) {
        Integer amount;
        if (order.getOfflinePaymentAmount() != null) {
            amount = order.getOfflinePaymentAmount() + offlinePaymentAmount;
        } else {
            amount = offlinePaymentAmount;
        }
        order.setOfflinePaymentAmount(amount);
        return updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order cancelAppointment(Order order, OrderAppointmentListener listener) {
        if (!Objects.equals(order.getStatus(), Order.STATUS_BOOKED)) {
            throw new BadRequestException("订单未预约，不能取消预约！");
        }
        listener.onCancelAppointment(order);
        order.setStatus(Order.STATUS_NEED_BOOKED);
        order.setAppointmentStockId(null);
        return updateById(order);
    }

    @Override
    public PageInfo<Order> findTomorrowOrdersByStore(Integer storeId, Integer page, Integer rows) {
        return PageHelper.startPage(page, rows)
                .doSelectPageInfo(() -> orderMapper.selectTomorrowAppointOrdersByStore(storeId));
    }

    @Override
    public PageInfo<Order> findByStoreAndDate(Integer storeId, Date date, Integer page, Integer rows) {
        return PageHelper.startPage(page, rows)
                .doSelectPageInfo(() -> orderMapper.selectByStoreAndDate(storeId, date));
    }

    @Override
    public PageInfo<Order> listFetchAll(Integer page, Integer rows) {
        return PageHelper.startPage(page, rows).doSelectPageInfo(orderMapper::selectFetchAll);
    }

    @Override
    public PageInfo<Order> search(String query, Integer page, Integer rows) {
        final String queryString = "%" + query + "%";
        return PageHelper.startPage(page, rows).doSelectPageInfo(() -> orderMapper.search(queryString));
    }

    @Override
    public Order requireFetchAll(Integer id) {
        val order = orderMapper.selectByIdFetchAll(id);
        if (order == null) {
            throw new ResourceNotFoundException(Order.class, id);
        }
        return order;
    }
}
