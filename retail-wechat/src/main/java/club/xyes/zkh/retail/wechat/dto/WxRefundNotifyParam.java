package club.xyes.zkh.retail.wechat.dto;

import club.xyes.zkh.retail.commons.xml.annotation.XmlProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/6/13 0013 15:55
 * WxRefundNotifyParam
 * 微信退款通知参数
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@Data
public class WxRefundNotifyParam {
    private static final String SUCCESS = "SUCCESS";
    @XmlProperty(name = "return_code")
    private String returnCode;
    @XmlProperty(name = "appid")
    private String appId;
    @XmlProperty(name = "mch_id")
    private String mchId;
    @XmlProperty(name = "nonce_str")
    private String nonceStr;
    @XmlProperty(name = "req_info")
    private String reqInfo;

    public boolean isSuccess() {
        return Objects.equals(SUCCESS, returnCode);
    }
}
