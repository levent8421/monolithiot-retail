package club.xyes.zkh.retail.wechat.api.impl;

import club.xyes.zkh.retail.commons.entity.CashApplication;
import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.RefundLog;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.utils.KeyStoreUtils;
import club.xyes.zkh.retail.commons.utils.RandomUtils;
import club.xyes.zkh.retail.commons.utils.XmlUtils;
import club.xyes.zkh.retail.wechat.api.Wechat;
import club.xyes.zkh.retail.wechat.dto.*;
import club.xyes.zkh.retail.wechat.props.WechatConfig;
import club.xyes.zkh.retail.wechat.utils.WxSignUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by 郭文梁 2019/5/18 0018 13:47
 * WechatImpl
 * 微信Api实现
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@Component
@Slf4j
public class WechatImpl extends AbstractHttpApi implements Wechat, ApplicationContextAware {
    private static final String DEV_PROFILE_NAME = "dev";
    /**
     * 发送模板消息失败 原因是需要关注的errcode
     */
    private static final String REQUIRE_SUBSCRIBE_ERR_CODE = "43004";
    /**
     * 获取AccessToken
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * 获取JsApiTicket
     */
    private static final String JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    /**
     * OAuth2 code换Token的Api地址
     */
    private static final String CODE_2_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    /**
     * 获取用户信息的Api地址
     */
    private static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=%s";
    /**
     * 微信支付API地址
     */
    private static final String WX_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 订单查询接口
     */
    private static final String WX_PAY_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    /**
     * 微信红包API地址
     */
    private static final String WX_PAY_WITHDRAW_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
    /**
     * 设置菜单API地址
     */
    private static final String WX_SET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
    /**
     * 退款API地址
     */
    private static final String WX_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    /**
     * 发送模板消息，接口地址
     */
    private static final String WX_TEMPLATE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    private static final int NONCE_STR_LEN = 15;
    /**
     * 微信红包发送者名称
     */
    private static final String WITHDRAW_SEND_NAME = "小龙侠优品";
    /**
     * 微信红包祝福语
     */
    private static final String WITHDRAW_WISHING = "小龙侠感谢您的推广支持";
    /**
     * 红包活动名称
     */
    private static final String WITHDRAW_ACT_NAME = "推广返佣";
    /**
     * 微信红包备注
     */
    private static final String WITHDRAW_REMARK = "推得越多，返的越多";
    /**
     * 微信红包场景
     * 发放红包使用场景，红包金额大于200或者小于1元时必传
     * PRODUCT_1:商品促销
     * PRODUCT_2:抽奖
     * PRODUCT_3:虚拟物品兑奖
     * PRODUCT_4:企业内部福利
     * PRODUCT_5:渠道分润
     * PRODUCT_6:保险回馈
     * PRODUCT_7:彩票派奖
     * PRODUCT_8:税务刮奖
     */
    private static final String WITHDRAW_SCENE_ID = "PRODUCT_5";
    /**
     * 应用上下文
     */
    private ApplicationContext applicationContext;
    /**
     * AccessToken缓存
     */
    private Map<String, WxAccessToken> localAccessTokenCache = new ConcurrentHashMap<>();
    /**
     * JsApiTicket缓存
     */
    private Map<String, WxJsApiTicket> localJsApiTicketCache = new ConcurrentHashMap<>();
    private final WechatConfig wechatConfig;
    private KeyStore keyStore;

    public WechatImpl(WechatConfig wechatConfig) {
        this.wechatConfig = wechatConfig;
    }

    /**
     * 初始化证书文件
     */
    @PostConstruct
    private void initKey() {
        String keyFile = wechatConfig.getKeyFile();
        String password = wechatConfig.getKeyPassword();
        val activeProfiles = new ArrayList<String>(Arrays.asList(applicationContext.getEnvironment().getActiveProfiles()));
        if (!activeProfiles.contains(DEV_PROFILE_NAME)) {
            this.keyStore = KeyStoreUtils.loadKey(keyFile, password);
        }
    }

