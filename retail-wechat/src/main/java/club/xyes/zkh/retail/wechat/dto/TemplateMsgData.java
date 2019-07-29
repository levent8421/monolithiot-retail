package club.xyes.zkh.retail.wechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create by 郭文梁 2019/6/21 0021 08:50
 * TemplateMsgData
 * 模板消息参数数据
 *
 * @author 郭文梁
 * @data 2019/6/21 0021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateMsgData {
    public static final String DEFAULT_COLOR = "#000000";
    private String value;
    private String color;
}
