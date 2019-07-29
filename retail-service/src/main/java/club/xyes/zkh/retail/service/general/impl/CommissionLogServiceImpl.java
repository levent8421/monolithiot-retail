package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.entity.CommissionLog;
import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.repository.dao.mapper.CommissionLogMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.general.CommissionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by 郭文梁 2019/6/13 0013 18:02
 * CommissionLogServiceImpl
 * 返佣记录相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@Service
@Slf4j
public class CommissionLogServiceImpl extends AbstractServiceImpl<CommissionLog> implements CommissionLogService {
    private final CommissionLogMapper commissionLogMapper;

    public CommissionLogServiceImpl(CommissionLogMapper mapper) {
        super(mapper);
        this.commissionLogMapper = mapper;
    }

    @Override
    public void log(User promoter, Order order, Integer amount, int reason) {
        CommissionLog res = new CommissionLog();
        res.setOrderId(order.getId());
        res.setPromoterId(promoter.getId());
        res.setAmount(amount);
        res.setReason(reason);
        res.setStatus(CommissionLog.STATUS_SUCCESS);
        res = save(res);
        log.debug("Save Commission Log : [{}]", res);
    }

    @Override
    public List<CommissionLog> findByOrder(Order order) {
        CommissionLog query = new CommissionLog();
        query.setOrderId(order.getId());
        return findByQuery(query);
    }

    @Override
    public List<CommissionLog> findByOrderFetchAll(Integer orderId) {
        return commissionLogMapper.selectByOrderIdFetchAll(orderId);
    }
}
