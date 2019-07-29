package club.xyes.zkh.retail.wechat.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/6/21 0021 08:43
 * WxTemplateMsgResult
 * 微信模板消息发送结果包装类
 *
 * @author 郭文梁
 * @data 2019/6/21 0021
 */
@Data
public class WxTemplateMsgResult {
    @JSONField(name = "errcode")
    @JsonProperty("errcode")
    private String errCode;
    @JSONField(name = "errmsg")
    @JsonProperty("errmsg")
    private String errMsg;
    @JSONField(name = "msgid")
    @JsonProperty("msgid")
    private String msgId;

    /**
     * 是否发送成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return Objects.equals(errCode, "0");
    }
}
