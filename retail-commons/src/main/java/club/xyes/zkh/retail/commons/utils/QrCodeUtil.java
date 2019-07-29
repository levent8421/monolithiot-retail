package club.xyes.zkh.retail.commons.utils;

import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Create by 郭文梁 2019/5/11 0011 14:39
 * QrCodeUtil
 * 二维码工具类
 *
 * @author 郭文梁
 * @data 2019/5/11 0011
 */
@Slf4j
public class QrCodeUtil {
    private static final int DEFAULT_MARGIN = 2;
    /**
     * 二维码图片格式
     */
    private static final String IMAGE_FORMAT = "png";

    /**
     * 创建二维码
     *
     * @param contents 内容
     * @param width    宽度
     * @param height   高度
     * @return bytes
     */
    public static byte[] createQrCode(String contents, int width, int height) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            createQrCode(contents, width, height, out);
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
    }

    /**
     * 生成二维码并写入到流中
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @param out     流
     * @throws IOException IO异常
     */
    public static void createQrCode(String content, int width, int height, OutputStream out) throws IOException {
        try {
            BufferedImage image = toBufferedImage(content, width, height);
            //转换成png格式的IO流
            ImageIO.write(image, IMAGE_FORMAT, out);
        } catch (WriterException e) {
            throw new InternalServerErrorException(e);
        }
    }

    /**
     * 生成二维码的BufferedImage
     *
     * @param content 内容
     * @param width   宽度
     * @param height  高度
     * @return BufferImage
     * @throws WriterException 异常
     */
    private static BufferedImage toBufferedImage(String content, int width, int height) throws WriterException {
        return toBufferedImage(content, width, height, DEFAULT_MARGIN);
    }

    /**
     * 生成二维码的BufferedImage
     *
     * @param content 内容
     * @param width   宽度
     * @param height  高度
     * @param margin  边距
     * @return BufferImage
     * @throws WriterException 异常
     */
    private static BufferedImage toBufferedImage(String content, int width, int height, int margin) throws WriterException {
        HashMap<EncodeHintType, Object> hints = new HashMap<>(2);
        hints.put(EncodeHintType.MARGIN, margin);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                content, BarcodeFormat.QR_CODE, width, height, hints);
        // 1、读取文件转换为字节数组
        return toBufferedImage(bitMatrix);
    }

    /**
     * image流数据处理
     *
     * @author ianly
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * 生成分享二维码图片图片
     *
     * @param content    二维码内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param background 背景图
     * @return 分享图片内容
     */
    public static byte[] generateShareQrCodeImage(String content, int width, int height, BufferedImage background) {
        final int bdHeight = background.getHeight();
        try {
            final BufferedImage qrCode = toBufferedImage(content, width, height, 0);
            final Graphics g = background.getGraphics();
            boolean result = g.drawImage(qrCode, 23, bdHeight - height - 12, null);
            log.debug("Draw result {}", result);
            g.dispose();
            return image2Bytes(background);
        } catch (WriterException | IOException e) {
            throw new InternalServerErrorException("Create share qrcode error!", e);
        }
    }

    /**
     * 转换Image为二进制数组
     *
     * @param image 图片文件
     * @return 二进制数组
     * @throws IOException IO异常
     */
    private static byte[] image2Bytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, IMAGE_FORMAT, out);
        out.close();
        return out.toByteArray();
    }

    /**
     * 生成加入团队二维码
     *
     * @param content     二维码内容
     * @param width       二维码宽度
     * @param height      二维码高度
     * @param bgImageFile 背景图文件
     * @return bytes
     */
    public static byte[] generateTeamQrCode(String content, int width, int height, File bgImageFile) {
        try {
            final BufferedImage qrCode = toBufferedImage(content, width, height, 0);
            final BufferedImage bg = ImageIO.read(bgImageFile);
            final Graphics g = bg.getGraphics();
            boolean result = g.drawImage(qrCode, 12, 500, null);
            log.debug("Draw result {}", result);
            g.dispose();
            return image2Bytes(bg);
        } catch (IOException | WriterException e) {
            throw new InternalServerErrorException("Unable to create team qrcode!", e);
        }
    }
}
