package club.xyes.zkh.retail.wechat.dto;

import club.xyes.zkh.retail.commons.xml.annotation.XmlProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/6/13 0013 16:44
 * WxRefundNotifyData
 * 微信退款通知数据
 * <pre>
 *     <code>
 *         <root>
 *              <out_refund_no><![CDATA[5e8be139003c4f5f8422c41d54ae7b9b]]></out_refund_no>
 *              <out_trade_no><![CDATA[f4f42c983af84130897eeebd13236211]]></out_trade_no>
 *              <refund_account><![CDATA[REFUND_SOURCE_RECHARGE_FUNDS]]></refund_account>
 *              <refund_fee><![CDATA[1]]></refund_fee>
 *              <refund_id><![CDATA[50000300772019061310004057718]]></refund_id>
 *              <refund_recv_accout><![CDATA[支付用户零钱]]></refund_recv_accout>
 *              <refund_request_source><![CDATA[API]]></refund_request_source>
 *              <refund_status><![CDATA[SUCCESS]]></refund_status>
 *              <settlement_refund_fee><![CDATA[1]]></settlement_refund_fee>
 *              <settlement_total_fee><![CDATA[1]]></settlement_total_fee>
 *              <success_time><![CDATA[2019-06-13 15:25:24]]></success_time>
 *              <total_fee><![CDATA[1]]></total_fee>
 *              <transaction_id><![CDATA[4200000307201906139022011971]]></transaction_id>
 *          </root>
 *     </code>
 * </pre>
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@Data
public class WxRefundNotifyData {
    public static final String SUCCESS = "SUCCESS";
    @XmlProperty(name = "out_refund_no")
    private String outRefundNo;
    @XmlProperty(name = "out_trade_no")
    private String outTradeNo;
    @XmlProperty(name = "refund_account")
    private String refundAccount;
    @XmlProperty(name = "refund_fee")
    private String refundFee;
    @XmlProperty(name = "refund_id")
    private String refundId;
    @XmlProperty(name = "refund_recv_accout")
    private String refundRecvAccout;
    @XmlProperty(name = "refund_request_source")
    private String refundRequestSource;
    @XmlProperty(name = "refund_status")
    private String refundStatus;
    @XmlProperty(name = "settlement_refund_fee")
    private String settlementRefundFee;
    @XmlProperty(name = "settlement_total_fee")
    private String settlementTotalFee;
    @XmlProperty(name = "success_time")
    private String successTime;
    @XmlProperty(name = "total_fee")
    private String totalFee;
    @XmlProperty(name = "transaction_id")
    private String transactionId;

    public boolean isSuccess() {
        return Objects.equals(SUCCESS, refundStatus);
    }
}
