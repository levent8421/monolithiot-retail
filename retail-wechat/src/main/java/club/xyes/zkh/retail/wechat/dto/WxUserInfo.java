package club.xyes.zkh.retail.wechat.dto;

import lombok.Data;

import java.util.List;

/**
 * Create by 郭文梁 2019/5/18 0018 17:46
 * WxUserInfo
 * 卫星用户信息
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@Data
public class WxUserInfo implements SourceJsonAware {
    private String openid;
    private String nickname;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private List<String> privilege;
    private String unionid;
    /**
     * 源 JSON
     */
    private String sourceJson;
}
