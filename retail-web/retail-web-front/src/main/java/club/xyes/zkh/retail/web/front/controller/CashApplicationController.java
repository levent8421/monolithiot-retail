package club.xyes.zkh.retail.web.front.controller;

import club.xyes.zkh.retail.commons.entity.CashApplication;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.exception.ResourceNotFoundException;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.CashApplicationService;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.web.commons.vo.UserLoginCookie;
import club.xyes.zkh.retail.web.front.vo.WithdrawVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Create by 郭文梁 2019/5/25 0025 10:57
 * CashApplicationController
 * 提现申请相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/25 0025
 */
@RestController
@RequestMapping("/api/cash")
@Slf4j
public class CashApplicationController extends AbstractEntityController<CashApplication> {
    /**
     * 最低提现金额
     */
    private static final int MIN_WITHDRAW_AMOUNT = 50 * 100;
    private final CashApplicationService cashApplicationService;
    private final UserService userService;
    private final CashApplicationService.ApplicationCreateSuccessListener applicationCreateSuccessListener;

    /**
     * 构造时指定业务组件
     *
     * @param service     业务组件
     * @param userService 用户业务组件
     */
    protected CashApplicationController(CashApplicationService service,
                                        UserService userService,
                                        CashApplicationService.ApplicationCreateSuccessListener applicationCreateSuccessListener) {
        super(service);
        this.cashApplicationService = service;
        this.userService = userService;
        this.applicationCreateSuccessListener = applicationCreateSuccessListener;
    }

    /**
     * 发起提现申请
     *
     * @param param 参数
     * @return GR
     */
    @PostMapping("/create")
    public GeneralResult<CashApplication> create(@RequestBody WithdrawVo param) {
        if (param == null || param.getAmount() == null) {
            throw new BadRequestException("参数未传");
        }
        if (param.getAmount() < MIN_WITHDRAW_AMOUNT) {
            throw new BadRequestException("最少提现" + MIN_WITHDRAW_AMOUNT / 100 + "元");
        }
        User user = requireCurrentUser(userService);
        CashApplication res = cashApplicationService.create(user, param.getAmount(), applicationCreateSuccessListener);
        return GeneralResult.ok(res);
    }

    /**
     * 当前用户的最后一条提现记录
     *
     * @return GR
     */
    @GetMapping("/last")
    public GeneralResult<CashApplication> lastRecord() {
        final UserLoginCookie.UserInfo userInfo = requireUserInfo();
        CashApplication cashApplication = cashApplicationService.findLastByUser(userInfo.getUserId());
        if (cashApplication == null) {
            throw new ResourceNotFoundException("无提现记录");
        }
        return GeneralResult.ok(cashApplication);
    }

    /**
     * 通过状态查询当前用户的提现记录
     *
     * @param status 状态
     * @param page   页码
     * @param rows   每页大小
     * @return GR
     */
    @GetMapping("/status/{status}")
    public GeneralResult<PageInfo> byStatus(@PathVariable("status") Integer status,
                                            Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        final Integer userId = requireUserInfo().getUserId();
        final PageInfo<CashApplication> res = cashApplicationService.findByUserAndStatus(userId, status, page, rows);
        return GeneralResult.ok(res);
    }
}
