package club.xyes.zkh.retail.commons.dto;

import lombok.Data;

/**
 * Create by 郭文梁 2019/7/17 9:19
 * CommonHttpResponse
 * 通用的http放回数据封装
 *
 * @author 郭文梁
 * @data 2019/7/17 9:19
 */
@Data
public class CommonHttpResponse {
    private String msg;
    private int code;
    private String body;
}
