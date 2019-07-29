package club.xyes.zkh.retail.wechat.dto;

import club.xyes.zkh.retail.commons.xml.annotation.XmlProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/30 0030 10:23
 * WxTradeInfo
 * 微信交易记录查询结果
 *
 * @author 郭文梁
 * @data 2019/5/30 0030
 */
@Data
public class WxTradeInfo {
    @XmlProperty(name = "return_code")
    private String returnCode;
    @XmlProperty(name = "return_msg")
    private String returnMsg;
    @XmlProperty(name = "appid")
    private String appId;
    @XmlProperty(name = "mch_id")
    private String mchId;
    @XmlProperty(name = "nonce_str")
    private String nonceStr;
    private String sign;
    @XmlProperty(name = "result_code")
    private String resultCode;
    @XmlProperty(name = "err_code")
    private String errCode;
    @XmlProperty(name = "err_code_des")
    private String errCodeDes;
    @XmlProperty(name = "device_info")
    private String deviceInfo;
    @XmlProperty(name = "openid")
    private String openId;
    @XmlProperty(name = "is_subscribe")
    private String isSubscribe;
    @XmlProperty(name = "trade_type")
    private String tradeType;
    @XmlProperty(name = "trade_state")
    private String tradeState;
    @XmlProperty(name = "bank_type")
    private String bankType;
    @XmlProperty(name = "total_fee")
    private String totalFee;
    @XmlProperty(name = "settlement_total_fee")
    private String settlementTotalFee;
    @XmlProperty(name = "fee_type")
    private String feeType;
    @XmlProperty(name = "cash_fee")
    private String cashFee;
    @XmlProperty(name = "cash_fee_type")
    private String cashFeeType;
    @XmlProperty(name = "coupon_fee")
    private String couponFee;
    @XmlProperty(name = "coupon_count")
    private String couponCount;
    @XmlProperty(name = "transaction_id")
    private String transactionId;
    @XmlProperty(name = "out_trade_no")
    private String outTradeNo;
    @XmlProperty(name = "attach")
    private String attach;
    @XmlProperty(name = "time_end")
    private String timeEnd;
    @XmlProperty(name = "trade_state_desc")
    private String tradeStateDesc;

    /**
     * 获取是否已经成功支付
     *
     * @return 是否已经成功支付
     */
    public boolean isPaid() {
        return isSuccess() &&
                Objects.equals("SUCCESS", tradeState);
    }

    /**
     * 检查请求操作是否成
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return Objects.equals("SUCCESS", returnCode)
                &&
                Objects.equals("SUCCESS", resultCode);
    }
}
