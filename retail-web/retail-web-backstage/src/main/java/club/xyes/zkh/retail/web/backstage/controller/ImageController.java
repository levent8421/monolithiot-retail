package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.MultipartFileUtil;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.config.StaticConfigProp;
import club.xyes.zkh.retail.web.backstage.vo.ImageVo;
import club.xyes.zkh.retail.web.commons.controller.AbstractController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Create by 郭文梁 2019/6/1 0001 11:52
 * ImageController
 * 图片操作控制器
 *
 * @author 郭文梁
 * @data 2019/6/1 0001
 */
@RestController
@RequestMapping("/api/image")
public class ImageController extends AbstractController {
    private final StaticConfigProp staticConfigProp;

    public ImageController(StaticConfigProp staticConfigProp) {
        this.staticConfigProp = staticConfigProp;
    }

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return GR
     * @throws IOException IO异常
     */
    @PostMapping("/upload")
    public GeneralResult<ImageVo> upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("未上传文件");
        }
        String filePath = staticConfigProp.getStaticFilePath();
        String imagePath = staticConfigProp.getImagePath();
        String path = filePath + imagePath;
        String fileName = MultipartFileUtil.save(file, path);
        ImageVo res = new ImageVo();
        res.setUrl(fileName);
        prettyPath(res);
        return GeneralResult.ok(res);
    }

    /**
     * 替换为绝对路径
     *
     * @param image 图片对象
     */
    private void prettyPath(ImageVo image) {
        String url = image.getUrl();
        String server = staticConfigProp.getStaticServer();
        String path = staticConfigProp.getImagePath();
        String targetUrl = server + path + url;
        image.setUrl(targetUrl);
    }
}
