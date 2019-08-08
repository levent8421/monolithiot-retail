package club.xyes.zkh.retail.web.front.controller;

import club.xyes.zkh.retail.commons.entity.Feedback;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.FeedbackService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import lombok.val;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static club.xyes.zkh.retail.commons.utils.ParamChecker.notEmpty;
import static club.xyes.zkh.retail.commons.utils.ParamChecker.notNull;

/**
 * Create By leven ont 2019/8/8 22:47
 * Class Name :[FeedbackController]
 * <p>
 * 反馈相关数据访问控制器
 *
 * @author leven
 */
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController extends AbstractEntityController<Feedback> {
    private final FeedbackService feedbackService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    public FeedbackController(FeedbackService service) {
        super(service);
        this.feedbackService = service;
    }

    /**
     * 创建反馈建议
     *
     * @param param 参数
     * @return GR
     */
    @PutMapping("/")
    public GeneralResult<Feedback> create(@RequestBody Feedback param) {
        val feedback = new Feedback();
        checkAndCopyCreateParam(param, feedback);
        val userId = requireUserInfo().getUserId();
        feedback.setUserId(userId);
        val res = feedbackService.save(feedback);
        return GeneralResult.ok(res);
    }

    /**
     * 检查并拷贝创建参数
     *
     * @param param  参数
     * @param target 拷贝目标
     */
    private void checkAndCopyCreateParam(Feedback param, Feedback target) {
        val ex = BadRequestException.class;
        notNull(param, ex, "参数未传！");
        notEmpty(param.getUsername(), ex, "姓名必填！");
        notEmpty(param.getContent(), ex, "反馈内容必填！");

        target.setUsername(param.getUsername());
        target.setPhone(param.getPhone());
        target.setContent(param.getContent());
    }
}
