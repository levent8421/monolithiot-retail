package club.xyes.zkh.retail.web.front.controller.open;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.Commodity;
import club.xyes.zkh.retail.commons.entity.User;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.service.general.CommodityService;
import club.xyes.zkh.retail.service.general.UserService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.web.front.util.LoginCookieUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Create by 郭文梁 2019/6/5 0005 19:18
 * OpenCommodityController
 * 开放的商品相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/6/5 0005
 */
@RestController
@RequestMapping("/api/open/commodity")
@Slf4j
public class OpenCommodityController extends AbstractEntityController<Commodity> {
    private static final String SHARE_IMAGE_VIEW_NAME = "share-image";
    private static final String COMMODITY_DETAIL_VIEW_NAME = "redirect:" + ApplicationConstants.BASE_URL + "/index.html#/about?id=%s&promo-code=%s";
    private final CommodityService commodityService;
    private final AccessTokenEncoder accessTokenEncoder;
    private final UserService userService;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected OpenCommodityController(CommodityService service,
                                      AccessTokenEncoder accessTokenEncoder,
                                      UserService userService) {
        super(service);
        this.commodityService = service;
        this.accessTokenEncoder = accessTokenEncoder;
        this.userService = userService;
    }

    /**
     * 到商品详情页面
     * 微信扫描分享二维码后到改地址
     *
     * @param id    商品ID 路径参数
     * @param code  oauth code
     * @param state state附加参数  这里定义为推广码
     * @return MV
     */
    @GetMapping("/{id}/detail-page")
    public ModelAndView detailPage(@PathVariable("id") Integer id,
                                   @RequestParam("code") String code,
                                   @RequestParam("state") String state,
                                   HttpServletResponse response) {
        final User user = userService.loginByOAuthCode(code);
        LoginCookieUtils.setLoginCookie(user, response, accessTokenEncoder);
        String url = String.format(COMMODITY_DETAIL_VIEW_NAME, id, state);
        log.debug("Wechat login success! will redirect to commodity detail page:[{}]", url);
        return new ModelAndView(url);
    }

    /**
     * 创建商品分享图片
     *
     * @param id        商品ID
     * @param promoCode 推广码
     * @return 分享图片视图对象
     */
    @GetMapping("/{id}/share-image")
    public ModelAndView shareImage(@PathVariable("id") Integer id,
                                   @RequestParam("promo-code") String promoCode) {
        val promoter = userService.findByPromoCode(promoCode);
        if (promoter == null) {
            throw new BadRequestException("推手不存在");
        }
        check4Share(promoter);
        val commodity = commodityService.require(id);
        val url = commodityService.createShareImage(commodity, promoter);
        val model = new HashMap<String, Object>(16);
        model.put("commodity", commodity);
        model.put("imageUrl", url);
        model.put("promoter", promoter);
        return new ModelAndView(SHARE_IMAGE_VIEW_NAME, model);
    }

    /**
     * 检查推手的分享权限
     *
     * @param promoter 推手
     */
    private void check4Share(User promoter) {
        switch (promoter.getRole()) {
            case User.ROLE_CAPTAIN:
            case User.ROLE_PROMOTERS:
                break;
            default:
                throw new BadRequestException("当前用户没有分享权限");
        }
    }
}
