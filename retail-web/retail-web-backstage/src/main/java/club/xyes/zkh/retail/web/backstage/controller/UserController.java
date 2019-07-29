package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.ParamChecker;
import club.xyes.zkh.retail.commons.utils.TextUtils;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Create by 郭文梁 2019/5/30 0030 09:14
 * UserController
 * 用户相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/30 0030
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends AbstractEntityController<User> {
    private final UserService userService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected UserController(UserService service) {
        super(service);
        this.userService = service;
    }

    /**
     * 查询所有用户
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR
     */
    @GetMapping("/all")
    public GeneralResult<PageInfo<User>> all(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        PageInfo<User> userPageInfo = userService.list(page, rows);
        return GeneralResult.ok(userPageInfo);
    }

    /**
     * 设置用户为推广者
     *
     * @param id 用户ID
     * @return GR with user
     */
    @PostMapping("/{id}/to-promoter")
    public GeneralResult<User> toPromoter(@PathVariable("id") Integer id, @RequestBody User param) {
        @NotNull User user = userService.require(id);
        checkAndCopyModifyRoleParam(param, user);
        User res = userService.toPromoter(user);
        return GeneralResult.ok(res);
    }

    /**
     * 设置为队长
     *
     * @param id 用户ID
     * @return GR with user
     */
    @PostMapping("/{id}/to-captain")
    public GeneralResult<User> toCaptain(@PathVariable("id") Integer id, @RequestBody User param) {
        @NotNull User user = userService.require(id);
        checkAndCopyModifyRoleParam(param, user);
        User res = userService.toCaptain(user);
        return GeneralResult.ok(res);
    }

    /**
     * 检查并拷贝修改角色的参数
     *
     * @param param 参数
     * @param user  拷贝目标
     */
    private void checkAndCopyModifyRoleParam(User param, User user) {
        final Class<BadRequestException> ex = BadRequestException.class;
        ParamChecker.notNull(param, ex, "参数未传");
        ParamChecker.notEmpty(param.getPromoterPhone(), ex, "电话号码必填！");
        ParamChecker.notEmpty(param.getPromoterWxNo(), ex, "微信号必填！");
        ParamChecker.notEmpty(param.getPromoterName(), ex, "姓名必填！");
        user.setPromoterName(param.getPromoterName());
        user.setPromoterPhone(param.getPromoterPhone());
        user.setPromoterWxNo(param.getPromoterWxNo());
    }

    /**
     * 设置用户为普通用户
     *
     * @param id 用户ID
     * @return GR
     */
    @PostMapping("/{id}/to-user")
    public GeneralResult<User> toUser(@PathVariable("id") Integer id) {
        @NotNull User user = userService.require(id);
        User res = userService.toPrimaryUser(user);
        return GeneralResult.ok(res);
    }

    /**
     * 通过用户名或昵称搜索用户
     *
     * @param name 搜索字段
     * @return GR
     */
    @GetMapping("/search")
    public GeneralResult<List<User>> searchByName(@RequestParam("name") String name) {
        if (TextUtils.isTrimedEmpty(name)) {
            throw new BadRequestException("未指定搜索条件");
        }
        List<User> users = userService.searchByName(name);
        return GeneralResult.ok(users);
    }
}
