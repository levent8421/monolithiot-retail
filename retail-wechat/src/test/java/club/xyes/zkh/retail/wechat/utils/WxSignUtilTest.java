package club.xyes.zkh.retail.wechat.utils;

import club.xyes.zkh.retail.commons.utils.XmlUtils;
import club.xyes.zkh.retail.wechat.dto.WxRefundNotifyParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WxSignUtilTest {

    @Test
    public void decodeNotifyReqInfo() throws Exception {
        String content = "<xml><return_code>SUCCESS</return_code><appid><![CDATA[wx45725d38c171c33f]]></appid><mch_id><![CDATA[1538466881]]></mch_id><nonce_str><![CDATA[38b62f4e11e1ce9d5de77c0e180fef00]]></nonce_str><req_info><![CDATA[eWJuStRRj25K2zdfBZYzXVfAVLerdzZOGkfdM0TAg4pLlER2aOgfssLFFye3ZoE7//7gy1qTF8jFyS8R+QrD4tHGIKJdRDn1PgJ01BqHgY9Qkvjbo9lJHZenJojtfV7gCPMaS7CscD602admAriuRelZxAGdWEYru2fmq5gdHgWFkJkjMMO6rXW8X/Mwmvha992uOTd3GICacw7bcB2x2sdY1izL7m0ftUr79n53uB88EWJoYxGPPcKGr2oAcShV4X08KryDOPdRyTGkezHN9ey7fsCsApY9tiCkgF4Fz8jUS7cUDvfDoy/K/ydqflE3aVy9dzGwO9QOOvTeh7HQTmk6wvQsfJOyhsgM8xYsSIloD8zhtwQoAsWUP/yLvuMqq6CeRrpePkkLAaMnDGrEenQXJSs4/9D2ue3HfE6rrZnOtEcIrhs+mFA6wumVfqjwI4oAhElzjDY3Go16QDauDGvt0nTxjwZsvXbqVUJlDkZAOkYAo8iNuksew27pPOQwugcLLB/i2XwOI/2apLSs39v7+8JNKTUFO25QUhyLJKxJc+sm5WkFq1cJy76q/Kc+wUXjla3VoSEa24rypWfuWjKIMmujc5fLDOZ4KxL8rnaV80aBUgrl1dyipk03wrMkHVTgmKOS9UBNc405x20PJukxPhYL6FA5TAUble1+GKj/PdTykpqpp63kxqaWo4RxfZO3u+9kB4ogx0HpEmjDnAgIJ+GGg/bknDD77P5LKFA4LC55W/OdGtPo5N7JgmVoC+qBuS+HL3VfcWr6PzSQEObByBihAjKvwrjUA9jAMctkc8f9CDzgvZzgVeEIXjzPh7YjH5lLi3vu6aehtfYz7o11Elx+jC8NfIcvM2jKKRx+WaGWNhaNl/7t5C49XWHQu/gO0gY5zyJcjC7rcf2Qg7reAjVB+HikjZRW4sH6SnQIvgV/ZOLgkEl8JHGShqj9GPtMnkv8GIJNZ/Xw7ChKbQJrFMFM2w/n4pDl6QTB7zaHQ/ZMjA2WyrUrOQZhxOXE3zZvuz+uu9oHHg+uZ4HXdr+mltCS1DeZTl2akY0P1WuJOUo99gfHKt2IezRaIP5r+BFW6jpcxi5EHWKkNmB5NQ==]]></req_info></xml>";

        final WxRefundNotifyParam param = XmlUtils.parseObject(content, WxRefundNotifyParam.class);
        final String reqInfo = param.getReqInfo();
        String key = "0dd1c031210041f0963526448e163f58";
        final String result = WxSignUtil.decodeNotifyReqInfo(reqInfo, key);
        log.debug("[{}]", result);
    }

    @Test
    public void testSign() {
        Map<String, String> param = new HashMap<>(16);
        param.put("nonce_str", "2mzjCy3f2NbXwk1");
        param.put("out_trade_no", "c4cfe396bb1240b5a9bfa58aadfb9ffb");
        param.put("out_refund_no", "ce68ac26afe64c9ba3ea606994bd3fcc");
        param.put("appid", "wx62f498b5f0e523fb");
        param.put("total_fee", "38800");
        param.put("refund_fee", "38800");
        param.put("mch_id", "1540413381");
        param.put("notify_url", "");
        param.put("sign_type", "MD5");
        String sign = WxSignUtil.signMd5(param, "96e79218965eb72c92a549dd5a330112");
        param.put("sign", sign);
        log.debug(XmlUtils.asXml(param, "xml"));
    }
}