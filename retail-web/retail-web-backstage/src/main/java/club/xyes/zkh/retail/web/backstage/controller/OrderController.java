package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.ParamChecker;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.CommissionLogService;
import club.xyes.zkh.retail.service.general.OrderService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import com.github.pagehelper.PageInfo;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by 郭文梁 2019/6/13 0013 09:48
 * OrderController
 * 订单相关数据访问控制器
 * md
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@RestController
@RequestMapping("/api/order")
public class OrderController extends AbstractEntityController<Order> {
    private final OrderService orderService;
    private final OrderService.OrderAppointmentListener orderAppointmentListener;
    private final CommissionLogService commissionLogService;
    private final OrderService.OrderCompleteListener orderCompleteListener;

    public OrderController(OrderService orderService,
                           OrderService.OrderAppointmentListener orderAppointmentListener,
                           CommissionLogService commissionLogService,
                           OrderService.OrderCompleteListener orderCompleteListener) {
        super(orderService);
        this.orderService = orderService;
        this.orderAppointmentListener = orderAppointmentListener;
        this.commissionLogService = commissionLogService;
        this.orderCompleteListener = orderCompleteListener;
    }

    /**
     * 查询某用户的订单列表
     *
     * @param userId 用户ID
     * @param page   页码
     * @param rows   每页大小
     * @return GR
     */
    @GetMapping("/user/{userId}")
    public GeneralResult<PageInfo<Order>> findByUser(@PathVariable("userId") Integer userId,
                                                     Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);

        final PageInfo<Order> orders = orderService.findByUserId(userId, page, rows);
        return GeneralResult.ok(orders);
    }

    /**
     * 取消预约
     *
     * @param id 订单ID
     * @return GR
     */
    @PostMapping("/{id}/cancel-appointment")
    public GeneralResult<Order> cancelAppointment(@PathVariable("id") Integer id) {
        @NotNull final Order order = orderService.require(id);
        Order res = orderService.cancelAppointment(order, orderAppointmentListener);
        return GeneralResult.ok(res);
    }

    /**
     * 查询所有订单
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR
     */
    @GetMapping("/")
    public GeneralResult<PageInfo<Order>> list(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);

        final PageInfo<Order> orderPageInfo = orderService.listFetchAll(page, rows);
        return GeneralResult.ok(orderPageInfo);
    }

    /**
     * 订单搜索
     *
     * @param query 搜索内容
     * @param page  页码
     * @param rows  每页大小
     * @return GR
     */
    @GetMapping("/_search")
    public GeneralResult<PageInfo<Order>> search(@RequestParam("q") String query,
                                                 Integer page, Integer rows) {
        ParamChecker.notEmpty(query, BadRequestException.class, "搜索内容必填");
        page = defaultPage(page);
        rows = defaultRows(rows);

        final PageInfo<Order> orderPageInfo = orderService.search(query, page, rows);
        return GeneralResult.ok(orderPageInfo);
    }

    /**
     * 查询订单的详细信息
     *
     * @param id 订单ID
     * @return GR
     */
    @GetMapping("/{id}/_detail")
    public GeneralResult<Map<String, Object>> detail(@PathVariable("id") Integer id) {
        val order = orderService.requireFetchAll(id);
        final Map<String, Object> res = new HashMap<>(16);
        val commissionLogList = commissionLogService.findByOrderFetchAll(order.getId());
        res.put("order", order);
        res.put("commission", commissionLogList);
        return GeneralResult.ok(res);
    }

    /**
     * 核销订单
     *
     * @param id ID
     * @return GR
     */
    @PostMapping("/{id}/complete")
    public GeneralResult<Order> complete(@PathVariable("id") Integer id) {
        val order = orderService.require(id);
        val res = orderService.complete(order, orderCompleteListener);
        return GeneralResult.ok(res);
    }
}
