package club.xyes.zkh.retail.commons.utils;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Create by 郭文梁 2019/6/5 0005 17:09
 * QrCodeUtilTest
 * 二维码工具测试
 *
 * @author 郭文梁
 * @data 2019/6/5 0005
 */
public class QrCodeUtilTest {

    @Test
    public void generateShareQrCodeImage() throws IOException {
        String bgFile = "D:\\bg.jpg";
        String content = "Text Content";
        int with = 500;
        int height = 500;
        String targetFile = "D:\\target.png";

        final BufferedImage bg = ImageIO.read(new File(bgFile));
        final byte[] result = QrCodeUtil.generateShareQrCodeImage(content, with, height, bg);

        final FileOutputStream fos = new FileOutputStream(targetFile);
        fos.write(result);
        fos.close();
    }
}