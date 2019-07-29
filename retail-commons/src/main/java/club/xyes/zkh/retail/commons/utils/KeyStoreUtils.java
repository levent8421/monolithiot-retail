package club.xyes.zkh.retail.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * Create by 郭文梁 2019/6/3 0003 17:53
 * KeyStoreUtils
 * 证书工具类
 *
 * @author 郭文梁
 * @data 2019/6/3 0003
 */
@Slf4j
public class KeyStoreUtils {
    /**
     * 加载证书文件
     *
     * @param path     文件地址
     * @param password 证书密码
     * @return KeyStore
     */
    public static KeyStore loadKey(String path, String password) {
        log.info("Load Secret key from file [{}]", path);
        try (FileInputStream fis = FileUtils.openInputStream(new File(path))) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(fis, password.toCharArray());
            return keyStore;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
