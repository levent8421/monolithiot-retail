package club.xyes.zkh.retail.web.front.controller;

import club.xyes.zkh.retail.commons.dto.CountAndAmount;
import club.xyes.zkh.retail.commons.entity.*;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.CommissionUtil;
import club.xyes.zkh.retail.commons.utils.OrderUtils;
import club.xyes.zkh.retail.commons.utils.TextUtils;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.config.StaticConfigProp;
import club.xyes.zkh.retail.service.general.*;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.web.front.vo.AppointDateParam;
import club.xyes.zkh.retail.web.front.vo.OrderCreateVo;
import club.xyes.zkh.retail.wechat.dto.WxPayParams;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static club.xyes.zkh.retail.commons.utils.ParamChecker.notEmpty;
import static club.xyes.zkh.retail.commons.utils.ParamChecker.notNull;

/**
 * Create by 郭文梁 2019/5/22 0022 17:05
 * OrderController
 * 订单相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/22 0022
 */
@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController extends AbstractEntityController<Order> {
    private final UserService userService;
    private final OrderService orderService;
    private final CommodityService commodityService;
    private final OrderService.PaySuccessListener paySuccessListener;
    private final StaticConfigProp staticConfigProp;
    private final StoreService storeService;
    private final OrderService.OrderCompleteListener orderCompleteListener;
    private final OrderService.OrderAppointmentListener orderAppointmentListener;
    private final StockService stockService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected OrderController(OrderService service,
                              UserService userService,
                              CommodityService commodityService,
                              OrderService.PaySuccessListener paySuccessListener,
                              StaticConfigProp staticConfigProp,
                              StoreService storeService,
                              OrderService.OrderCompleteListener orderCompleteListener,
                              OrderService.OrderAppointmentListener orderAppointmentListener,
                              StockService stockService) {
        super(service);
        this.orderService = service;
        this.userService = userService;
        this.commodityService = commodityService;
        this.paySuccessListener = paySuccessListener;
        this.staticConfigProp = staticConfigProp;
        this.storeService = storeService;
        this.orderCompleteListener = orderCompleteListener;
        this.orderAppointmentListener = orderAppointmentListener;
        this.stockService = stockService;
    }

    /**
     * 查询当前用户的订单记录
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR with Order PageInfo Object
     */
    @GetMapping("/my/all")
    public GeneralResult<PageInfo<Order>> findByCurrentUser(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        Integer userId = requireUserInfo().getUserId();
        PageInfo<Order> orders = orderService.findByUserId(userId, page, rows);
        prettyStaticPath(orders);
        return GeneralResult.ok(orders);
    }

    /**
     * 通过状态查询当前用户的订单
     *
     * @param status 状态
     * @param page   页码
     * @param rows   每页大小
     * @return Gr with Order PageInfo
     */
    @GetMapping("/my/status/{status}")
    public GeneralResult<PageInfo<Order>> findByCurrentUserAndStatus(@PathVariable("status") Integer status,
                                                                     Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        Integer userId = requireUserInfo().getUserId();
        PageInfo<Order> orders = orderService.findByUserIdAndStatus(userId, status, page, rows);
        prettyStaticPath(orders);
        return GeneralResult.ok(orders);
    }

    /**
     * 创建订单
     *
     * @param param 参数
     * @return GR with order info
     */
    @PostMapping("/create")
    public GeneralResult<Order> create(@RequestBody OrderCreateVo param) {
        notNull(param, BadRequestException.class, "参数未传");
        notNull(param.getCommodityId(), BadRequestException.class, "商品ID必填");
        notEmpty(param.getPhone(), BadRequestException.class, "电话必填");
        notNull(param.getQuantity(), BadRequestException.class, "购买数量必填");
        notEmpty(param.getUsername(), BadRequestException.class, "姓名必填");
        notEmpty(param.getAddress(), BadRequestException.class, "收货地址必填！");
        @NotNull Commodity commodity = commodityService.require(param.getCommodityId());
        @NotNull User user = requireCurrentUser(userService);
        Order order = createOrder(user, commodity, param);
        Order res = orderService.create(order);
        prettyStaticPath(order);

        //记录用户订单姓名
        user.setName(param.getUsername());
        user.setPhone(param.getPhone());
        userService.updateById(user);
        log.debug("Save order username {}", user.getName());
        return GeneralResult.ok(res);
    }

    /**
     * 创建一个携带基本信息的订单对象
     *
     * @param user      用户
     * @param commodity 商品
     * @param param     其他参数
     * @return 订单对象
     */
    private Order createOrder(User user, Commodity commodity, OrderCreateVo param) {
        if (commodity.getStockCount() <= 0) {
            throw new BadRequestException("该商品已售完");
        }
        Order order = new Order();
        order.setCommodityId(commodity.getId());
        order.setCommodity(commodity);
        order.setUserId(user.getId());
        order.setUser(user);
        order.setQuantity(param.getQuantity());
        order.setUsername(param.getUsername());
        order.setPhone(param.getPhone());
        //商铺ID 冗余存储
        order.setStoreId(commodity.getStoreId());
        order.setAddress(param.getAddress());

        //处理推广关系
        if (!TextUtils.isTrimedEmpty(param.getPromoCode())) {
            //推广码存在
            User promoter = userService.findByPromoCode(param.getPromoCode());
            if (promoter != null) {
                //推广用户存在
                order.setPromoter(promoter);
                order.setPromoterId(promoter.getId());
            }
        } else {
            //推广码没传
            if (CommissionUtil.canPromot(user)) {
                //用户拥有推广权限 则推手就是他自己
                order.setPromoterId(user.getId());
                order.setPromoter(user);
            } else if (user.getPromoterId() != null) {
                //用户没有推广权限 但是原来绑定过推手 则继续使用原来的推手信息
                order.setPromoterId(user.getPromoterId());
                order.setPromoter(userService.require(user.getPromoterId()));
            }
        }

        if (order.getPromoterId() == null) {
            log.warn("创建订单时没有指定推广码，用户也没有推广权限，也没有推手推广过该用户！user=[{}],param=[{}]", user.getId(), param);
        } else {
            log.info("订单创建成功，推手为[{},{}]", order.getPromoterId(), order.getPromoter().getNickname());
        }
        prettyStaticPath(order);
        return order;
    }

    /**
     * 查看（刷新）订单状态
     *
     * @param id 订单ID
     * @return 订单信息
     */
    @GetMapping("/{id}/status")
    public GeneralResult<Order> getStatus(@PathVariable("id") Integer id) {
        Order order = orderService.refreshStatus(id, paySuccessListener);
        return GeneralResult.ok(order);
    }

    /**
     * 订单预约
     *
     * @param id    订单ID
     * @param param 预约参数
     * @return GR with Order
     */
    @PostMapping("/{id}/appoint")
    public GeneralResult<Order> appoint(@PathVariable("id") Integer id,
                                        @RequestBody Order param) {
        notNull(param, BadRequestException.class, "参数未传");
        notNull(param.getAppointmentStockId(), BadRequestException.class, "请选择一个时段");
        @NotNull Order order = orderService.require(id);
        if (!OrderUtils.canAppointment(order)) {
            throw new BadRequestException("订单不可预约");
        }
        @NotNull final Stock stock = stockService.require(param.getAppointmentStockId());
        Order res = orderService.appoint(order, stock, orderAppointmentListener);
        return GeneralResult.ok(res);
    }

    /**
     * 扫码核销订单
     *
     * @param id 订单ID
     * @return GR
     */
    @PostMapping("/{id}/complete")
    public GeneralResult<Order> complete(@PathVariable("id") Integer id) {
        @NotNull Order order = orderService.require(id);
        @NotNull final Store store = requireCurrentStore(storeService);
        checkStorePermission(store, order);
        Order res = orderService.complete(order, orderCompleteListener);
        return GeneralResult.ok(res);
    }

    /**
     * 检查订单是否为当前商铺的订单
     *
     * @param store 商铺对象
     * @param order 订单
     */
    private void checkStorePermission(Store store, Order order) {
        if (!Objects.equals(store.getId(), order.getStoreId())) {
            throw new BadRequestException("该订单不是您店铺的订单！");
        }
    }

    /**
     * 替换静态路径
     *
     * @param order 订单
     */
    private void prettyStaticPath(Order order) {
        String server = staticConfigProp.getStaticServer();
        String path = staticConfigProp.getOrderQrCodePath();
        String file = order.getSn();
        order.setQrCodeUrl(server + path + file);
    }

    /**
     * 优化静态路径
     *
     * @param orders 订单列表
     * @return 替换后的结果
     */
    private List<Order> prettyStaticPath(List<Order> orders) {
        if (orders == null || orders.size() <= 0) {
            return orders;
        }
        return orders.stream().peek(this::prettyStaticPath).collect(Collectors.toList());
    }

    /**
     * 优化静态路径
     *
     * @param orderPageInfo 订单PageInfo对象
     */
    private void prettyStaticPath(PageInfo<Order> orderPageInfo) {
        orderPageInfo.setList(prettyStaticPath(orderPageInfo.getList()));
    }

    /**
     * 分析商家的销售数据
     *
     * @param storeId 商铺ID
     * @return GR
     */
    @GetMapping("/store/{storeId}/analysis")
    public GeneralResult<Map<String, CountAndAmount>> analysisNyStore(@PathVariable("storeId") Integer storeId) {
        Map<String, CountAndAmount> res = orderService.analysisByStore(storeId);
        return GeneralResult.ok(res);
    }

    /**
     * 订单支付
     *
     * @param id 订单ID
     * @return 支付结果
     */
    @PostMapping("/{id}/pay")
    public GeneralResult<Map<String, Object>> pay(@PathVariable("id") Integer id) {
        @NotNull Order order = orderService.require(id);
        @NotNull User user = userService.require(order.getUserId());
        Map<String, Object> res = new HashMap<>(2);
        WxPayParams params = orderService.pay(order, user);
        res.put("order", order);
        res.put("wxPay", params);
        return GeneralResult.ok(res);
    }

    /**
     * 查询当前用户的推广订单
     *
     * @return 推广订单
     */
    @GetMapping("/my/promotions")
    public GeneralResult<PageInfo<Order>> myPromotions(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        @NotNull final User user = requireCurrentUser(userService);
        PageInfo<Order> orderPageInfo = orderService.findByPromoter(user.getId(), page, rows);
        return GeneralResult.ok(orderPageInfo);
    }

    /**
     * 通过SN查看订单详情
     *
     * @param sn SN
     * @return GR with Order
     */
    @GetMapping("/{sn}/detail")
    public GeneralResult<Order> detailBySn(@PathVariable("sn") String sn) {
        Order order = orderService.requireBySn(sn);
        @NotNull Store store = requireCurrentStore(storeService);
        if (!Objects.equals(order.getStoreId(), store.getId())) {
            throw new BadRequestException("您无权查看该订单");
        }
        return GeneralResult.ok(order);
    }

    /**
     * 查询当前商铺的订单
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR
     */
    @GetMapping("/current-store")
    public GeneralResult<PageInfo<Order>> currentStore(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);

        @NotNull final Store store = requireCurrentStore(storeService);
        PageInfo<Order> orders = orderService.findByStore(store, page, rows);
        return GeneralResult.ok(orders);
    }

    /**
     * 增加线下消费金额
     *
     * @param id    订单ID
     * @param param 参数
     * @return GR
     */
    @PostMapping("/{id}/add-offline-amount")
    public GeneralResult<Order> addOfflinePaymentAmount(@PathVariable("id") Integer id,
                                                        @RequestBody Order param) {
        notNull(param, BadRequestException.class, "参数未传");
        notNull(param.getOfflinePaymentAmount(), BadRequestException.class, "金额必填");
        @NotNull final Order order = orderService.require(id);
        checkStorePermission(requireCurrentStore(storeService), order);
        Order res = orderService.addOfflinePaymentAmount(order, param.getOfflinePaymentAmount());
        return GeneralResult.ok(res);
    }

    /**
     * 获取明天的预约订单
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR
     */
    @GetMapping("/store/tomorrow")
    public GeneralResult<PageInfo<Order>> tomorrowOrderByStore(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        final Integer storeId = requireStoreLoginCookie().getStoreInfo().getStoreId();
        final PageInfo<Order> orderPageInfo = orderService.findTomorrowOrdersByStore(storeId, page, rows);
        return GeneralResult.ok(orderPageInfo);
    }

    /**
     * 查询店铺某天的订单预约情况
     *
     * @param param 店铺
     * @param page  页码
     * @param rows  每页大小
     * @return GR
     */
    @GetMapping("/store/date")
    public GeneralResult<PageInfo<Order>> findByStoreAndAppointDate(AppointDateParam param,
                                                                    Integer page, Integer rows) {
        rows = defaultRows(rows);
        page = defaultPage(page);
        notNull(param, BadRequestException.class, "参数未传！");
        notNull(param.getDate(), BadRequestException.class, "日期必填！");
        val storeId = requireStoreLoginCookie().getStoreInfo().getStoreId();
        val orderPageInfo = orderService.findByStoreAndDate(storeId, param.getDate(), page, rows);
        return GeneralResult.ok(orderPageInfo);
    }
}
