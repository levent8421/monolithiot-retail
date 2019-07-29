package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.DailyLogin;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.service.basic.AbstractService;

import java.util.List;

/**
 * Create By leven ont 2019/7/29 22:43
 * Class Name :[DailyLoginService]
 * <p>
 * 每日登录记录相关业务行为定义
 *
 * @author leven
 */
public interface DailyLoginService extends AbstractService<DailyLogin> {
    /**
     * 用户签到
     *
     * @param user 用户
     * @return 每日签到记录
     */
    DailyLogin checkIn(User user);

    /**
     * 获取用户当月的登录记录
     *
     * @param userId 用户ID
     * @return 登录记录
     */
    List<DailyLogin> findCurrentMonthLogForUser(Integer userId);
}
