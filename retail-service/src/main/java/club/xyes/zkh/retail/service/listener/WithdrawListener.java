package club.xyes.zkh.retail.service.listener;

import club.xyes.zkh.retail.commons.entity.CashApplication;

/**
 * 体现操作监听器
 */
public interface WithdrawListener {
    /**
     * 拒绝体现时调用
     *
     * @param cashApplication 体现申请
     */
    void onRefuse(CashApplication cashApplication);
}
