package club.xyes.zkh.retail.service.listener;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.CommissionLog;
import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.utils.CommissionUtil;
import club.xyes.zkh.retail.commons.utils.DateTimeUtils;
import club.xyes.zkh.retail.commons.utils.MoneyUtils;
import club.xyes.zkh.retail.commons.utils.OrderUtils;
import club.xyes.zkh.retail.service.general.*;
import club.xyes.zkh.retail.wechat.api.Wechat;
import club.xyes.zkh.retail.wechat.api.YunPian;
import club.xyes.zkh.retail.wechat.dto.TemplateMsgData;
import club.xyes.zkh.retail.wechat.dto.WxTemplateMsgParam;
import club.xyes.zkh.retail.wechat.dto.WxTemplateMsgResult;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/30 0030 10:17
 * PaySuccessListenerImpl
 * 支付成功监听器实现
 *
 * @author 郭文梁
 * @data 2019/5/30 0030
 */
@Component
@Slf4j
public class PaySuccessListenerImpl implements OrderService.PaySuccessListener {
    private final OrderService orderService;
    private final CommodityService commodityService;
    private final UserService userService;
    private final CommissionLogService commissionLogService;
    private final YunPian yunPian;
    private final StoreService storeService;
    private final Wechat wechat;

    public PaySuccessListenerImpl(OrderService orderService,
                                  CommodityService commodityService,
                                  UserService userService,
                                  CommissionLogService commissionLogService,
                                  YunPian yunPian,
                                  StoreService storeService,
                                  Wechat wechat) {
        this.orderService = orderService;
        this.commodityService = commodityService;
        this.userService = userService;
        this.commissionLogService = commissionLogService;
        this.yunPian = yunPian;
        this.storeService = storeService;
        this.wechat = wechat;
    }


    @Override
    public void onPaySuccess(Order order) {
        log.debug("Pay success {}", order);
        val commodity = commodityService.require(order.getCommodityId());
        val user = userService.require(order.getUserId());
        updateUserScore(user);
        updateStock(commodity);
        updateOrderStatus(order, commodity);
        doReturnCommission(order, commodity);
    }

    /**
     * 更新用户的积分
     *
     * @param user 用户
     */
    private void updateUserScore(User user) {
        var score = user.getScore() == null ? 0 : user.getScore();
        if (Objects.equals(user.getRole(), User.ROLE_USER)) {
            score += ApplicationConstants.COMMISSION_SCORE;
        }
        user.setScore(score);
        userService.updateById(user);
    }

    /**
     * 保存推手信息
     *
     * @param user     用户
     * @param promoter 推手
     */
    private void savePromoter(User user, User promoter) {
        if (user.getPromoterId() == null) {
            user.setPromoterId(promoter.getId());
        }
        userService.updateById(user);
    }

    /**
     * 更新商品库存
     *
     * @param commodity 商品对象
     */
    private void updateStock(Commodity commodity) {
        int amount = commodityService.updateStock(commodity, 1);
        log.debug("Update stock for commodity {} , amount={}", commodity.getId(), amount);
    }

    /**
     * 执行推广返现逻辑
     *
     * @param order     订单信息
     * @param commodity 商品信息
     */
    private void doReturnCommission(Order order, Commodity commodity) {
        if (order.getPromoterId() == null) {
            log.info("Important Information : no promoter for order:" + order.getId());
            return;
        }
        //直接推广用户
        User promoter = userService.require(order.getPromoterId());
        if (!CommissionUtil.canPromot(promoter)) {
            //该推手已经没有推广权限  不在继续执行返现逻辑
            log.debug("User {} is not a Promoter", promoter);
            return;
        }
        @NotNull final User user = userService.require(order.getUserId());
        savePromoter(user, promoter);
        //返现规则列表
        List<Integer> commissionAmountList = CommissionUtil.getCommissionAmountList(promoter, commodity);
        //一级返现 存入推手的直接收益
        doReturnCommission(order, commissionAmountList.get(0), promoter);
        //二三级返现 存入用户的团队收益
        doReturnTeamCommission(order, promoter, commissionAmountList);
        //发送模板消息通知
        sendNotifyWxMsg(order);
        orderService.updateById(order);
        log.info("Save order promoter info: order=[{}], promoter=[{},{},{}]", order.getId(),
                order.getPromoterId(), order.getPromoter2Id(), order.getPromoter3Id());
    }

    /**
     * 执行二三级返现
     *
     * @param promoter       直接推手
     * @param commissionList 返现规则列表
     */
    private void doReturnTeamCommission(Order order, User promoter, List<Integer> commissionList) {
        User captain = promoter;
        //由于第一条返现规则是直接推广返现规则 故这里从第二条开始算
        for (int i = 1; i < commissionList.size(); i++) {
            if (captain.getLeaderId() == null) {
                //上级队长不存在 不再继续执行返现
                break;
            }
            Integer commission = commissionList.get(i) * order.getQuantity();
            promoter = captain;
            captain = userService.require(captain.getLeaderId());
            if (!Objects.equals(captain.getRole(), User.ROLE_CAPTAIN)) {
                //该队长已不再是队长 可能一杯设置为其他角色 不再计算返现金额
                log.debug("User {} is not a Captain!", captain);
                break;
            }
            Integer originalTeamIncome = captain.getTeamIncome();
            int income = CommissionUtil.add(originalTeamIncome, commission);
            captain.setTeamIncome(income);
            userService.updateById(captain);
            //更新订单的推广人信息
            log.info("Team Commission [{}] promoter=[{}], commission=[{}], original income=[{}], result income=[{}]", i, captain.getId(), commission, originalTeamIncome, income);
            if (i == 1) {
                order.setPromoter2Id(captain.getId());
            } else if (i == 2) {
                order.setPromoter3Id(captain.getId());
            } else {
                throw new InternalServerErrorException("Only Support commission level 2");
            }
            commissionLogService.log(captain, order, commission, CommissionLog.REASON_TEAM_PROMOTE);
            try {
                final String title = String.format("您的队员【%s】推广成功！", promoter.getPromoterName());
                sendCommissionNotifyMsg(title, "团队推广返现", commission, captain.getWxOpenId());
            } catch (Exception e) {
                log.error("Team commission fail, user=[{}], order=[{}]", captain.getId(), order.getId(), e);
            }
        }
    }

