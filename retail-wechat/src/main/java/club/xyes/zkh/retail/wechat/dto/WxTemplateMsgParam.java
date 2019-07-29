package club.xyes.zkh.retail.wechat.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * Create by 郭文梁 2019/6/21 0021 08:47
 * WxTemplateMsgParam
 * 微信模板消息发送参数
 *
 * @author 郭文梁
 * @data 2019/6/21 0021
 */
@Data
public class WxTemplateMsgParam {
    public static final String DEFAULT_TOP_COLOR = "#FF0000";
    @JsonProperty("touser")
    @JSONField(name = "touser")
    private String toUser;
    @JsonProperty("template_id")
    @JSONField(name = "template_id")
    private String templateId;
    private String url;
    @JsonProperty("topcolor")
    @JSONField(name = "topcolor")
    private String topColor = DEFAULT_TOP_COLOR;
    private Map<String, TemplateMsgData> data;
}