    @Override
    public WxAccessToken getToken(String appId, String secret) {
        WxAccessToken lastToken = localAccessTokenCache.get(appId);
        if (lastToken != null && !lastToken.expired()) {
            return lastToken;
        }
        String tokenUrl = String.format(ACCESS_TOKEN_URL, appId, secret);
        lastToken = get(tokenUrl, WxAccessToken.class, res -> res.get("errcode") == null);
        lastToken.setRefreshTime(System.currentTimeMillis() / 1000);
        localAccessTokenCache.put(appId, lastToken);
        return lastToken;
    }

    @Override
    public WxJsApiTicket getJsApiTicket(String appId, String secret) {
        WxJsApiTicket ticket = localJsApiTicketCache.get(appId);
        if (ticket != null && !ticket.expired()) {
            return ticket;
        }
        WxAccessToken token = getToken(appId, secret);
        String apiUrl = String.format(JS_API_TICKET_URL, token.getAccessToken());
        ticket = get(apiUrl, WxJsApiTicket.class, res -> res.getInteger("errcode") == 0);
        ticket.setRefreshTime(System.currentTimeMillis() / 1000);
        localJsApiTicketCache.put(appId, ticket);
        return ticket;
    }

    @Override
    public WxJsApiConfigParam getJsApiConfigParam(String appId, String secret, String url) {
        String nonceStr = RandomUtils.randomString(NONCE_STR_LEN, RandomUtils.ALL_VISIBLE_CHARS);
        WxJsApiTicket ticket = getJsApiTicket(appId, secret);
        long timestamp = System.currentTimeMillis() / 1000;
        String signTemplate = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";
        String signText = String.format(signTemplate, ticket.getTicket(), nonceStr, timestamp, url);
        String sign = DigestUtils.sha1Hex(signText);

        WxJsApiConfigParam res = new WxJsApiConfigParam();
        res.setAppId(appId);
        res.setNonceStr(nonceStr);
        res.setSignature(sign);
        res.setTimestamp(timestamp);
        return res;
    }

    @Override
    public WxOAuth2AccessToken code2Token(String appId, String secret, String code) {
        String apiUrl = String.format(CODE_2_TOKEN_URL, appId, secret, code);
        return get(apiUrl, WxOAuth2AccessToken.class, res -> res.get("openid") != null && res.get("access_token") != null);
    }

    @Override
    public WxUserInfo getUserInfo(String accessToken, String openId, String lang) {
        String apiUrl = String.format(GET_USER_INFO_URL, accessToken, openId, lang);
        return get(apiUrl, WxUserInfo.class, res -> res.get("errcode") == null);
    }

    @Override
    public WxTradeInfo queryTrade(Order order) {
        Map<String, String> params = buildOrderQueryParams(order);
        String sign = WxSignUtil.signMd5(params, wechatConfig.getSignKey());
        params.put("sign", sign);
        String xmlBody = XmlUtils.asXml(params, "xml");
        String resp = postXml(WX_PAY_QUERY_URL, xmlBody);
        WxTradeInfo tradeInfo;
        try {
            tradeInfo = XmlUtils.parseObject(resp, WxTradeInfo.class);
        } catch (DocumentException e) {
            throw new InternalServerErrorException("Parse response xml error:[" + resp + "]");
        }
        if (!tradeInfo.isSuccess()) {
            throw new InternalServerErrorException("Query trade error, [" + resp + "]");
        }
        return tradeInfo;
    }

    @Override
    public WxWithdrawResult withdraw(User user, CashApplication application) {
        Map<String, String> params = buildWithdrawParams(user, application);
        String sign = WxSignUtil.signMd5(params, wechatConfig.getSignKey());
        params.put("sign", sign);
        String xmlBody = XmlUtils.asXml(params, "xml");
        String resp = postXmlWithP12(WX_PAY_WITHDRAW_URL, xmlBody, keyStore, wechatConfig.getKeyPassword());
        WxWithdrawResult result;
        try {
            result = XmlUtils.parseObject(resp, WxWithdrawResult.class);
        } catch (DocumentException e) {
            throw new InternalServerErrorException("Parse Withdraw result error:[" + resp + "]");
        }
        if (!result.isSuccess()) {
            throw new InternalServerErrorException("Withdraw fail:[" + resp + "]");
        }
        log.info("Withdraw user{} withdraw {} !", user, application.getAmount());
        return result;
    }

