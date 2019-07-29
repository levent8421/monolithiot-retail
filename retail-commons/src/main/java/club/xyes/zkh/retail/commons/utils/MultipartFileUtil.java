package club.xyes.zkh.retail.commons.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Create by 郭文梁 2019/4/17 0017 13:06
 * MultipartFileUtil
 * springmvc文件工具类
 *
 * @author 郭文梁
 * @data 2019/4/17 0017
 */
public class MultipartFileUtil {
    /**
     * 文件扩展名分隔符
     */
    private static final String EXTENSIONS_DELIMITER = ".";

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名
     */
    public static String getExtensions(String filename) {
        int delimiterIndex = filename.lastIndexOf(EXTENSIONS_DELIMITER);
        if (delimiterIndex >= 0) {
            return filename.substring(delimiterIndex);
        } else {
            return "";
        }
    }

    /**
     * 随机文件名
     *
     * @param file 文件
     * @return 随机文件名
     */
    public static String randomFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extensions;
        if (originalFilename == null) {
            extensions = "";
        } else {
            extensions = getExtensions(originalFilename);
        }
        return RandomUtils.randomPrettyUUIDString().concat(extensions);
    }

    /**
     * 保存上传文件到本地
     *
     * @param file 文件
     * @param path 保存路径
     * @return 文件名
     * @throws IOException IO异常
     */
    public static String save(MultipartFile file, String path) throws IOException {
        File pathFile = new File(path);
        if (pathFile.exists()) {
            if (!pathFile.isDirectory()) {
                throw new IOException("The target dir name are early exists，but it is not a Directory!");
            }
        } else {
            if (!pathFile.mkdirs()) {
                throw new IOException("Unable to mkdir:" + path);
            }
        }
        String filename = randomFileName(file);
        File targetFile = new File(pathFile, filename);
        file.transferTo(targetFile);
        return filename;
    }
}

