package club.xyes.zkh.retail.repository.dao.mapper;

import club.xyes.zkh.retail.commons.entity.CommissionLog;
import club.xyes.zkh.retail.repository.dao.AbstractMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Create by 郭文梁 2019/6/13 0013 17:56
 * CommissionLogMapper
 * 返佣记录相关数据库访问组件
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@Repository
public interface CommissionLogMapper extends AbstractMapper<CommissionLog> {
    /**
     * 通过订单ID查询返现记录 同时抓取出关联信息
     *
     * @param orderId 订单ID
     * @return 返现记录
     */
    List<CommissionLog> selectByOrderIdFetchAll(@Param("orderId") Integer orderId);
}
