package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/25 0025 11:11
 * WithdrawUtils
 * 提现相关工具类
 *
 * @author 郭文梁
 * @data 2019/5/25 0025
 */
public class WithdrawUtils {
    /**
     * 获取可提现金额
     *
     * @param user 用户对象
     * @return 可提现金额
     */
    public static int getWithdrawableAmount(User user) {
        int amount;
        switch (user.getRole()) {
            case User.ROLE_USER:
                amount = 0;
                break;
            case User.ROLE_PROMOTERS:
                amount = user.getDirectIncome();
                break;
            case User.ROLE_CAPTAIN:
                amount = getCaptainWithdrawableAmount(user);
                break;
            default:
                throw new IllegalArgumentException("Unknown role" + user.getRole() + " for user " + user);
        }
        return amount;
    }

    /**
     * 获取队长的可提现金额
     *
     * @param user 用户对象
     * @return 可提现金额
     */
    private static int getCaptainWithdrawableAmount(User user) {
        if (Objects.equals(user.getRole(), User.ROLE_CAPTAIN)) {
            return add(user.getTeamIncome(), user.getDirectIncome());
        } else {
            throw new IllegalArgumentException("User " + user + " is not a Captain!");
        }
    }

    /**
     * 减去用户的一部分收益
     *
     * @param user   用户
     * @param amount 金额
     */
    public static void subIncome(User user, Integer amount) {
        Integer directIncome = user.getDirectIncome();
        if (directIncome == null) {
            directIncome = 0;
        }
        if (amount == null) {
            amount = 0;
        }
        if (directIncome >= amount) {
            user.setDirectIncome(directIncome - amount);
        } else {
            Integer teamIncome = user.getTeamIncome();
            int surplus = teamIncome + directIncome - amount;
            if (surplus < 0) {
                throw new InternalServerErrorException("用户账户余额不足");
            }
            user.setTeamIncome(surplus);
            user.setDirectIncome(0);
        }
        user.setWithdrawals((user.getWithdrawals() == null ? 0 : user.getWithdrawals()) + amount);
    }

    /**
     * 相加两个数 当参数为null是默认为0
     *
     * @param num1 num1
     * @param num2 num2
     * @return result
     */
    public static int add(Integer num1, Integer num2) {
        num1 = num1 == null ? 0 : num1;
        num2 = num2 == null ? 0 : num2;
        return num1 + num2;
    }
}
