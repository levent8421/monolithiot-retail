package club.xyes.zkh.retail.wechat.dto;

import club.xyes.zkh.retail.commons.utils.TextUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Create by 郭文梁 2019/5/18 0018 16:51
 * WxOAuth2AccessToken
 * 微信Oauth2AccessToken接口返回内容
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@Data
public class WxOAuth2AccessToken implements SourceJsonAware {
    @JSONField(name = "access_token")
    @JsonProperty("access_token")
    private String accessToken;
    @JSONField(name = "expires_in")
    @JsonProperty("expires_in")
    private String expiresIn;
    @JSONField(name = "refresh_token")
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JSONField(name = "openid")
    @JsonProperty("openid")
    private String openId;
    private String scope;
    /**
     * 源JSON
     */
    private String sourceJson;

    /**
     * 判断是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return !TextUtils.isTrimedEmpty(openId);
    }
}
