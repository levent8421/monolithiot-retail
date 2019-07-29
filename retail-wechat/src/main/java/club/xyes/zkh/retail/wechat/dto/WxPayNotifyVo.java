package club.xyes.zkh.retail.wechat.dto;

import club.xyes.zkh.retail.commons.xml.annotation.XmlProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/6/3 0003 15:51
 * WxPayNotifyVo
 * 微信支付通知参数对象
 *
 * @author 郭文梁
 * @data 2019/6/3 0003
 */
@Data
public class WxPayNotifyVo {
    @XmlProperty(name = "appid")
    private String appId;
    @XmlProperty(name = "attach")
    private String attach;
    @XmlProperty(name = "bank_type")
    private String bankType;
    @XmlProperty(name = "fee_type")
    private String feeType;
    @XmlProperty(name = "is_subscribe")
    private String isSubscribe;
    @XmlProperty(name = "mch_id")
    private String mchId;
    @XmlProperty(name = "nonce_str")
    private String nonceStr;
    @XmlProperty(name = "openid")
    private String openid;
    @XmlProperty(name = "out_trade_no")
    private String outTradeNo;
    @XmlProperty(name = "result_code")
    private String resultCode;
    @XmlProperty(name = "return_code")
    private String returnCode;
    @XmlProperty(name = "sign")
    private String sign;
    @XmlProperty(name = "sub_mch_id")
    private String subMchId;
    @XmlProperty(name = "time_end")
    private String timeEnd;
    @XmlProperty(name = "total_fee")
    private String totalFee;
    @XmlProperty(name = "coupon_fee_0")
    private String couponFee0;
    @XmlProperty(name = "coupon_count")
    private String couponCount;
    @XmlProperty(name = "coupon_type")
    private String couponType;
    @XmlProperty(name = "coupon_id")
    private String couponId;
    @XmlProperty(name = "trade_type")
    private String tradeType;
    @XmlProperty(name = "transaction_id")
    private String transactionId;

    /**
     * 判断此次通知是否为一次成功的通知
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return Objects.equals("SUCCESS", resultCode);
    }

    /**
     * 判断支付是否成功
     *
     * @return 是否成功
     */
    public boolean isPaid() {
        return isSuccess()
                &&
                Objects.equals("SUCCESS", resultCode);
    }
}
