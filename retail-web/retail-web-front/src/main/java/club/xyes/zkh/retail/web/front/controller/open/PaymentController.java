package club.xyes.zkh.retail.web.front.controller.open;

import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.utils.XmlUtils;
import club.xyes.zkh.retail.commons.vo.GeneralResult;
import club.xyes.zkh.retail.service.general.OrderService;
import club.xyes.zkh.retail.wechat.dto.WxPayNotifyVo;
import club.xyes.zkh.retail.wechat.props.WechatConfig;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by 郭文梁 2019/5/28 0028 10:41
 * PaymentController
 * 支付相关数据访问控制器
 *
 * @author 郭文梁
 * @data 2019/5/28 0028
 */
@RestController
@RequestMapping("/pay")
@Slf4j
public class PaymentController {
    private final OrderService orderService;
    private final OrderService.PaySuccessListener paySuccessListener;
    private final WechatConfig wechatConfig;

    public PaymentController(OrderService orderService,
                             OrderService.PaySuccessListener paySuccessListener, WechatConfig wechatConfig) {
        this.orderService = orderService;
        this.paySuccessListener = paySuccessListener;
        this.wechatConfig = wechatConfig;
    }

    /**
     * 处理支付结果通知
     *
     * @param notifyData 通知数据
     * @return 响应数据
     */
    @RequestMapping("/notify")
    public String onNotify(@RequestBody String notifyData) {
        log.debug("On payment notify [{}]", notifyData);
        WxPayNotifyVo notifyVo;
        try {
            notifyVo = XmlUtils.parseObject(notifyData, WxPayNotifyVo.class);
        } catch (DocumentException e) {
            String resp = buildNotifyFailResult("Notify data format invalidate");
            log.warn("Notify error response:[{}]", resp);
            return resp;
        }
        if (!checkNotifyVo(notifyVo)) {
            String resp = buildNotifyFailResult("Notify data sign invalidate!");
            log.warn("Payment Notify sign invalidate!");
            return resp;
        }

        try {
            Order order = orderService.resolveTradeNotify(notifyVo, paySuccessListener);
            log.info("Payment Notify Success [{}]", order);
            return buildNotifySuccessResult();
        } catch (Exception e) {
            log.warn("Resolve notify error:[{}]", notifyData, e);
            String resp = buildNotifyFailResult(e.getMessage());
            log.warn("Notify error response:[{}]", resp);
            return resp;
        }
    }

    /**
     * 检查通知数据签名
     *
     * @param notifyVo 通知数据
     * @return 是否合法
     */
    private boolean checkNotifyVo(WxPayNotifyVo notifyVo) {
        //TODO 通知数据签名检查 暂未实现
        return true;
    }

    /**
     * 构建通知成功的返回结果
     *
     * @return 响应结果
     */
    private String buildNotifySuccessResult() {
        Map<String, String> res = new HashMap<>(2);
        res.put("return_code", "SUCCESS");
        res.put("return_msg", "OK");
        return XmlUtils.asXml(res, "xml");
    }

    /**
     * 构建通知失败的响应结果
     *
     * @param error 错误内容
     * @return 响应内容
     */
    private String buildNotifyFailResult(String error) {
        Map<String, String> res = new HashMap<>(2);
        res.put("return_code", "FAIL");
        res.put("return_msg", error);
        return XmlUtils.asXml(res, "xml");
    }

    /**
     * 获取微信配置
     *
     * @return GR
     */
    @GetMapping("/config")
    public GeneralResult<WechatConfig> config() {
        return GeneralResult.ok(wechatConfig);
    }
}
