package club.xyes.zkh.retail.wechat.dto;

import club.xyes.zkh.retail.commons.xml.annotation.XmlProperty;
import club.xyes.zkh.retail.wechat.utils.WxSignUtil;
import lombok.Data;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/6/3 0003 10:57
 * WxPayResult
 * 微信支付返回结果
 *
 * @author 郭文梁
 * @data 2019/6/3 0003
 */
@Data
public class WxPayResult {
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
    @XmlProperty(name = "openid")
    private String opeId;
    private String sign;
    @XmlProperty(name = "result_code")
    private String resultCode;
    @XmlProperty(name = "prepay_id")
    private String prepayId;
    @XmlProperty(name = "trade_type")
    private String tradeType;
    private Long timestamp;
    private String signType = WxSignUtil.SIGN_TYPE_MD5;

    /**
     * 判断下单请求是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return Objects.equals("SUCCESS", returnCode)
                &&
                Objects.equals("SUCCESS", resultCode);
    }
}
