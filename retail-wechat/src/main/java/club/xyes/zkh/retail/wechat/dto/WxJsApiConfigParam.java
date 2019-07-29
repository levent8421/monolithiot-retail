package club.xyes.zkh.retail.wechat.dto;

import lombok.Data;

import java.util.List;

/**
 * Create by 郭文梁 2019/5/18 0018 14:44
 * WxJsApiConfigParam
 * 微信JSApi配置参数
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@Data
public class WxJsApiConfigParam {
    private boolean debug = true;
    private String appId;
    private long timestamp;
    private String nonceStr;
    private String signature;
    private List<String> jsApiList;
}
