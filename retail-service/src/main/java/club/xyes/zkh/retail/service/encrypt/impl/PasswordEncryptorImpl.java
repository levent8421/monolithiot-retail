package club.xyes.zkh.retail.service.encrypt.impl;

import club.xyes.zkh.retail.service.encrypt.PasswordEncryptor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Create by 郭文梁 2019/5/21 0021 13:59
 * PasswordEncryptorImpl
 * 密码加解密器实现 使用MD5实现
 *
 * @author 郭文梁
 * @data 2019/5/21 0021
 */
@Component
public class PasswordEncryptorImpl implements PasswordEncryptor {
    @Override
    public String encode(String password) {
        return DigestUtils.md5Hex(password);
    }

    @Override
    public boolean matches(String encoded, String password) {
        String otherEncoded = encode(password);
        return Objects.equals(encoded, otherEncoded);
    }
}
