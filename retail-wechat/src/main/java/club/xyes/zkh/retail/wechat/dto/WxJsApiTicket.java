package club.xyes.zkh.retail.wechat.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Create by 郭文梁 2019/5/18 0018 14:28
 * WxJsApiTicket
 * 微信JsApiTicket
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@Data
public class WxJsApiTicket {
    @JSONField(name = "errcode")
    @JsonProperty("errcode")
    private Integer errCode;
    @JSONField(name = "errmsg")
    @JsonProperty("errmsg")
    private String errMsg;
    private String ticket;
    @JSONField(name = "expires_in")
    @JsonProperty("expires_in")
    private Integer expiresIn;
    /**
     * 最后一次刷新时间
     */
    private long refreshTime;

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
