package club.xyes.zkh.retail.wechat.dto;

import club.xyes.zkh.retail.commons.xml.annotation.XmlProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/6/13 0013 10:54
 * WxRefundResult
 * 微信退款返回结果
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@Data
public class WxRefundResult {
    private static final String SUCCESS = "SUCCESS";
    @XmlProperty(name = "return_code")
    private String returnCode;
    @XmlProperty(name = "return_msg")
    private String returnMsg;
    @XmlProperty(name = "result_code")
    private String resultCode;
    @XmlProperty(name = "err_code_des")
    private String errCodeDes;
    @XmlProperty(name = "appid")
    private String appId;
    @XmlProperty(name = "mch_id")
    private String mchId;
    @XmlProperty(name = "nonce_str")
    private String nonceStr;
    @XmlProperty(name = "sign")
    private String sign;
    @XmlProperty(name = "transaction_id")
    private String transactionId;
    @XmlProperty(name = "out_trade_no")
    private String outTradeNo;
    @XmlProperty(name = "out_refund_no")
    private String outRefundNo;
    @XmlProperty(name = "refund_id")
    private String refundId;
    @XmlProperty(name = "refund_fee")
    private String refundFee;
    @XmlProperty(name = "settlement_refund_fee")
    private String settlementRefundFee;
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
    @XmlProperty(name = "cash_refund_fee")
    private String cashRefundFee;
    @XmlProperty(name = "coupon_refund_fee")
    private String couponRefundFee;
    @XmlProperty(name = "coupon_refund_count")
    private String couponRefundCount;

    /**
     * 退款请求是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return Objects.equals(SUCCESS, returnCode) && Objects.equals(resultCode, SUCCESS);
    }
}
