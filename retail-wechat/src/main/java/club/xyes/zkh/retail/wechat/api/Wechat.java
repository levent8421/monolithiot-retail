package club.xyes.zkh.retail.wechat.api;

import club.xyes.zkh.retail.commons.entity.CashApplication;
import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.RefundLog;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.wechat.dto.*;

import java.io.IOException;

/**
 * Create by 郭文梁 2019/5/18 0018 13:46
 * Wechat
 * 微信Api
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
public interface Wechat {
    /**
     * 我去微信AccessToken
     *
     * @param appId  AppId
     * @param secret Secret
     * @return WxAccessToken
     */
    WxAccessToken getToken(String appId, String secret);

    /**
     * 获取JsApiTicket
     *
     * @param appId  AppId
     * @param secret Secret
     * @return WxJsApiTicket
     */
    WxJsApiTicket getJsApiTicket(String appId, String secret);

    /**
     * 获取JsApi配置参数
     *
     * @param appId  AppId
     * @param secret Secret
     * @param url    Url
     * @return WxJsApiConfigParam
     */
    WxJsApiConfigParam getJsApiConfigParam(String appId, String secret, String url);

    /**
     * 用Code换取AccessToken和OpenId
     *
     * @param appId  AppId
     * @param secret Secret
     * @param code   code
     * @return WxOAuth2AccessToken
     */
    WxOAuth2AccessToken code2Token(String appId, String secret, String code);

    /**
     * 获取用户信息
     *
     * @param accessToken AccessToken
     * @param openId      OpenId
     * @param lang        语言
     * @return 微信用户信息
     */
    WxUserInfo getUserInfo(String accessToken, String openId, String lang);

    /**
     * 查询交易状态
     *
     * @param order 订单
     * @return 查询结果
     */
    WxTradeInfo queryTrade(Order order);

    /**
     * 微信提现
     *
     * @param user        用户
     * @param application 申请对象
     * @return 提现结果
     */
    WxWithdrawResult withdraw(User user, CashApplication application);

    /**
     * 微信支付
     *
     * @param order 订单
     * @param user  用户
     * @return 支付结果
     */
    WxPayResult pay(Order order, User user);

    /**
     * 设置菜单
     *
     * @param menuJson 菜单JSON
     * @return 设置结果
     * @throws IOException IO异常
     */
    String setMenu(String menuJson) throws IOException;

    /**
     * 转换为微信JSPay的参数
     *
     * @param prepayId prepay ID
     * @return 参数对象
     */
    WxPayParams toJsPayParams(String prepayId);

    /**
     * 微信退款
     *
     * @param order     订单
     * @param refundLog 退款记录
     * @return 退款结果
     */
    WxRefundResult refund(RefundLog refundLog, Order order);

    /**
     * 发送模板消息
     *
     * @param param 参数
     * @return 发送结果
     */
    WxTemplateMsgResult sendTemplateMsg(WxTemplateMsgParam param);
}
