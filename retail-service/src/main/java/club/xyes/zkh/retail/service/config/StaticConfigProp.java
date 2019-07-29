package club.xyes.zkh.retail.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * Create by 郭文梁 2019/5/20 0020 19:19
 * StaticConfigProp
 * 静态资源配置
 *
 * @author 郭文梁
 * @data 2019/5/20 0020
 */
@Data
@ConfigurationProperties(prefix = "site")
@Component
public class StaticConfigProp {
    /**
     * 静态资源服务器地址
     */
    private String staticServer;
    /**
     * 静态资源保存路径
     */
    private String staticFilePath;
    /**
     * 商品图片保存路径
     */
    private String commodityImagePath = "/commodity/image/";
    /**
     * 订单的二维码保存路径
     */
    private String orderQrCodePath = "/order/qrcode/";
    /**
     * 通用图片保存路径
     */
    private String imagePath = "/image/";
    /**
     * 商品详情HTML保存路径
     */
    private String commodityHtmlPath = "/commodity/html/";
    /**
     * 分享二维码的背景图
     */
    private String shareImagePath = "/commodity/share-image/";
    /**
     * 推广海报二维码保存地址
     */
    private String promoQrCodePath = "/commodity/promo-qr-code/";
    /**
     * 加入团队背景图
     */
    private String teamQrCodeBg = "/join-qr-code-bg/bg.jpg";
}
