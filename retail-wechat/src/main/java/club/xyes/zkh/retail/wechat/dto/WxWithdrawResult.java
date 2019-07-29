package club.xyes.zkh.retail.wechat.dto;

import club.xyes.zkh.retail.commons.xml.annotation.XmlProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/30 0030 18:12
 * WxWithdrawResult
 * 微信提现返回结果
 *
 * @author 郭文梁
 * @data 2019/5/30 0030
 */
@Data
public class WxWithdrawResult {
    @XmlProperty(name = "return_code")
    private String returnCode;
    @XmlProperty(name = "return_msg")
    private String returnMsg;
    @XmlProperty(name = "result_code")
    private String resultCode;
    @XmlProperty(name = "err_code")
    private String errCode;
    @XmlProperty(name = "err_code_des")
    private String errCodeDes;
    @XmlProperty(name = "mch_billno")
    private String mchBillNo;
    @XmlProperty(name = "mch_id")
    private String mchId;
    @XmlProperty(name = "wxappid")
    private String wxAppId;
    @XmlProperty(name = "re_openid")
    private String reOpenid;
    @XmlProperty(name = "total_amount")
    private String totalAmount;

    /**
     * 是否提现成功
     *
     * @return 提现成功？
     */
    public boolean isSuccess() {
        return Objects.equals("SUCCESS", returnCode)
                &&
                Objects.equals("SUCCESS", resultCode);
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public String getErrorMsg() {
        return returnMsg;
    }
}
