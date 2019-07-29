package club.xyes.zkh.retail.wechat.utils;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.utils.CipherUtil;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Create by 郭文梁 2019/6/3 0003 08:57
 * WxPaySignUtil
 * 微信支付签名工具类
 *
 * @author 郭文梁
 * @data 2019/6/3 0003
 */
public class WxSignUtil {
    private static final String DEFAULT_AES_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String AES_KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CHARSET = ApplicationConstants.DEFAULT_CHARSET;
    private static final String HMAC_SHA256 = "HmacSHA256";
    /**
     * 签名方式 MD5
     */
    public static final String SIGN_TYPE_MD5 = "MD5";
    /**
     * 签名方式：HMAC-SHA256
     */
    public static final String SIGN_TYPE_HMAC_SHA256 = "HMAC-SHA256";

    /**
     * MD5签名
     *
     * @param params 参数
     * @param key    签名KEY
     * @return 签名结果
     */
    public static String signMd5(Map<String, String> params, String key) {
        return sign(params, key, SIGN_TYPE_MD5);
    }

    /**
     * SHA256签名
     *
     * @param params 参数
     * @param key    KEY
     * @return 签名结果
     */
    public static String signSha256(Map<String, String> params, String key) {
        return sign(params, key, SIGN_TYPE_HMAC_SHA256);
    }

    /**
     * 对微信支付参数签名
     *
     * @param params   参数
     * @param key      秘钥
     * @param signType 签名方式
     * @return 签名结果
     */
    public static String sign(Map<String, String> params, String key, String signType) {
        List<String> paramsList = asParamsList(params);
        String signString = asSignString(paramsList, key);
        String sign;
        switch (signType) {
            case SIGN_TYPE_MD5:
                sign = doMd5Sign(signString);
                break;
            case SIGN_TYPE_HMAC_SHA256:
                sign = doSha256Sign(signString, key);
                break;
            default:
                throw new IllegalArgumentException("Unknown sign type:" + signType);
        }
        return sign;
    }

    /**
     * 转换为参数列表（KV pairs）
     *
     * @param params 参数
     * @return 参数列表
     */
    private static List<String> asParamsList(Map<String, String> params) {
        List<String> paramsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String paramString = String.format("%s=%s&", entry.getKey(), entry.getValue());
            paramsList.add(paramString);
        }
        Collections.sort(paramsList);
        return paramsList;
    }

    /**
     * 转换为待签名字符串
     *
     * @param paramsList 参数列表
     * @param key        签名KEY
     * @return 待签名字符串
     */
    private static String asSignString(List<String> paramsList, String key) {
        StringBuilder sb = new StringBuilder();
        for (String str : paramsList) {
            sb.append(str);
        }
        sb.append("key=");
        sb.append(key);
        return sb.toString();
    }

    /**
     * 执行签名操作
     *
     * @param str 待签名字符串
     * @return 签名结果
     */
    private static String doMd5Sign(String str) {
        return DigestUtils.md5Hex(str).toUpperCase();
    }

    /**
     * sha256签名
     *
     * @param str 签名字符串
     * @param key key
     * @return 结果
     */
    private static String doSha256Sign(String str, String key) {
        try {
            Mac sha256Hmac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(DEFAULT_CHARSET), HMAC_SHA256);
            sha256Hmac.init(secretKey);
            byte[] array = sha256Hmac.doFinal(str.getBytes(DEFAULT_CHARSET));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            throw new InternalServerErrorException("Sha256 sign error!", e);
        }
    }

    /**
     * 解码退款通知请求数据
     *
     * @param reqInfo 请求字符串
     * @param key     key
     * @return 解码结果
     */
    public static String decodeNotifyReqInfo(String reqInfo, String key) {
        try {
            key = DigestUtils.md5Hex(key).toLowerCase();
            final SecretKeySpec keySpec = new SecretKeySpec(key.toLowerCase().getBytes(), AES_KEY_ALGORITHM);
            final Cipher cipher = Cipher.getInstance(DEFAULT_AES_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return CipherUtil.doFinalWithBase64Content(cipher, reqInfo);
        } catch (Exception e) {
            throw new InternalServerErrorException("Could not decrypt notify content with key:" + key, e);
        }
    }
}
