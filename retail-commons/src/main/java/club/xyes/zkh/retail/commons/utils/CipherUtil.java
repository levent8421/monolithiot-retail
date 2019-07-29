package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Create by 郭文梁 2019/4/19 0019 10:44
 * CipherUtil
 * 加密工具类
 *
 * @author 郭文梁
 * @data 2019/4/19 0019
 */
public class CipherUtil {
    /**
     * 执行加解密操作
     *
     * @param cipher  加解密器
     * @param content 内容
     * @return 结果
     */
    public static byte[] doFinal(Cipher cipher, byte[] content) {
        try {
            return cipher.doFinal(content);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加解密 并将结果装换位Base64
     *
     * @param cipher  加密器
     * @param content 加密内容
     * @return 加密结果
     */
    private static String doFinalAsBase64(Cipher cipher, byte[] content) {
        try {
            byte[] res = cipher.doFinal(content);
            return Base64Utils.encodeToString(res);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加解密
     *
     * @param cipher  加密器
     * @param content 加密内容
     * @return 加密结果
     */
    public static String doFinalAsBase64(Cipher cipher, String content) {
        try {
            byte[] bytes = content.getBytes(ApplicationConstants.DEFAULT_CHARSET);
            return doFinalAsBase64(cipher, bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载秘钥
     *
     * @param key       秘钥字符串
     * @param algorithm 秘钥类型
     * @param length    秘钥长度
     * @return 秘钥对象
     */
    public static SecretKeySpec loadKey(String key, String algorithm, int length) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(algorithm);
            kg.init(length, new SecureRandom(key.getBytes()));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * d对base64内容进行加解密
     *
     * @param decoder 加解密器
     * @param base64  内容
     * @return 结果
     */
    public static String doFinalWithBase64Content(Cipher decoder, String base64) {
        byte[] content = Base64Utils.decodeFromString(base64);
        byte[] bytes = doFinal(decoder, content);
        try {
            return new String(bytes, ApplicationConstants.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