    /**
     * 执行一级推广返现
     *
     * @param order      订单
     * @param commission 返现金额
     * @param promoter   直接推广者
     */
    private void doReturnCommission(Order order, Integer commission, User promoter) {
        commission *= order.getQuantity();
        //用户的直接收益
        Integer directIncome = promoter.getDirectIncome();
        //与本次推广收益相加
        int resultIncome = CommissionUtil.add(directIncome, commission);
        //设置为新的直接推广收益
        promoter.setDirectIncome(resultIncome);
        //更新数据库
        userService.updateById(promoter);
        log.info("Direct Commission :promoter=[{}], commission=[{}], original income=[{}], result income=[{}]", promoter.getId(),
                commission, directIncome, resultIncome);
        commissionLogService.log(promoter, order, commission, CommissionLog.REASON_DIRECT_PROMOTE);
        try {
            final String title = String.format("您的推广用户【%s】下单成功！", order.getUsername());
            sendCommissionNotifyMsg(title, "直接推广", commission, promoter.getWxOpenId());
        } catch (Exception e) {
            log.warn("Send Direct promote msg error,order={}, promoter={},err={}", order.getId(), promoter.getId(), e.getMessage(), e);
        }
    }

    /**
     * 更新订单状态
     *
     * @param commodity 商品信息
     * @param order     订单
     */
    private void updateOrderStatus(Order order, Commodity commodity) {
        if (OrderUtils.needAppointment(commodity)) {
            order.setStatus(Order.STATUS_NEED_BOOKED);
        } else {
            order.setStatus(Order.STATUS_PAID);
            final SmsSingleSend sendRes = yunPian.sendPaymentSuccessNotifySms(order, storeService.require(commodity.getStoreId()));
            log.info("Send Sms Result:[{}]", sendRes);
        }
        orderService.updateById(order);
    }

    /**
     * 发送支付成功模板消息通知
     *
     * @param order 订单
     */
    private void sendNotifyWxMsg(Order order) {
        @NotNull final User user = userService.require(order.getUserId());

        Map<String, TemplateMsgData> data = new HashMap<>(16);
        data.put("first", new TemplateMsgData("微信付款成功", TemplateMsgData.DEFAULT_COLOR));
        data.put("remark", new TemplateMsgData("感谢您的使用", TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword1", new TemplateMsgData(order.getSn(), TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword2", new TemplateMsgData(MoneyUtils.fen2Yuan(order.getAmount()), TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword3", new TemplateMsgData("微信在线支付", TemplateMsgData.DEFAULT_COLOR));


        final WxTemplateMsgParam msgParam = new WxTemplateMsgParam();
        msgParam.setToUser(user.getWxOpenId());
        msgParam.setTemplateId(ApplicationConstants.WeChat.PAY_NOTIFY_MSG_TEMPLATE_ID);
        msgParam.setUrl(null);
        msgParam.setData(data);
        final WxTemplateMsgResult result = wechat.sendTemplateMsg(msgParam);
        log.debug("Send payment success notify template msg result [{}]", result);
    }

    /**
     * 发送返现通知
     *
     * @param title  辩题
     * @param type   返现类型
     * @param amount 返现金额
     * @param openId 发送用户
     */
    private void sendCommissionNotifyMsg(String title, String type, int amount, String openId) {
        final Map<String, TemplateMsgData> data = new HashMap<>(16);
        final String timeStr = DateTimeUtils.format(DateTimeUtils.now(), ApplicationConstants.DATE_TIME_FORMAT);
        final String amountStr = MoneyUtils.fen2Yuan(amount) + "元";
        data.put("first", new TemplateMsgData(title, TemplateMsgData.DEFAULT_COLOR));
        data.put("remark", new TemplateMsgData("返佣已入账，请前往[我的]>[我的金库]查看", TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword1", new TemplateMsgData(type, TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword2", new TemplateMsgData(amountStr, TemplateMsgData.DEFAULT_COLOR));
        data.put("keyword3", new TemplateMsgData(timeStr, TemplateMsgData.DEFAULT_COLOR));

        final WxTemplateMsgParam param = new WxTemplateMsgParam();
        param.setToUser(openId);
        param.setTemplateId(ApplicationConstants.WeChat.COMMISSION_NOTIFY_TEMPLATE_MSG_ID);
        param.setData(data);

        final WxTemplateMsgResult sendRes = wechat.sendTemplateMsg(param);
        log.debug("Commission notify template msg send result [{}]", sendRes);
    }
}
