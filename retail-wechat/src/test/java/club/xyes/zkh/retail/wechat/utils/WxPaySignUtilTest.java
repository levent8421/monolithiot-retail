package club.xyes.zkh.retail.wechat.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by 郭文梁 2019/6/3 0003 09:06
 * WxPaySignUtilTest
 * 测试卫星签名
 *
 * @author 郭文梁
 * @data 2019/6/3 0003
 */
@Slf4j
public class WxPaySignUtilTest {

    @Test
    public void sign() {
        String key = "192006250b4c09247ec02edce69f6a2d";
        Map<String, String> params = new HashMap<>(5);
        params.put("appid", "wxd930ea5d5a258f4f");
        params.put("mch_id", "10000100");
        params.put("device_info", "1000");
        params.put("body", "test");
        params.put("nonce_str", "ibuaiVcKdpRxkhJA");
        String sign = WxSignUtil.sign(params, key, WxSignUtil.SIGN_TYPE_MD5);
        log.info("sign md5 = [{}]", sign);
        String sign2 = WxSignUtil.sign(params, key, WxSignUtil.SIGN_TYPE_HMAC_SHA256);
        log.info("sign sha256 = [{}]", sign2);
    }
}