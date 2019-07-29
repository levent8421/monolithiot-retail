package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.entity.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/30 0030 11:12
 * CommissionUtil
 * 返现佣金相关工具类
 *
 * @author 郭文梁
 * @data 2019/5/30 0030
 */
public class CommissionUtil {
    /**
     * 佣金相加
     *
     * @param nums 佣金
     * @return 相加结果
     */
    public static int add(Integer... nums) {
        if (nums == null) {
            return 0;
        }
        int res = 0;
        for (Integer num : nums) {
            if (num != null) {
                res += num;
            }
        }
        return res;
    }

    /**
     * 获取返佣金额列表
     *
     * @param promoter  推广人
     * @param commodity 商品
     * @return 金额列表
     */
    public static List<Integer> getCommissionAmountList(User promoter, Commodity commodity) {
        if (Objects.equals(promoter.getRole(), User.ROLE_PROMOTERS)) {
            //普通推手 推手：1 二级：2 三级：3
            return Arrays.asList(commodity.getCommission1(), commodity.getCommission2(), commodity.getCommission3());
        } else if (Objects.equals(promoter.getRole(), User.ROLE_CAPTAIN)) {
            List<Integer> res;
            switch (promoter.getTeamHeaderLevel()) {
                case User.LEVEL_PRIMARY:
                    //小队长 推手：1 二级：2 三级：3
                    res = Arrays.asList(commodity.getCommission1(), commodity.getCommission2(), commodity.getCommission3());
                    break;
                case User.LEVEL_MIDDLE:
                    //中队长 推手：1+2 二级：3
                    res = Arrays.asList(add(commodity.getCommission1(), commodity.getCommission2()), commodity.getCommission3());
                    break;
                case User.LEVEL_SENIOR:
                    //大队长 推手 1+2+3
                    res = Collections.singletonList(add(commodity.getCommission1(), commodity.getCommission2(), commodity.getCommission3()));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown level " + promoter.getTeamHeaderLevel() + " for captain " + promoter);
            }
            return res;
        } else {
            throw new IllegalArgumentException("Can not user commission roles for a primary user (" + promoter.getId() + ")!");
        }
    }

    /**
     * 判断用户是否有推广权限
     *
     * @param user 用户
     * @return 是否有权限
     */
    public static boolean canPromot(User user) {
        switch (user.getRole()) {
            case User.ROLE_CAPTAIN:
            case User.ROLE_PROMOTERS:
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断用户是否为队长
     *
     * @param user 用户
     * @return 是否为队长
     */
    public static boolean isCaptain(User user) {
        return Objects.equals(user.getRole(), User.ROLE_CAPTAIN);
    }

    /**
     * 剪发 允许空值
     *
     * @param num1 数字2
     * @param num2 数字2
     * @return 结果
     */
    public static int subtract(Integer num1, Integer num2) {
        if (num2 == null) {
            num2 = 0;
        }
        if (num1 == null) {
            return 0 - num2;
        }
        return num1 - num2;
    }
}
