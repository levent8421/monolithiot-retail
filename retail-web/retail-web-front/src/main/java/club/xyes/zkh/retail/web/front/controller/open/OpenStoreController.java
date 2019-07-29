package club.xyes.zkh.retail.web.front.controller.open;

import club.xyes.zkh.retail.commons.context.ApplicationConstants;
import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.service.general.StoreService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.web.commons.vo.StoreLoginCookie;
import club.xyes.zkh.retail.web.front.vo.StoreAutoLoginVo;
import club.xyes.zkh.retail.web.front.vo.StoreLoginParamVo;
import club.xyes.zkh.retail.wechat.dto.WxOAuth2AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static club.xyes.zkh.retail.commons.utils.ParamChecker.notNull;

/**
 * Create by 郭文梁 2019/5/31 0031 15:14
 * OpenStoreController
 * 开放的商铺控制器
 *
 * @author 郭文梁
 * @data 2019/5/31 0031
 */
@RestController
@RequestMapping("/api/open/store")
@Slf4j
public class OpenStoreController extends AbstractEntityController<Store> {
    private static final String LOGIN_VIEW_NAME = "redirect:" + ApplicationConstants.BASE_URL + "/store/index.html#/login?openid=";
    private static final String INDEX_VIEW_NAME = "redirect:" + ApplicationConstants.BASE_URL + "/store/index.html";
    private final StoreService storeService;
    private final AccessTokenEncoder encoder;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected OpenStoreController(StoreService service,
                                  AccessTokenEncoder encoder) {
        super(service);
        this.storeService = service;
        this.encoder = encoder;
    }

    /**
     * 商家登录
     *
     * @param param    （username ,password,openId）
     * @param response 响应对象
     * @return GR
     */
    @PostMapping("/login")
    public GeneralResult<Store> login(@RequestBody StoreLoginParamVo param,
                                      HttpServletResponse response) {
        notNull(param, BadRequestException.class, "缺少参数");
        notNull(param.getLoginName(), BadRequestException.class, "登录名必填");
        notNull(param.getPassword(), BadRequestException.class, "密码必填");
        notNull(param.getOpenId(), BadRequestException.class, "openId未指定");
        Store res = storeService.login(param.getLoginName(), param.getPassword(), param.getOpenId());
        if (res != null) {
            setLoginCookie(res, param.getOpenId(), response);
        }
        return GeneralResult.ok(res);
    }

    /**
     * 商铺自动登录
     *
     * @param param 自动登录参数
     * @return GR
     */
    @PostMapping("/auto-login")
    public GeneralResult<Map<String, Object>> autoLogin(@RequestBody StoreAutoLoginVo param,
                                                        HttpServletResponse response) {
        notNull(param, BadRequestException.class, "参数未传");
        notNull(param.getCode(), BadRequestException.class, "Code不能为空");
        notNull(param.getState(), BadRequestException.class, "State不能为空");
        Map<String, Object> res = new HashMap<>(3);
        Store store = storeService.loginByOAuthCode(param.getCode(), res);
        res.put("store", store);
        res.put("loggedIn", store != null);
        if (store != null) {
            final WxOAuth2AccessToken token = (WxOAuth2AccessToken) res.get("token");
            setLoginCookie(store, token.getOpenId(), response);
        }
        return GeneralResult.ok(res);
    }

    /**
     * 设置登录Cookie
     *
     * @param openId 本次登录的OpenId
     * @param store  商铺信息
     */
    private void setLoginCookie(Store store, String openId, HttpServletResponse response) {
        StoreLoginCookie cookie = new StoreLoginCookie(store, openId, encoder);
        cookie.write2Response(response);
    }

    /**
     * 模拟登录
     *
     * @param id       商铺ID
     * @param response 响应对象
     * @return GR
     */
    @GetMapping("/{id}/login")
    public GeneralResult<Store> login(@PathVariable("id") Integer id,
                                      HttpServletResponse response) {
        @NotNull Store store = storeService.require(id);
        setLoginCookie(store, null, response);
        return GeneralResult.ok(store);
    }

    /**
     * 微信登录
     *
     * @param code  oauth2 code
     * @param state state
     * @return MV
     */
    @GetMapping("/wx-login")
    public ModelAndView wxLogin(@RequestParam("code") String code,
                                @RequestParam("state") String state,
                                HttpServletResponse response) {
        log.info("Store auto login, code=[{}],state=[{}]", code, state);
        Map<String, Object> context = new HashMap<>(1);
        Store store = storeService.loginByOAuthCode(code, context);
        String targetUrl;
        WxOAuth2AccessToken accessToken = (WxOAuth2AccessToken) context.get("token");
        final String openId = accessToken.getOpenId();
        if (store == null) {
            targetUrl = LOGIN_VIEW_NAME + openId;
        } else {
            targetUrl = INDEX_VIEW_NAME;
            new StoreLoginCookie(store, openId, encoder).write2Response(response);
        }
        return new ModelAndView(targetUrl);
    }
}
