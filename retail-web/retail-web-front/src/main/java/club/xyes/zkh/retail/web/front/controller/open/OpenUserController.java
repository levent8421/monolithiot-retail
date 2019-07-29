package club.xyes.zkh.retail.web.front.controller.open;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.web.commons.vo.UserLoginCookie;
import club.xyes.zkh.retail.web.front.util.LoginCookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/24 0024 14:30
 * OpenUserController
 * 不带权限验证的用户数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/24 0024
 */
@RestController
@RequestMapping("/api/open/user")
@Slf4j
public class OpenUserController extends AbstractEntityController<User> {
    /**
     * 团队加入成功的页面名称
     */
    private static final String JOIN_SUCCESS_VIEW_NAME = "redirect:" + ApplicationConstants.BASE_URL + "/index.html#/join-team?leader=%s&leader_name=%s";
    private final UserService userService;
    private final AccessTokenEncoder accessTokenEncoder;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected OpenUserController(UserService service, AccessTokenEncoder accessTokenEncoder) {
        super(service);
        this.userService = service;
        this.accessTokenEncoder = accessTokenEncoder;
    }

    /**
     * 获取用户的登录令牌 仅供调试时使用
     *
     * @param id ID
     * @return GR with cookie
     */
    @GetMapping("/{id}/cookie")
    public GeneralResult<UserLoginCookie> cookie(@PathVariable("id") Integer id) {
        @NotNull User user = userService.require(id);
        UserLoginCookie cookie = new UserLoginCookie(user, accessTokenEncoder);
        return GeneralResult.ok(cookie);
    }

    /**
     * 获取用户的登录令牌 同时登录用户 仅供调试时使用
     *
     * @param id ID
     * @return GR with cookie
     */
    @GetMapping("/{id}/login")
    public GeneralResult<UserLoginCookie> login(@PathVariable("id") Integer id, HttpServletResponse response) {
        @NotNull User user = userService.require(id);
        UserLoginCookie cookie = new UserLoginCookie(user, accessTokenEncoder);
        cookie.write2Response(response);
        return GeneralResult.ok(cookie);
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return GR with 用户信息
     */
    @GetMapping("/me")
    public GeneralResult<User> me() {
        User user = requireCurrentUser(userService);
        return GeneralResult.ok(user);
    }

    /**
     * 加入团队
     *
     * @param id    团队所有者ID
     * @param code  oauth code
     * @param state state
     * @return MV
     */
    @GetMapping("/{id}/join")
    public ModelAndView preJoin(@PathVariable("id") Integer id,
                                @RequestParam("code") String code,
                                @RequestParam("state") String state,
                                HttpServletResponse response) throws UnsupportedEncodingException {
        log.debug("Login for Team Join, code[{}], state[{}]", code, state);
        @NotNull final User leader = userService.require(id);
        if (!(Objects.equals(leader.getRole(), User.ROLE_PROMOTERS) || Objects.equals(leader.getRole(), User.ROLE_CAPTAIN))) {
            throw new BadRequestException("无创建团队权限");
        }
        final User user = userService.loginByOAuthCode(code);
        if (Objects.equals(leader.getId(), user.getId())) {
            throw new BadRequestException("你不能加入自己的团队");
        }
        ModelAndView mv = new ModelAndView();
        final String viewName = String.format(JOIN_SUCCESS_VIEW_NAME, leader.getId(),
                URLEncoder.encode(leader.getPromoterName(), ApplicationConstants.DEFAULT_CHARSET));
        mv.setViewName(viewName);
        LoginCookieUtils.setLoginCookie(user, response, accessTokenEncoder);
        return mv;
    }
}