    @Override
    public WxPayResult pay(Order order, User user) {
        Map<String, String> params = buildPayParams(order, user);
        String sign = WxSignUtil.signMd5(params, wechatConfig.getSignKey());
        params.put("sign", sign);
        String postBody = XmlUtils.asXml(params, "xml");
        String resp = postXml(WX_PAY_URL, postBody);
        log.debug("Pay http request : >>[{}] <<[{}]", postBody, resp);
        WxPayResult payResult;
        try {
            payResult = XmlUtils.parseObject(resp, WxPayResult.class);
        } catch (DocumentException e) {
            throw new InternalServerErrorException("Parse response xml error![" + resp + "]");
        }
        if (!payResult.isSuccess()) {
            throw new InternalServerErrorException("Wechat pay error:" + resp);
        }
        return payResult;
    }

    /**
     * 构建微信支付参数
     *
     * @param order 订单
     * @param user  用户
     * @return 参数
     */
    private Map<String, String> buildPayParams(Order order, User user) {
        String nonceStr = RandomUtils.randomString(NONCE_STR_LEN);
        Map<String, String> params = new HashMap<>(12);
        params.put("appid", wechatConfig.getAppId());
        params.put("mch_id", wechatConfig.getMchId());
        params.put("nonce_str", nonceStr);
        params.put("sign_type", wechatConfig.getPaySignType());
        params.put("body", "Commodity for xiaolongxia retail");
        params.put("out_trade_no", order.getTradeNo());
        params.put("total_fee", String.valueOf(order.getAmount()));
        params.put("spbill_create_ip", wechatConfig.getIp());
        params.put("notify_url", wechatConfig.getNotifyUrl());
        params.put("trade_type", wechatConfig.getTradeType());
        params.put("openid", user.getWxOpenId());
        return params;
    }

    /**
     * 构建微信支付订单查询参数
     *
     * @param order 订单
     * @return 参数
     */
    private Map<String, String> buildOrderQueryParams(Order order) {
        String nonceStr = RandomUtils.randomString(NONCE_STR_LEN);
        Map<String, String> params = new HashMap<>(6);
        params.put("appid", wechatConfig.getAppId());
        params.put("mch_id", wechatConfig.getMchId());
        params.put("out_trade_no", order.getTradeNo());
        params.put("nonce_str", nonceStr);
        params.put("sign_type", wechatConfig.getPaySignType());
        return params;
    }

    /**
     * 构建微信红包参数
     *
     * @param user        用户
     * @param application tixcianshenq对象
     * @return 参数
     */
    private Map<String, String> buildWithdrawParams(User user, CashApplication application) {
        Map<String, String> params = new HashMap<>(14);
        params.put("mch_billno", application.getTradeNo());
        params.put("mch_id", wechatConfig.getMchId());
        params.put("wxappid", wechatConfig.getAppId());
        params.put("send_name", WITHDRAW_SEND_NAME);
        params.put("re_openid", user.getWxOpenId());
        params.put("total_amount", String.valueOf(application.getAmount()));
        params.put("total_num", String.valueOf(1));
        params.put("wishing", WITHDRAW_WISHING);
        params.put("client_ip", wechatConfig.getIp());
        params.put("act_name", WITHDRAW_ACT_NAME);
        params.put("remark", WITHDRAW_REMARK);
        params.put("scene_id", WITHDRAW_SCENE_ID);
        params.put("nonce_str", RandomUtils.randomString(NONCE_STR_LEN));
        return params;
    }

