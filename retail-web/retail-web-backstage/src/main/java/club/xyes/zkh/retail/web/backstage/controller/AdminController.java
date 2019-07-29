package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.entity.Admin;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.ParamChecker;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.service.general.AdminService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.web.commons.vo.AdminLoginCookie;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * Create by 郭文梁 2019/6/20 0020 13:51
 * AdminController
 * 管理员相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/6/20 0020
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController extends AbstractEntityController<Admin> {
    private final AdminService adminService;
    private final AccessTokenEncoder accessTokenEncoder;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    public AdminController(AdminService service, AccessTokenEncoder accessTokenEncoder) {
        super(service);
        this.adminService = service;
        this.accessTokenEncoder = accessTokenEncoder;
    }

    /**
     * 查出所有管理员账户
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR
     */
    @GetMapping("/")
    public GeneralResult<PageInfo<Admin>> all(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);
        final PageInfo<Admin> res = adminService.list(page, rows);
        return GeneralResult.ok(res);
    }

    /**
     * 创建管理员账户
     *
     * @param param 参数
     * @return GR
     */
    @PostMapping("/")
    public GeneralResult<Admin> create(@RequestBody Admin param) {
        final Admin admin = new Admin();
        checkAndCopyParam(param, admin);
        final Admin res = adminService.create(admin);
        return GeneralResult.ok(res);
    }

    /**
     * 更新管理员账户信息
     *
     * @param id    ID
     * @param param 参数
     * @return GR
     */
    @PostMapping("/{id}")
    public GeneralResult<Admin> update(@PathVariable("id") Integer id,
                                       @RequestBody Admin param) {
        final Admin admin = adminService.require(id);
        checkAndCopyParam(param, admin);
        final Admin res = adminService.update(admin);
        return GeneralResult.ok(res);
    }

    /**
     * 检查并拷贝参数
     *
     * @param param  参数
     * @param target 拷贝目标
     */
    private void checkAndCopyParam(Admin param, Admin target) {
        final Class<BadRequestException> ex = BadRequestException.class;
        ParamChecker.notNull(param, ex, "参数未传");
        ParamChecker.notEmpty(param.getName(), ex, "登录名必填");
        ParamChecker.notEmpty(param.getPassword(), ex, "密码必填");
        target.setName(param.getName());
        target.setPassword(param.getPassword());
    }

    /**
     * 删除管理员账户
     *
     * @param id ID
     * @return GR
     */
    @DeleteMapping("/{id}")
    public GeneralResult<Admin> delete(@PathVariable("id") Integer id) {
        @NotNull final Admin admin = adminService.require(id);
        adminService.deleteById(id);
        return GeneralResult.ok(admin);
    }

    /**
     * 登出操作
     *
     * @param response 响应对象
     * @return GR
     */
    @PostMapping("/logout")
    public GeneralResult<Void> logout(HttpServletResponse response) {
        final Admin admin = requireCurrentAdmin(adminService);
        final AdminLoginCookie cookie = new AdminLoginCookie(admin, accessTokenEncoder);
        cookie.setMaxAge(0);
        cookie.write2Response(response);
        return GeneralResult.ok();
    }

    /**
     * 当前登录的管理员
     *
     * @return GR
     */
    @GetMapping("/me")
    public GeneralResult<Admin> me() {
        final Admin admin = requireCurrentAdmin(adminService);
        return GeneralResult.ok(admin);
    }
}
