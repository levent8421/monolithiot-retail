package club.xyes.zkh.retail.web.backstage.controller;

import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.web.commons.controller.AbstractController;
import club.xyes.zkh.retail.wechat.api.Wechat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Create by 郭文梁 2019/6/4 0004 11:07
 * WechatController
 * 微信相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/6/4 0004
 */
@RestController
@RequestMapping("/api/wechat")
public class WechatController extends AbstractController {
    private final Wechat wechat;

    public WechatController(Wechat wechat) {
        this.wechat = wechat;
    }

    /**
     * 设置菜单
     *
     * @param menuJson 菜单内容
     * @return GR
     * @throws IOException IO异常
     */
    @PostMapping("/set-menu")
    public GeneralResult<String> setMenu(@RequestBody String menuJson) throws IOException {
        final String resp = wechat.setMenu(menuJson);
        return GeneralResult.ok(resp);
    }
}
