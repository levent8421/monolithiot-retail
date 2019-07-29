package club.xyes.zkh.retail.wechat.dto;

/**
 * Create by 郭文梁 2019/5/18 0018 17:17
 * SourceJsonAware
 * 接口 表明对象需要知晓创建其的源JSON
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
public interface SourceJsonAware {
    /**
     * 设置源JSON
     *
     * @param json JSON
     */
    void setSourceJson(String json);
}
