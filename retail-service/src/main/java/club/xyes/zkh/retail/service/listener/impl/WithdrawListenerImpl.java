package club.xyes.zkh.retail.service.listener.impl;

import club.xyes.zkh.retail.commons.entity.CashApplication;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.service.listener.WithdrawListener;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Create by 郭文梁 2019/7/15 16:42
 * WithdrawListenerImpl
 * 提现监听器实现
 *
 * @author 郭文梁
 * @data 2019/7/15 16:42
 */
@Component
public class WithdrawListenerImpl implements WithdrawListener {
    private final UserService userService;

    public WithdrawListenerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onRefuse(CashApplication cashApplication) {
        @NotNull User user = userService.require(cashApplication.getUserId());
        val directIncome = (user.getDirectIncome() == null ? 0 : user.getDirectIncome()) + cashApplication.getAmount();
        user.setDirectIncome(directIncome);
        user.setWithdrawals((user.getWithdrawals() == null ? 0 : user.getWithdrawals()) - cashApplication.getAmount());
        userService.updateById(user);
    }
}
