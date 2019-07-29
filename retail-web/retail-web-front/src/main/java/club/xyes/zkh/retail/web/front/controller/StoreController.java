package club.xyes.zkh.retail.web.front.controller;

import club.xyes.zkh.retail.commons.entity.Store;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.encrypt.AccessTokenEncoder;
import club.xyes.zkh.retail.service.general.StoreService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.web.commons.vo.StoreLoginCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * Create by 郭文梁 2019/5/20 0020 14:08
 * StoreController
 * 商铺相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/20 0020
 */
@RestController
@RequestMapping("/api/store")
public class StoreController extends AbstractEntityController<Store> {
    private final StoreService storeService;
    private final AccessTokenEncoder accessTokenEncoder;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected StoreController(StoreService service, AccessTokenEncoder accessTokenEncoder) {
        super(service);
        this.storeService = service;
        this.accessTokenEncoder = accessTokenEncoder;
    }

    /**
     * 获取商铺的详细信息
     *
     * @param id ID
     * @return GR with store
     */
    @GetMapping("/{id}")
    public GeneralResult<Store> detail(@PathVariable("id") Integer id) {
        @NotNull Store store = storeService.require(id);
        return GeneralResult.ok(store);
    }

    /**
     * 获取当前登录的商铺信息
     *
     * @return 商铺信息
     */
    @GetMapping("/me")
    public GeneralResult<Store> me() {
        @NotNull Store store = requireCurrentStore(storeService);
        return GeneralResult.ok(store);
    }

    /**
     * 商家登出操作
     *
     * @param response 响应对象
     * @return GR
     */
    @PostMapping("/logout")
    public GeneralResult<Store> logout(HttpServletResponse response) {
        @NotNull final Store store = requireCurrentStore(storeService);
        @NotNull final StoreLoginCookie storeLoginCookie = requireStoreLoginCookie();
        //销毁Cookie
        final String openId = storeLoginCookie.getStoreInfo().getOpenId();
        final StoreLoginCookie cookie = new StoreLoginCookie(store, openId, accessTokenEncoder);
        cookie.setMaxAge(0);
        cookie.write2Response(response);
        //清空openId
        storeService.cleanOpenId(store, openId);
        return GeneralResult.ok(store);
    }
}
