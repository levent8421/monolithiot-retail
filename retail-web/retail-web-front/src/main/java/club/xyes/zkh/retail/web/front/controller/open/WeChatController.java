package club.xyes.zkh.retail.web.front.controller.open;

import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.utils.TextUtils;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.web.commons.controller.AbstractController;
import club.xyes.zkh.retail.web.front.vo.WxJsApiParamVo;
import club.xyes.zkh.retail.wechat.api.Wechat;
import club.xyes.zkh.retail.wechat.dto.WxJsApiConfigParam;
import club.xyes.zkh.retail.wechat.props.WechatConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Create by 郭文梁 2019/5/18 0018 15:33
 * WeChatController
 * 微信相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/18 0018
 */
@RestController
@RequestMapping("/api/open/wx")
public class WeChatController extends AbstractController {
    private final Wechat wechat;
    private final WechatConfig wechatConfig;

    public WeChatController(Wechat wechat, WechatConfig wechatConfig) {
        this.wechat = wechat;
        this.wechatConfig = wechatConfig;
    }

    /**
     * 获取微信JsApi配置参数
     *
     * @param paramVo {url: 'xxx'}
     * @return GR
     */
    @PostMapping("/jsapi-config")
    public GeneralResult<WxJsApiConfigParam> configParam(@RequestBody WxJsApiParamVo paramVo) {
        if (paramVo == null) {
            throw new BadRequestException("参数未传");
        }
        TextUtils.notEmpty(paramVo::getUrl, "授权地址必填");
        String appId = wechatConfig.getAppId();
        String secret = wechatConfig.getSecret();
        WxJsApiConfigParam config = wechat.getJsApiConfigParam(appId, secret, paramVo.getUrl());
        return GeneralResult.ok(config);
    }
}
