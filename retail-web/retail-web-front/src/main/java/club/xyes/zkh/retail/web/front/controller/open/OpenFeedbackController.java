package club.xyes.zkh.retail.web.front.controller.open;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.Feedback;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.service.general.FeedbackService;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.web.commons.vo.UserLoginCookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * Create by 郭文梁 2019/8/9 10:04
 * OpenFeedbackController
 * 反馈相关开放数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/8/9 10:04
 */
@Slf4j
@RestController
@RequestMapping("/api/open/feedback")
public class OpenFeedbackController extends AbstractEntityController<Feedback> {
    private static final String FEEDBACK_VIEW_NAME = "redirect:" + ApplicationConstants.BASE_URL + "/index.html#/feedback";
    private final FeedbackService feedbackService;
    private final UserService userService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    public OpenFeedbackController(FeedbackService service, UserService userService) {
        super(service);
        this.feedbackService = service;
        this.userService = userService;
    }

    /**
     * 登陆后跳转到反馈页面
     *
     * @param code     code
     * @param state    state
     * @param response 响应对象
     * @return MV
     */
    @GetMapping("/wx-login")
    public ModelAndView wxLogin(@RequestParam("code") String code,
                                @RequestParam("state") String state,
                                HttpServletResponse response) {
        log.info("Login for feedback, code=[{}],state=[{}]", code, state);
        User user = userService.loginByOAuthCode(code);
        new UserLoginCookie(user).write2Response(response);
        return new ModelAndView(FEEDBACK_VIEW_NAME);
    }
}
