package club.xyes.zkh.retail.service.encrypt;

/**
 * Create by 郭文梁 2019/5/21 0021 13:57
 * PasswordEncryptor
 * 密码加解密器
 *
 * @author 郭文梁
 * @data 2019/5/21 0021
 */
public interface PasswordEncryptor {
    /**
     * 加密密码
     *
     * @param password 明文
     * @return 密文
     */
    String encode(String password);

    /**
     * 检查是否匹配
     *
     * @param encoded  加密过的字符串
     * @param password 待匹配明文
     * @return 是否匹配
     */
    boolean matches(String encoded, String password);
}
