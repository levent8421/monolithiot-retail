package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.entity.Feedback;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.FeedbackService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import com.github.pagehelper.PageInfo;
import lombok.val;
import org.springframework.web.bind.annotation.*;

/**
 * Create By leven ont 2019/8/8 23:25
 * Class Name :[FeedBackController]
 * <p>
 * 反馈建议相关数据访问控制器
 *
 * @author leven
 */
@RestController
@RequestMapping("/api/feedback")
public class FeedBackController extends AbstractEntityController<Feedback> {
    private final FeedbackService feedbackService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    public FeedBackController(FeedbackService service) {
        super(service);
        this.feedbackService = service;
    }

    /**
     * 所有反馈信息
     *
     * @param page 页码
     * @param rows 每页大小
     * @return GR with PageInfo
     */
    @GetMapping("/")
    public GeneralResult<PageInfo<Feedback>> list(Integer page, Integer rows) {
        page = defaultPage(page);
        rows = defaultRows(rows);

        val res = feedbackService.listFetchAll(page, rows);
        return GeneralResult.ok(res);
    }

    /**
     * 删除反馈记录
     *
     * @param id ID
     * @return GR
     */
    @DeleteMapping("/{id}")
    public GeneralResult<Feedback> delete(@PathVariable("id") Integer id) {
        val feedback = feedbackService.require(id);
        feedbackService.removeById(id);
        return GeneralResult.ok(feedback);
    }
}
