package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.entity.DailyLogin;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.DateTimeUtils;
import club.xyes.zkh.retail.repository.dao.mapper.DailyLoginMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.general.DailyLoginService;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Create By leven ont 2019/7/29 22:43
 * Class Name :[DailyLoginServiceImpl]
 * <p>
 * 登录记录相关业务行为定义
 *
 * @author leven
 */
@Service
public class DailyLoginServiceImpl extends AbstractServiceImpl<DailyLogin> implements DailyLoginService {
    private final DailyLoginMapper dailyLoginMapper;

    public DailyLoginServiceImpl(DailyLoginMapper mapper) {
        super(mapper);
        this.dailyLoginMapper = mapper;
    }

    @Override
    public DailyLogin checkIn(User user) {
        val now = DateTimeUtils.now();
        if (signInToday(user.getId(), now)) {
            throw new BadRequestException("今日已登录过");
        }
        if (user.getDailyLoginTimes() == null) {
            user.setDailyLoginTimes(0);
        }
        if (signInYesterday(user.getId(), now)) {
            user.setDailyLoginTimes(user.getDailyLoginTimes() + 1);
        } else {
            user.setDailyLoginTimes(1);
        }

        val res = new DailyLogin();
        res.setLoginDate(now);
        res.setUser(user);
        res.setUserId(user.getId());
        return save(res);
    }

    @Override
    public List<DailyLogin> findCurrentMonthLogForUser(Integer userId) {
        val range = DateTimeUtils.currentMonthRange();
        return dailyLoginMapper.selectByUserAndDateRange(userId, range.getStart(), range.getEnd());
    }

    /**
     * 查看用户昨天有没有签到
     *
     * @param userId 用户ID
     * @param now    日期
     * @return 是否签到
     */
    private boolean signInYesterday(Integer userId, Date now) {
        val nowCalendar = DateTimeUtils.calendar(now);
        DateTimeUtils.cleanTime(nowCalendar);
        nowCalendar.add(Calendar.DAY_OF_YEAR, -1);
        val yesterday = nowCalendar.getTime();
        val count = dailyLoginMapper.countByUserAndDate(userId, yesterday);
        return count > 0;
    }

    /**
     * 判断用户今天是否登陆过
     *
     * @param userId 用户ID
     * @param now    当前时间
     * @return 是否登陆过
     */
    private boolean signInToday(Integer userId, Date now) {
        val calendar = DateTimeUtils.calendar(now);
        DateTimeUtils.cleanTime(calendar);
        val today = calendar.getTime();
        val count = dailyLoginMapper.countByUserAndDate(userId, today);
        return count > 0;
    }
}
