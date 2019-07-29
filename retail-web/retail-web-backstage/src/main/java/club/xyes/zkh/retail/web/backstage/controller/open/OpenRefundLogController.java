package club.xyes.zkh.retail.web.backstage.controller.open;

import club.xyes.zkh.retail.commons.entity.RefundLog;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.utils.XmlUtils;
import club.xyes.zkh.retail.service.general.RefundLogService;
import club.xyes.zkh.retail.web.commons.controller.AbstractEntityController;
import club.xyes.zkh.retail.wechat.dto.WxRefundNotifyParam;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by 郭文梁 2019/6/13 0013 13:33
 * OpenRefundLogController
 * 退款记录相关开放API访问控制器
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@Slf4j
@RestController
@RequestMapping("/api/open/refund")
public class OpenRefundLogController extends AbstractEntityController<RefundLog> {
    private final RefundLogService refundLogService;
    private final RefundLogService.RefundListener refundListener;

    /**
     * 构造时指定业务组件
     *
     * @param service 业务组件
     */
    protected OpenRefundLogController(RefundLogService service,
                                      RefundLogService.RefundListener refundListener) {
        super(service);
        this.refundLogService = service;
        this.refundListener = refundListener;
    }

    /**
     * 处理微信退款通知
     *
     * @param xmlString post body
     */
    @PostMapping("/notify")
    public String onNotify(@RequestBody String xmlString) {
        log.debug("Resolve Refund notify: [{}]", xmlString);

        try {
            final WxRefundNotifyParam param = XmlUtils.parseObject(xmlString, WxRefundNotifyParam.class);
            if (!param.isSuccess()) {
                log.debug("Refund notify for Wechat is not success!");
                throw new InternalServerErrorException("FAIL");
            }
            refundLogService.onNotify(param.getReqInfo(), refundListener);
        } catch (DocumentException e) {
            throw new InternalServerErrorException("Unable to parse request param!");
        }
        return XmlUtils.asXml(buildSuccessReturn(), "xml");
    }

    /**
     * 构建成功的返回结果
     *
     * @return map
     */
    private Map<String, String> buildSuccessReturn() {
        Map<String, String> res = new HashMap<>(2);
        res.put("return_code", "SUCCESS");
        res.put("return_msg", "OK");
        return res;
    }
}
