package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.entity.CashApplication;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.exception.WithdrawException;
import club.xyes.zkh.retail.commons.utils.RandomUtils;
import club.xyes.zkh.retail.commons.utils.WithdrawUtils;
import club.xyes.zkh.retail.repository.dao.mapper.CashApplicationMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.general.CashApplicationService;
import club.xyes.zkh.retail.service.listener.WithdrawListener;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/25 0025 10:50
 * CashApplicationServiceImpl
 * 提现申请相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/5/25 0025
 */
@Service
@Slf4j
public class CashApplicationServiceImpl extends AbstractServiceImpl<CashApplication> implements CashApplicationService {
    /**
     * 订单号长度
     */
    private static final int TRADE_NO_LEN = 28;
    private final CashApplicationMapper cashApplicationMapper;

    public CashApplicationServiceImpl(CashApplicationMapper mapper) {
        super(mapper);
        this.cashApplicationMapper = mapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CashApplication create(User user, Integer amount, ApplicationCreateSuccessListener listsner) {
        int withdrawableAmount = WithdrawUtils.getWithdrawableAmount(user);
        if (amount > withdrawableAmount) {
            throw new BadRequestException("输入金额必须小于可提现金额");
        }
        CashApplication application = new CashApplication();
        application.setUser(user);
        application.setUserId(user.getId());
        application.setAmount(amount);
        application.setStatus(CashApplication.STATUS_CREATE);
        application.setTradeNo(RandomUtils.randomPrettyUUIDString(TRADE_NO_LEN));
        CashApplication saveResult = save(application);
        if (listsner != null) {
            listsner.onApplicationCreateSuccess(saveResult);
        }
        return saveResult;
    }

    @Override
    public PageInfo<CashApplication> findByStatus(Integer status, Integer page, Integer rows) {
        return PageHelper.startPage(page, rows).doSelectPageInfo(() -> cashApplicationMapper.selectByStatusWithUser(status));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CashApplication withdraw(CashApplication application, ApplicationWithdrawSuccessListener listener) {
        try {
            listener.onWithdrawSuccess(application);
            application.setStatus(CashApplication.STATUS_COMPLETE);
            return updateById(application);
        } catch (WithdrawException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public CashApplication refuse(CashApplication application) {
        if (Objects.equals(application.getStatus(), CashApplication.STATUS_CREATE)) {
            application.setStatus(CashApplication.STATUS_REFUSE);
            return updateById(application);
        } else {
            throw new BadRequestException("申请状态异常，不允许拒绝申请");
        }
    }

    @Override
    public CashApplication refuse(CashApplication application, WithdrawListener withdrawListener) {
        CashApplication res = refuse(application);
        withdrawListener.onRefuse(res);
        return res;
    }

    @Override
    public CashApplication findLastByUser(Integer userId) {
        return cashApplicationMapper.selectLastOneByUserId(userId);
    }

    @Override
    public PageInfo<CashApplication> findByUserAndStatus(Integer userId, Integer status, Integer page, Integer rows) {
        final CashApplication query = new CashApplication();
        query.setUserId(userId);
        query.setStatus(status);
        return PageHelper.startPage(page, rows, "create_time desc").doSelectPageInfo(() -> cashApplicationMapper.select(query));
    }

    @Override
    public PageInfo<CashApplication> search(String search, int page, int rows) {
        final String searchStr = "%" + search + "%";
        return PageHelper.startPage(page, rows).doSelectPageInfo(() -> cashApplicationMapper.search(searchStr));
    }
}