    @Override
    public String setMenu(String menuJson) throws IOException {
        final String appId = wechatConfig.getAppId();
        final String secret = wechatConfig.getSecret();
        final WxAccessToken token = getToken(appId, secret);
        final String apiUrl = String.format(WX_SET_MENU_URL, token.getAccessToken());
        return postJson(apiUrl, menuJson);
    }

    @Override
    public WxPayParams toJsPayParams(String prepayId) {
        String appId = wechatConfig.getAppId();
        String nonceStr = RandomUtils.randomString(NONCE_STR_LEN);
        String packageStr = "prepay_id=" + prepayId;
        String signType = WxSignUtil.SIGN_TYPE_MD5;
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String apiKey = wechatConfig.getSignKey();

        Map<String, String> signParams = new HashMap<>(5);
        signParams.put("appId", appId);
        signParams.put("nonceStr", nonceStr);
        signParams.put("package", packageStr);
        signParams.put("signType", signType);
        signParams.put("timeStamp", timestamp);

        final String sign = WxSignUtil.signMd5(signParams, apiKey);

        final WxPayParams params = new WxPayParams();
        params.setAppId(appId);
        params.setTimestamp(timestamp);
        params.setNonceStr(nonceStr);
        params.setPackageStr(packageStr);
        params.setSignType(signType);
        params.setPaySign(sign);
        return params;
    }

    @Override
    public WxRefundResult refund(RefundLog refundLog, Order order) {
        final Map<String, String> params = buildRefundParams(refundLog, order);
        final String sign = WxSignUtil.signMd5(params, wechatConfig.getSignKey());
        params.put("sign", sign);
        final String xml = XmlUtils.asXml(params, "xml");
        final String response = postXmlWithP12(WX_REFUND_URL, xml, keyStore, wechatConfig.getKeyPassword());
        try {
            return XmlUtils.parseObject(response, WxRefundResult.class);
        } catch (DocumentException e) {
            log.warn("Could not parse wx refund response [{}]", response);
            throw new InternalServerErrorException("Could not parse Wx refund response", e);
        }
    }

    private Map<String, String> buildRefundParams(RefundLog refundLog, Order order) {
        final String appId = wechatConfig.getAppId();
        final String mchId = wechatConfig.getMchId();
        final String nonceStr = RandomUtils.randomString(NONCE_STR_LEN);
        Map<String, String> params = new HashMap<>(10);
        params.put("appid", appId);
        params.put("mch_id", mchId);
        params.put("nonce_str", nonceStr);
        params.put("sign_type", WxSignUtil.SIGN_TYPE_MD5);
        params.put("out_trade_no", order.getTradeNo());
        params.put("out_refund_no", refundLog.getTradeNo());
        params.put("total_fee", String.valueOf(refundLog.getOrderAmount()));
        params.put("refund_fee", String.valueOf(refundLog.getRefundAmount()));
        params.put("notify_url", wechatConfig.getRefundNotifyUrl());
        return params;
    }

    @Override
    public WxTemplateMsgResult sendTemplateMsg(WxTemplateMsgParam param) {
        final String appId = wechatConfig.getAppId();
        final String secret = wechatConfig.getSecret();
        final WxAccessToken token = getToken(appId, secret);

        final String url = String.format(WX_TEMPLATE_MSG_URL, token.getAccessToken());
        final String postBody = JSON.toJSONString(param);
        try {
            final WxTemplateMsgResult result = postJson(url, postBody, WxTemplateMsgResult.class);
            if (!result.isSuccess()) {
                if (REQUIRE_SUBSCRIBE_ERR_CODE.equals(result.getErrCode())) {
                    log.warn("Send template msg to [" + param.getToUser() + "] fail, this user require subscribe wechat account!");
                } else {
                    throw new InternalServerErrorException("Could not send template msg, result=[" + result + "]");
                }
            }
            return result;
        } catch (IOException e) {
            log.warn("Send template msg error, url=[{}], body=[{}]", url, postBody, e);
            throw new InternalServerErrorException("Send template msg error[IOException]!", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
