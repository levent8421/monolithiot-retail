package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.dto.CommonHttpResponse;
import lombok.val;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Create by 郭文梁 2019/7/17 9:18
 * CommonHttpUtils
 * 通用Http工具类
 *
 * @author 郭文梁
 * @data 2019/7/17 9:18
 */
public class CommonHttpUtils {
    /**
     * 参数与url的分隔符
     */
    private static final String PARAMS_DELIMITER = "?";

    /**
     * 获取默认的http客户端
     *
     * @return http客户端
     */
    private static CloseableHttpClient getClient() {
        return HttpClients.createDefault();
    }

    /**
     * 发送Http get请求
     *
     * @param url    请求地址
     * @param params 参数
     * @return Response
     */
    public static CommonHttpResponse get(String url, Map<String, String> params) throws IOException {
        try (val client = getClient()) {
            val fullUrl = resolveGetUrl(url, params);
            val get = new HttpGet(fullUrl);
            try (val response = client.execute(get)) {
                val res = new CommonHttpResponse();
                val statusLine = response.getStatusLine();
                res.setBody(EntityUtils.toString(response.getEntity()));
                res.setCode(statusLine.getStatusCode());
                res.setMsg(statusLine.getReasonPhrase());
                return res;
            }
        }
    }

    /**
     * 处理Http get的请求路径
     *
     * @param url    url
     * @param params 参数
     * @return http get full url
     */
    private static String resolveGetUrl(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        if (!url.contains(PARAMS_DELIMITER)) {
            //url未包含参数
            url += PARAMS_DELIMITER;
        }
        StringBuilder sb = new StringBuilder(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb.toString();
    }
}
