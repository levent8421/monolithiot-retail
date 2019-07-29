package club.xyes.zkh.retail.web.backstage.controller.open;

import club.xyes.zkh.retail.commons.entity.Admin;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.ParamChecker;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.service.general.AdminService;
import club.xyes.zkh.retail.web.commons.vo.AdminLoginCookie;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Create by 郭文梁 2019/6/20 0020 13:20
 * OpenAdminController
 * 开放访问权限的管理员账户控制器
 *
 * @author 郭文梁
 * @data 2019/6/20 0020
 */
@RestController
@RequestMapping("/api/open/admin")
public class OpenAdminController extends AbstractEntityController<Admin> {
    private final AdminService adminService;
    private final AccessTokenEncoder accessTokenEncoder;

    public OpenAdminController(AdminService adminService, AccessTokenEncoder accessTokenEncoder) {
        super(adminService);
        this.adminService = adminService;
        this.accessTokenEncoder = accessTokenEncoder;
    }

    /**
     * 管理员登录
     *
     * @param param    登录参数
     * @param response 响应对象
     * @return GR
     */
    @PostMapping("/login")
    public GeneralResult<Admin> login(@RequestBody Admin param, HttpServletResponse response) {
        final Class<BadRequestException> ex = BadRequestException.class;
        ParamChecker.notNull(param, ex, "参数未传");
        ParamChecker.notEmpty(param.getName(), ex, "登录名必填");
        ParamChecker.notEmpty(param.getPassword(), ex, "密码必填");
        Admin admin = adminService.login(param.getName(), param.getPassword());
        final AdminLoginCookie cookie = new AdminLoginCookie(admin, accessTokenEncoder);
        cookie.write2Response(response);
        return GeneralResult.ok(admin);
    }
}
