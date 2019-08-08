package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.Feedback;
import club.xyes.zkh.retail.service.basic.AbstractService;
import com.github.pagehelper.PageInfo;

/**
 * Create By leven ont 2019/8/8 22:44
 * Class Name :[FeedbackService]
 * <p>
 * 反馈相关业务行为定义
 *
 * @author leven
 */
public interface FeedbackService extends AbstractService<Feedback> {
    /**
     * 所有反馈记录
     *
     * @param page 页码
     * @param rows 每页大小
     * @return PageInfo
     */
    PageInfo<Feedback> listFetchAll(Integer page, Integer rows);
}
