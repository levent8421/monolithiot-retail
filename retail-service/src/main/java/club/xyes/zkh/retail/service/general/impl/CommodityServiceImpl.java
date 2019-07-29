package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.exception.ResourceNotFoundException;
import club.xyes.zkh.retail.commons.utils.MultipartFileUtil;
import club.xyes.zkh.retail.commons.utils.ParamChecker;
import club.xyes.zkh.retail.commons.utils.QrCodeUtil;
import club.xyes.zkh.retail.commons.utils.TextUtils;
import club.xyes.zkh.retail.commons.vo.CommodityHtmlVo;
import club.xyes.zkh.retail.commons.vo.CommodityWithDistance;
import club.xyes.zkh.retail.repository.dao.mapper.CommodityMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.config.StaticConfigProp;
import club.xyes.zkh.retail.service.general.CommodityService;
import club.xyes.zkh.retail.wechat.utils.LoginUrlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create by 郭文梁 2019/5/20 0020 11:16
 * CommodityServiceImpl
 * 商品相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/5/20 0020
 */
@Service
@Slf4j
public class CommodityServiceImpl extends AbstractServiceImpl<Commodity> implements CommodityService {
    /**
     * 分享二维码内容模板 [背景图文件名] - [推广码]
     */
    private static final String SHARE_IMAGE_QR_CODE_CONTENT_TEMPLATE = "%s-%s";
    /**
     * 分享推按二维码的宽高
     */
    private static final int SHARE_QR_CODE_WIDTH = 125, SHARE_QR_CODE_HEIGHT = 125;
    /**
     * 允许的轮播图数量
     */
    private static final int MAX_IMAGE_COUNT = 5;
    private CommodityMapper commodityMapper;
    private final StaticConfigProp staticConfigProp;

    public CommodityServiceImpl(CommodityMapper mapper, StaticConfigProp staticConfigProp) {
        super(mapper);
        this.commodityMapper = mapper;
        this.staticConfigProp = staticConfigProp;
    }

    @Override
    public PageInfo<Commodity> list(int page, int rows) {
        return PageHelper
                .startPage(page, rows)
                .doSelectPageInfo(() -> commodityMapper.selectAvailableOrderByCreateTimeDesc());
    }

    @Override
    public Commodity requireFetchAll(Integer id) {
        Commodity commodity = commodityMapper.selectByIdFetchAll(id);
        if (commodity == null) {
            throw new ResourceNotFoundException(Commodity.class, id);
        }
        return commodity;
    }

    @Override
    public Commodity create(Integer storeId, Commodity commodity) {
        commodity.setStoreId(storeId);
        commodity.setStatus(Commodity.STATUS_AVAILABLE);
        return save(commodity);
    }

    @Override
    public PageInfo<Commodity> findByStoreId(Integer storeId, int page, int rows) {
        return PageHelper.startPage(page, rows, "create_time desc").doSelectPageInfo(() -> {
            Commodity commodity = new Commodity();
            commodity.setStoreId(storeId);
            commodityMapper.select(commodity);
        });
    }

    @Override
    public void setHtml(Commodity commodity, String html) throws IOException {
        String filePath = staticConfigProp.getStaticFilePath();
        String htmlPath = staticConfigProp.getCommodityHtmlPath();
        String fileName = String.format("%s.html", commodity.getId());
        File fullPath = new File(filePath + htmlPath);
        if (!fullPath.exists()) {
            if (!fullPath.mkdirs()) {
                throw new InternalServerErrorException("Could not create path:" + fullPath.getAbsolutePath());
            }
        }
        if (!fullPath.isDirectory()) {
            throw new InternalServerErrorException("Path " + fullPath.getAbsolutePath() + " early exists, but it is not a directory!");
        }
        File file = new File(fullPath, fileName);
        FileUtils.write(file, html, ApplicationConstants.DEFAULT_CHARSET);
    }

    @Override
    public CommodityHtmlVo detailHtml(Integer commodityId) throws IOException {
        String filePath = staticConfigProp.getStaticFilePath();
        String htmlPath = staticConfigProp.getCommodityHtmlPath();
        String fileName = String.format("%s.html", commodityId);
        File file = new File(filePath + htmlPath + fileName);
        CommodityHtmlVo res = new CommodityHtmlVo();
        if (file.exists()) {
            String html = FileUtils.readFileToString(file, ApplicationConstants.DEFAULT_CHARSET);
            res.setHtml(html);
        }
        return res;
    }

    @Override
    public PageInfo<Commodity> searchByName(String name, Integer page, Integer rows) {
        String query = "%" + name + "%";
        return PageHelper.startPage(page, rows, "create_time desc").doSelectPageInfo(() -> commodityMapper.searchByName(query));
    }

