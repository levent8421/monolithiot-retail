package club.xyes.zkh.retail.service.listener;

import club.xyes.zkh.retail.commons.entity.CashApplication;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.exception.WithdrawException;
import club.xyes.zkh.retail.commons.utils.WithdrawUtils;
import club.xyes.zkh.retail.service.general.CashApplicationService;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.wechat.api.Wechat;
import club.xyes.zkh.retail.wechat.dto.WxWithdrawResult;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Create by 郭文梁 2019/5/30 0030 17:54
 * ApplicationWithdrawSuccessListenerImpl
 * 提现申请同意监听器实现
 * 即当管理员点击同意提现后触发北街口回调
 *
 * @author 郭文梁
 * @data 2019/5/30 0030
 */
@Component
public class ApplicationWithdrawSuccessListenerImpl implements CashApplicationService.ApplicationWithdrawSuccessListener, CashApplicationService.ApplicationCreateSuccessListener {
    private final UserService userService;
    private final Wechat wechat;

    public ApplicationWithdrawSuccessListenerImpl(UserService userService, Wechat wechat) {
        this.userService = userService;
        this.wechat = wechat;
    }

    @Override
    public void onWithdrawSuccess(CashApplication application) throws WithdrawException {
//        @NotNull User user = userService.require(application.getUserId());
//        //更新用户账户
//        updateUserIncome(user, application);
        //执行提现操作
//        doWithdraw(user, application);
    }

    /**
     * 执行提现操作
     *
     * @param user        用户
     * @param application 提现申请
     * @throws WithdrawException 提现异常
     */
    private void doWithdraw(User user, CashApplication application) throws WithdrawException {
        WxWithdrawResult result = wechat.withdraw(user, application);
        if (!result.isSuccess()) {
            throw new WithdrawException(result.getErrorMsg());
        }
    }

    /**
     * 更新用户账户
     *
     * @param user        用户
     * @param application 申请
     * @throws WithdrawException 提现异常
     */
    private void updateUserIncome(User user, CashApplication application) throws WithdrawException {
        int withdrawableAmount = WithdrawUtils.getWithdrawableAmount(user);
        if (withdrawableAmount < application.getAmount()) {
            throw new WithdrawException("提现金额大于用户账户金额，可能已经同意过用户的上一次提现申请！");
        }
        WithdrawUtils.subIncome(user, application.getAmount());
        userService.updateById(user);
    }

    @Override
    public void onApplicationCreateSuccess(CashApplication application) {
        @NotNull User user = userService.require(application.getUserId());
        try {
            updateUserIncome(user, application);
        } catch (WithdrawException e) {
            throw new BadRequestException(e.getMessage(), e);
        }
    }
}
