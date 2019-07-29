package club.xyes.zkh.retail.wechat.utils;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

/**
 * Create by 郭文梁 2019/4/19 0019 14:03
 * HttpUtils
 * Http相关工具类
 *
 * @author 郭文梁
 * @data 2019/4/19 0019
 */
public class HttpUtils {
    private static final String DEFAULT_CHARSET = "UTF-8";
    public static final String DEFAULT_RESPONSE_CHARSET = "UTF-8";

    /**
     * 获取客户端
     *
     * @return 客户端对象
     */
    private static CloseableHttpClient getClient() {
        return HttpClients.createDefault();
    }

    /**
     * POST请求
     *
     * @param url         URL
     * @param body        BODY
     * @param contentType Content-Type Header
     * @return Response
     * @throws IOException IO异常
     */
    public static String post(String url, String body, String contentType) throws IOException {
        try (CloseableHttpClient client = getClient()) {
            return doPost(client, url, body);
        }
    }

    /**
     * 发送Post请求 请求体为JSON
     *
     * @param url  地址
     * @param body 请求体
     * @return 响应结果
     */
    public static String postJson(String url, String body) throws IOException {
        return post(url, body, ApplicationConstants.Http.CONTENT_TYPE_JSON_UTF8);
    }

    /**
     * 发起Get请求
     *
     * @param url 请求地址
     * @return 字符串响应内容
     * @throws IOException IO异常
     */
    public static String get(String url) throws IOException {
        try (CloseableHttpClient client = getClient()) {
            HttpGet get = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(get)) {
                return response2String(response);
            }
        }
    }

    /**
     * 将Http响应转换为字符串
     *
     * @param response 响应对象
     * @return 字符创
     * @throws IOException IO异常
     */
    private static String response2String(CloseableHttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        } else {
            throw new IOException("Response status code is " + response.getStatusLine().getStatusCode());
        }
    }

    /**
     * 发送POST请求 内容为xml
     *
     * @param url  路径
     * @param body 内容
     * @return 响应
     * @throws IOException IO异常
     */
    public static String postXml(String url, String body) throws IOException {
        return post(url, body, ApplicationConstants.Http.CONTENT_TYPE_XML_URT8);
    }

    /**
     * 发送带证书的Http请求
     *
     * @param url      URL
     * @param body     请求体
     * @param keyStore 证书
     * @return 响应内容
     */
    public static String postXmlWithKeyStore(String url, String body, KeyStore keyStore, String password) {
        try (CloseableHttpClient client = buildSecurityClient(keyStore, password)) {
            return doPost(client, url, body);
        } catch (Exception e) {
            throw new InternalServerErrorException("Post with p12 error", e);
        }
    }

    /**
     * 执行POST操作
     *
     * @param client 客户端
     * @param url    地址
     * @param body   内容
     * @return 响应
     * @throws IOException IO异常
     */
    private static String doPost(CloseableHttpClient client, String url, String body) throws IOException {
        HttpPost post = new HttpPost(url);
        StringEntity entity = new StringEntity(body, DEFAULT_CHARSET);
        entity.setContentType(ApplicationConstants.Http.CONTENT_TYPE_XML_URT8);
        post.setEntity(entity);
        try (CloseableHttpResponse response = client.execute(post)) {
            return response2String(response);
        }
    }

    /**
     * 构建带证书的Client
     *
     * @param keyStore 证书
     * @param password 密码
     * @return Client
     * @throws Exception 异常
     */
    private static CloseableHttpClient buildSecurityClient(KeyStore keyStore, String password) throws Exception {
        HttpClientBuilder builder = HttpClientBuilder.create();
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0,
                                           String arg1) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0,
                                           String arg1) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        String alg = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(alg);
        keyManagerFactory.init(keyStore, password.toCharArray());
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
        sslcontext.init(keyManagers, new TrustManager[]{tm}, null);
        builder.setSSLContext(sslcontext);
        return builder.build();
    }
}

