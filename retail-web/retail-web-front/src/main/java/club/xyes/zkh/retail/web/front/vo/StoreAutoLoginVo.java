package club.xyes.zkh.retail.web.front.vo;

import lombok.Data;

/**
 * Create by 郭文梁 2019/5/31 0031 11:04
 * StoreAutoLoginVo
 * 商铺自动登录参数
 *
 * @author 郭文梁
 * @data 2019/5/31 0031
 */
@Data
public class StoreAutoLoginVo {
    /**
     * 登录OAuth Code
     */
    private String code;
    /**
     * 微信回传State
     */
    private String state;
}
