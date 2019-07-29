package club.xyes.zkh.retail.service.encrypt;

/**
 * Create by 郭文梁 2019/4/19 0019 10:54
 * AccessTokenEncoder
 * 范文令牌编码器
 *
 * @author 郭文梁
 * @data 2019/4/19 0019
 */
public interface AccessTokenEncoder {
    /**
     * 加密
     *
     * @param content 加密内容
     * @return 加密结果
     */
    String encode(String content);

    /**
     * 加密
     *
     * @param content 加密内容
     * @param prefix  前缀
     * @return 加密结果
     */
    String encode(String content, String prefix);

    /**
     * 解密
     *
     * @param token token字符串
     * @param type  解密结果类型
     * @param <T>   解密结果类型泛型
     * @return 解密结果
     */
    <T> T decode(String token, Class<T> type);
}
