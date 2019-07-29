package club.xyes.zkh.retail.wechat.api.impl;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.wechat.dto.SourceJsonAware;
import club.xyes.zkh.retail.wechat.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.security.KeyStore;

/**
 * Create by 郭文梁 2019/5/18 0018 13:46
 * AbstractHttpApi
 * Http方式的Api基类
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
abstract class AbstractHttpApi {
    /**
     * 发送Get请求
     *
     * @param url Url
     * @return response string
     */
    String get(String url) {
        try {
            return HttpUtils.get(url);
        } catch (IOException e) {
            throw new InternalServerErrorException("Http get[" + url + "]", e);
        }
    }

    /**
     * 发送POSt请求 contentType = xml
     *
     * @param url  地址
     * @param body 内容
     * @return 响应
     */
    String postXml(String url, String body) {
        try {
            return HttpUtils.postXml(url, body);
        } catch (IOException e) {
            throw new InternalServerErrorException("Http post xml [" + url + "],error[" + e.getMessage() + "]", e);
        }
    }

    /**
     * 发送Get请求，并将接管反序列化（json）
     *
     * @param url           请求地址
     * @param responseClass class
     * @param <T>           类型
     * @return 反序列化结果
     */
    <T> T get(String url, Class<T> responseClass) {
        String jsonString = get(url);
        T res = JSON.parseObject(jsonString, responseClass);
        if (res instanceof SourceJsonAware) {
            ((SourceJsonAware) res).setSourceJson(jsonString);
        }
        return res;
    }

    /**
     * 方get请求，并将响应结果反序列化，
     * 当检查器返回false时抛出InternalServerErrorException异常
     *
     * @param url           Api地址
     * @param responseClass 响应结果class
     * @param checker       检查器
     * @param <T>           反序列化类型
     * @return T
     */
    <T> T get(String url, Class<T> responseClass, JsonResponseChecker checker) {
        String response = get(url);
        JSONObject jsonObject = JSON.parseObject(response);
        if (checker.isSuccess(jsonObject)) {
            T res = jsonObject.toJavaObject(responseClass);
            if (res instanceof SourceJsonAware) {
                ((SourceJsonAware) res).setSourceJson(response);
            }
            return res;
        }
        throw new InternalServerErrorException("Http get[" + url + "]:response [" + response + "]is unexpect!");
    }

    /**
     * 发送Post请求 contenttype = json
     *
     * @param url  地址
     * @param body 请求体
     * @return 响应
     * @throws IOException IO异常
     */
    String postJson(String url, String body) throws IOException {
        return HttpUtils.post(url, body, ApplicationConstants.Http.CONTENT_TYPE_JSON_UTF8);
    }

    /**
     * 发送Post请求 contenttype = json
     *
     * @param url           地址
     * @param body          请求体
     * @param responseClass 响应接受类
     * @return 响应
     * @throws IOException IO异常
     */
    <T> T postJson(String url, String body, Class<T> responseClass) throws IOException {
        final String reasp = postJson(url, body);
        return JSON.parseObject(reasp, responseClass);
    }

    /**
     * 发送带证书的Http请求
     *
     * @param url      URL
     * @param body     内容
     * @param keyStore 证书文件
     * @param password 密码
     * @return 响应内容
     */
    static String postXmlWithP12(String url, String body, KeyStore keyStore, String password) {
        return HttpUtils.postXmlWithKeyStore(url, body, keyStore, password);
    }

    /**
     * 响应检查器
     */
    @FunctionalInterface
    public interface JsonResponseChecker {
        /**
         * 检查接口调用是否成功
         *
         * @param response 响应数据
         * @return 是否成功
         */
        boolean isSuccess(JSONObject response);
    }
}
