package club.xyes.zkh.retail.wechat.dto;

import lombok.Data;

/**
 * Create by 郭文梁 2019/6/6 0006 18:03
 * WxPayParams
 * 微信支付前端参数
 *
 * @author 郭文梁
 * @data 2019/6/6 0006
 */
@Data
public class WxPayParams {
    private String appId;
    private String timestamp;
    private String nonceStr;
    private String packageStr;
    private String signType;
    private String paySign;
}
