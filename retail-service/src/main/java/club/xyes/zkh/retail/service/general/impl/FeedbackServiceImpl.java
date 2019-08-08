package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.entity.Feedback;
import club.xyes.zkh.retail.repository.dao.mapper.FeedbackMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.general.FeedbackService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * Create By leven ont 2019/8/8 22:46
 * Class Name :[FeedbackServiceImpl]
 * <p>
 * 反馈相关业务行为定义
 *
 * @author leven
 */
@Service
public class FeedbackServiceImpl extends AbstractServiceImpl<Feedback> implements FeedbackService {
    private final FeedbackMapper feedbackMapper;

    public FeedbackServiceImpl(FeedbackMapper mapper) {
        super(mapper);
        this.feedbackMapper = mapper;
    }

    @Override
    public PageInfo<Feedback> listFetchAll(Integer page, Integer rows) {
        return PageHelper.startPage(page, rows).doSelectPageInfo(feedbackMapper::selectFetchAll);
    }
}
