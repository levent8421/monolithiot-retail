package club.xyes.zkh.retail.wechat.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Create by 郭文梁 2019/4/29 0029 17:22
 * WxAccessToken
 * 微信AccessToken包装类
 *
 * @author 郭文梁
 * @data 2019/4/29 0029
 */
@Data
public class WxAccessToken implements SourceJsonAware {
    /**
     * 刷新时间
     */
    private long refreshTime;
    /**
     * Access Token
     */
    @JSONField(name = "access_token")
    @JsonProperty("access_token")
    private String accessToken;
    /**
     * 过期时间
     */
    @JSONField(name = "expires_in")
    @JsonProperty("expires_in")
    private Integer expiresIn;
    /**
     * 源JSOn
     */
    private String sourceJson;

    /**
     * 判断当前Token是否已过期
     *
     * @return 是否已过期
     */
    public boolean expired() {
        if (expiresIn == null) {
            return true;
        }
        long now = System.currentTimeMillis() / 1000;
        return now - refreshTime > expiresIn;
    }
}
