package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.entity.Feedback;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create By leven ont 2019/8/8 22:41
 * Class Name :[FeedbackMapper]
 * <p>
 * 反馈相关数据库访问组件
 *
 * @author leven
 */
@Repository
public interface FeedbackMapper extends AbstractMapper<Feedback> {
    /**
     * 查询反馈记录 同时抓取出所有关联信息
     *
     * @return List
     */
    List<Feedback> selectFetchAll();
}
