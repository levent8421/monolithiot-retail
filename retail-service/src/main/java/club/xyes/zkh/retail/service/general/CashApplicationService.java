package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.CashApplication;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.WithdrawException;
import club.xyes.zkh.retail.service.basic.AbstractService;
import club.xyes.zkh.retail.service.listener.WithdrawListener;
import com.github.pagehelper.PageInfo;

/**
 * Create by 郭文梁 2019/5/25 0025 10:46
 * CashApplicationService
 * 提现申请相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/5/25 0025
 */
public interface CashApplicationService extends AbstractService<CashApplication> {
    /**
     * 创建新的提现申请
     *
     * @param user     用户
     * @param amount   提现金额
     * @param listener 创建成功的监听器
     * @return 提现申请对象
     */
    CashApplication create(User user, Integer amount, ApplicationCreateSuccessListener listener);

    /**
     * 通过状态分页查询提现申请
     *
     * @param status 状态
     * @param page   页码
     * @param rows   每页大小
     * @return PageInfo
     */
    PageInfo<CashApplication> findByStatus(Integer status, Integer page, Integer rows);

    /**
     * 执行提现操作
     *
     * @param application                        申请对象
     * @param applicationWithdrawSuccessListener 监听器
     * @return CashApplication
     */
    CashApplication withdraw(CashApplication application,
                             ApplicationWithdrawSuccessListener applicationWithdrawSuccessListener);

    /**
     * 拒绝用户的提现申请
     *
     * @param application 申请
     * @return 拒绝结果
     */
    CashApplication refuse(CashApplication application);

    /**
     * 拒绝用户体现
     *
     * @param application      提现申请对象
     * @param withdrawListener 体现监听器
     * @return 体现申请对象
     */
    CashApplication refuse(CashApplication application, WithdrawListener withdrawListener);

    /**
     * 获取用户的最后一条提现记录
     *
     * @param userId 用户ID
     * @return CashApplication
     */
    CashApplication findLastByUser(Integer userId);

    /**
     * 通过用户和状态查询提现记录
     *
     * @param userId 用户ID
     * @param status 状态
     * @param page   页码
     * @param rows   每页大小
     * @return PageInfo
     */
    PageInfo<CashApplication> findByUserAndStatus(Integer userId, Integer status, Integer page, Integer rows);

    /**
     * 搜索提现记录
     *
     * @param search 搜索条件
     * @param rows   每页大小
     * @param page   页码
     * @return PageInfo
     */
    PageInfo<CashApplication> search(String search, int page, int rows);

    /**
     * 提现申请创建成功监听
     */
    interface ApplicationCreateSuccessListener {
        /**
         * 提现申请创建成功监听
         *
         * @param application 申请对象
         */
        void onApplicationCreateSuccess(CashApplication application);
    }

    /**
     * 提现申请通过监听器
     */
    interface ApplicationWithdrawSuccessListener {
        /**
         * 提现申请通过时调用
         *
         * @param application 申请
         * @throws WithdrawException 提现异常
         */
        void onWithdrawSuccess(CashApplication application) throws WithdrawException;
    }
}
