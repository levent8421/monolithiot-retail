package club.xyes.zkh.retail.map.util;

import lombok.val;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Create by 郭文梁 2019/7/17 9:40
 * MapSignUtils
 * 地图接口相关签名工具类
 *
 * @author 郭文梁
 * @data 2019/7/17 9:40
 */
public class MapSignUtils {
    /**
     * MD5 签名参数
     *
     * @param params 参数
     * @param key    签名密钥
     * @return 签名结果
     */
    public static String md5Sign(Map<String, String> params, String key) {
        final List<String> paramTextList = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            val text = entry.getKey() + "=" + entry.getValue();
            paramTextList.add(text);
        }
        paramTextList.sort(String::compareTo);
        StringBuilder sb = new StringBuilder();
        for (val text : paramTextList) {
            sb.append(text).append("&");
        }
        sb.append(key);
        return DigestUtils.md2Hex(sb.toString());
    }
}
