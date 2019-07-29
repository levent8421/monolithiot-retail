package club.xyes.zkh.retail.service.encrypt.impl;

import club.xyes.zkh.retail.commons.utils.CipherUtil;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import com.alibaba.fastjson.JSON;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Create by 郭文梁 2019/4/19 0019 10:57
 * AccessTokenEncoderImpl
 * AccessToken、编码器实现
 *
 * @author 郭文梁
 * @data 2019/4/19 0019
 */
@Component
@ConfigurationProperties(prefix = "access-token")
@Slf4j
public class AccessTokenEncoderImpl implements AccessTokenEncoder {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final int KEY_LENGTH = 128;
    private Cipher encoder;
    private Cipher decoder;
    @Setter
    private String key = "levent8421";

    /**
     * Spring结束构造后初始化
     */
    @PostConstruct
    public void init() {
        try {
            SecretKeySpec secretKeySpec = CipherUtil.loadKey(key, KEY_ALGORITHM, KEY_LENGTH);
            encoder = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            encoder.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            decoder = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            decoder.init(Cipher.DECRYPT_MODE, secretKeySpec);
            log.info("AccessTokenEncoder init success with key [{}]", key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String encode(String content) {
        return CipherUtil.doFinalAsBase64(encoder, content);
    }

    @Override
    public String encode(String content, String prefix) {
        String token = encode(content);
        return prefix + token;
    }

    @Override
    public <T> T decode(String token, Class<T> type) {
        String json = CipherUtil.doFinalWithBase64Content(decoder, token);
        return JSON.parseObject(json, type);
    }
}
