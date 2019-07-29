package club.xyes.zkh.retail.web.front.controller;

import club.xyes.zkh.retail.commons.entity.DailyLogin;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.DailyLoginService;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create By leven ont 2019/7/29 22:45
 * Class Name :[DailyLoginController]
 * <p>
 * 登录记录相关数据访问控制器
 *
 * @author leven
 */
@RestController
@RequestMapping("/daily-login")
public class DailyLoginController extends AbstractEntityController<DailyLogin> {
    private final DailyLoginService dailyLoginService;
    private final UserService userService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    public DailyLoginController(DailyLoginService service, UserService userService) {
        super(service);
        this.dailyLoginService = service;
        this.userService = userService;
    }

    /**
     * 每日签到接口
     *
     * @return GR
     */
    @PostMapping("/checkIn")
    public GeneralResult<DailyLogin> checkIn() {
        val user = requireCurrentUser(userService);
        userService.updateById(user);
        val dailyLogin = dailyLoginService.checkIn(user);
        return GeneralResult.ok(dailyLogin);
    }

    /**
     * 查询用户当月的登录记录
     *
     * @param userId 用户ID
     * @return GR with list
     */
    @GetMapping("/user/{userId}")
    public GeneralResult<List<DailyLogin>> currentMonthLoginLogForUser(@PathVariable("userId") Integer userId) {
        val res = dailyLoginService.findCurrentMonthLogForUser(userId);
        return GeneralResult.ok(res);
    }
}
