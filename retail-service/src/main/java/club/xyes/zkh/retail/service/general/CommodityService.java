package club.xyes.zkh.retail.service.general;

import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.vo.CommodityHtmlVo;
import club.xyes.zkh.retail.commons.vo.CommodityWithDistance;
import club.xyes.zkh.retail.service.basic.AbstractService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Create by 郭文梁 2019/5/20 0020 11:15
 * CommodityService
 * 商品相关业务行为定义
 *
 * @author 郭文梁
 * @data 2019/5/20 0020
 */
public interface CommodityService extends AbstractService<Commodity> {
    /**
     * 通过ID查询商品 同时抓取出关联对象
     *
     * @param id ID
     * @return Commodity with all
     */
    Commodity requireFetchAll(Integer id);

    /**
     * 为商铺创建新的商品
     *
     * @param storeId   商铺ID
     * @param commodity 商品参数
     * @return GR
     */
    Commodity create(Integer storeId, Commodity commodity);

    /**
     * 通过商铺ID查询商品
     *
     * @param storeId 商铺ID
     * @param page    页码
     * @param rows    每页大小
     * @return Commodity PageInfo
     */
    PageInfo<Commodity> findByStoreId(Integer storeId, int page, int rows);

    /**
     * 设置商品的详情Html
     *
     * @param commodity 商品对象
     * @param html      html
     * @throws IOException IO异常
     */
    void setHtml(Commodity commodity, String html) throws IOException;

    /**
     * 根据商品ID查询商品的详情HTML
     *
     * @param commodityId 商品ID
     * @return HtmlVo
     * @throws IOException IO异常
     */
    CommodityHtmlVo detailHtml(Integer commodityId) throws IOException;

    /**
     * 通过名称搜索商品
     *
     * @param name 搜索名称
     * @param page 页码
     * @param rows 每页大小
     * @return PageInfo
     */
    PageInfo<Commodity> searchByName(String name, Integer page, Integer rows);

    /**
     * 删除商品图片
     *
     * @param commodity 商品
     * @param index     图片索引
     */
    void deleteImage(Commodity commodity, Integer index);

    /**
     * 追加商品轮播图
     *
     * @param commodity 商品
     * @param file      图片文件
     * @throws IOException IO异常
     */
    void appendImage(Commodity commodity, MultipartFile file) throws IOException;

    /**
     * 创建分享图片
     *
     * @param commodity 商品信息
     * @param promoter  推手信息
     * @return 图片的访问链接
     */
    String createShareImage(Commodity commodity, User promoter);

    /**
     * 设置商品分享二维码背景图
     *
     * @param commodity 商品
     * @param file      背景图文件
     * @return 商品
     * @throws IOException IO异常
     */
    Commodity setShareImage(Commodity commodity, MultipartFile file) throws IOException;

    /**
     * 更新商品库存
     *
     * @param commodity 商品对象
     * @param amount    递增（减）数量
     * @return 数量
     */
    int updateStock(Commodity commodity, int amount);

    /**
     * 通过地区索引查找商品
     *
     * @param addressIndexId 索引ID
     * @param page           页码
     * @param rows           每页大小
     * @return PageInfo
     */
    PageInfo<Commodity> findByAddressIndex(Integer addressIndexId, Integer page, Integer rows);

    /**
     * 通过位置查询商品信息
     *
     * @param lon   经度
     * @param lat   纬度
     * @param range 范围（单位 米）
     * @param page  页码
     * @param rows  每页大小
     * @return PageInfo
     */
    PageInfo<CommodityWithDistance> findByDistance(Double lon, Double lat, Long range, int page, int rows);
}