    @Override
    public void deleteImage(Commodity commodity, Integer index) {
        final String images = commodity.getImages();
        ParamChecker.notEmpty(images, BadRequestException.class, "该商品无轮播图");
        final String[] imageArr = images.split(ApplicationConstants.IMAGE_DELIMITER);
        if (imageArr.length <= index) {
            throw new BadRequestException("该索引处无图片");
        }
        final List<String> imageList = new ArrayList<>(Arrays.asList(imageArr));
        imageList.remove((int) index);
        commodity.setImageList(imageList);
        commodity.convertImageList2Images();
        updateById(commodity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void appendImage(Commodity commodity, MultipartFile file) throws IOException {
        commodity.parseImageList();
        if (commodity.getImageList() == null) {
            commodity.setImageList(new ArrayList<>());
        }
        if (commodity.getImageList().size() >= MAX_IMAGE_COUNT) {
            throw new BadRequestException("最多允许" + MAX_IMAGE_COUNT + "张图片");
        }
        final String filePath = staticConfigProp.getStaticFilePath();
        final String imagePath = staticConfigProp.getCommodityImagePath();

        final String fileName = MultipartFileUtil.save(file, filePath + imagePath);
        final int rows = commodityMapper.appendImage(commodity.getId(), fileName);
        if (rows != 1) {
            throw new BadRequestException("更新图片列表失败");
        }
    }

    @Override
    public String createShareImage(Commodity commodity, User promoter) {
        val fileName = getOrCreateShareQrCodeFileName(commodity, promoter.getPromoCode());
        return staticConfigProp.getStaticServer() + staticConfigProp.getPromoQrCodePath() + fileName;
    }

    /**
     * 获取分享二维码的文件名称，当文件不存在时创建文件
     *
     * @param commodity 商品
     * @param promoCode 推广码文件名称
     * @return 生成图片的文件名称
     */
    private String getOrCreateShareQrCodeFileName(Commodity commodity, String promoCode) {
        val shareImage = String.format(SHARE_IMAGE_QR_CODE_CONTENT_TEMPLATE, promoCode, commodity.getShareImage());
        val rootPath = staticConfigProp.getStaticFilePath() + staticConfigProp.getPromoQrCodePath();
        val path = new File(rootPath);
        if (!path.exists()) {
            if (!path.mkdirs()) {
                throw new InternalServerErrorException("Could not create share qrcode path!");
            }
        }
        val qrCodeFile = new File(rootPath, shareImage);
        if (qrCodeFile.exists()) {
            return shareImage;
        }
        try {
            val bgFile = new File(staticConfigProp.getStaticFilePath() + staticConfigProp.getShareImagePath(), commodity.getShareImage());
            val bg = ImageIO.read(bgFile);

            val content = LoginUrlUtil.getShareLoginUrl(commodity.getId(), promoCode);
            val bytes = QrCodeUtil.generateShareQrCodeImage(content, SHARE_QR_CODE_WIDTH, SHARE_QR_CODE_HEIGHT, bg);
            log.debug("Image Result size {}", bytes.length);
            FileUtils.writeByteArrayToFile(qrCodeFile, bytes);
            return shareImage;
        } catch (Exception e) {
            throw new InternalServerErrorException(TextUtils.isTrimedEmpty(e.getMessage()) ? "创建分享图片失败" : e.getMessage(), e);
        }
    }

    @Override
    public Commodity setShareImage(Commodity commodity, MultipartFile file) throws IOException {
        final String path = staticConfigProp.getStaticFilePath();
        final String filePath = staticConfigProp.getShareImagePath();
        final String filename = MultipartFileUtil.save(file, path + filePath);
        commodity.setShareImage(filename);
        return updateById(commodity);
    }

    @Override
    public int updateStock(Commodity commodity, int amount) {
        int rows = commodityMapper.updateStock(commodity.getId(), amount);
        log.debug("Update stock success, result rows={}", rows);
        return amount;
    }

    @Override
    public PageInfo<Commodity> findByAddressIndex(Integer addressIndexId, Integer page, Integer rows) {
        return PageHelper.startPage(page, rows).doSelectPageInfo(() -> commodityMapper.selectByAddressIndex(addressIndexId));
    }

    @Override
    public PageInfo<CommodityWithDistance> findByDistance(Double lon, Double lat, Long range, int page, int rows) {
        return PageHelper.startPage(page, rows).doSelectPageInfo(() -> commodityMapper.selectByDistance(lon, lat, range));
    }
}
