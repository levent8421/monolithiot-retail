package club.xyes.zkh.retail.service.general.impl;

import club.xyes.zkh.retail.commons.entity.Order;
import club.xyes.zkh.retail.commons.entity.RefundLog;
import club.xyes.zkh.retail.commons.exception.BadRequestException;
import club.xyes.zkh.retail.commons.exception.InternalServerErrorException;
import club.xyes.zkh.retail.commons.exception.ResourceNotFoundException;
import club.xyes.zkh.retail.commons.utils.DateTimeUtils;
import club.xyes.zkh.retail.commons.utils.RandomUtils;
import club.xyes.zkh.retail.commons.utils.XmlUtils;
import club.xyes.zkh.retail.repository.dao.mapper.RefundLogMapper;
import club.xyes.zkh.retail.service.basic.impl.AbstractServiceImpl;
import club.xyes.zkh.retail.service.general.RefundLogService;
import club.xyes.zkh.retail.wechat.api.Wechat;
import club.xyes.zkh.retail.wechat.dto.WxRefundNotifyData;
import club.xyes.zkh.retail.wechat.dto.WxRefundResult;
import club.xyes.zkh.retail.wechat.props.WechatConfig;
import club.xyes.zkh.retail.wechat.utils.WxSignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Create by 郭文梁 2019/6/13 0013 13:08
 * RefundLogServiceImpl
 * 退款记录相关业务行为实现
 *
 * @author 郭文梁
 * @data 2019/6/13 0013
 */
@Service
@Slf4j
public class RefundLogServiceImpl extends AbstractServiceImpl<RefundLog> implements RefundLogService {
    private final RefundLogMapper refundLogMapper;
    private final Wechat wechat;
    private final WechatConfig wechatConfig;

    public RefundLogServiceImpl(RefundLogMapper mapper,
                                Wechat wechat,
                                WechatConfig wechatConfig) {
        super(mapper);
        this.refundLogMapper = mapper;
        this.wechat = wechat;
        this.wechatConfig = wechatConfig;
    }

    @Override
    public RefundLog create(RefundLog refundLog, Order order, RefundListener listener) {
        if (refundLog.getRefundAmount() > order.getAmount()) {
            throw new BadRequestException("退款金额必须小于或等于订单金额");
        }
        final String tradeNo = RandomUtils.randomPrettyUUIDString();
        refundLog.setOrder(order);
        refundLog.setOrderAmount(order.getAmount());
        refundLog.setTradeNo(tradeNo);
        refundLog.setStatus(RefundLog.STATUS_CREATE);
        doRefund(refundLog, order);
        final RefundLog res = save(refundLog);
        listener.onRefundCreate(res, order);
        return res;
    }

    /**
     * 执行退款操作
     *
     * @param refundLog 退款记录对象
     * @param order     订单对象
     */
    private void doRefund(RefundLog refundLog, Order order) {
        WxRefundResult refundResult = wechat.refund(refundLog, order);
        if (!refundResult.isSuccess()) {
            throw new InternalServerErrorException("退款申请失败:" + refundResult.getReturnMsg());
        }
    }

    @Override
    public void onNotify(String reqInfo, RefundListener listener) {
        final String key = wechatConfig.getSignKey();
        final String xmlString = WxSignUtil.decodeNotifyReqInfo(reqInfo, key);
        WxRefundNotifyData notifyData;
        try {
            notifyData = XmlUtils.parseObject(xmlString, WxRefundNotifyData.class);
        } catch (Exception e) {
            throw new InternalServerErrorException("Unable to parse Notify xmlString[" + xmlString + "]", e);
        }
        final String outRefundNo = notifyData.getOutRefundNo();
        RefundLog refundLog = requireByTradeNo(outRefundNo);
        refundLog.setCompleteTime(DateTimeUtils.now());
        if (notifyData.isSuccess()) {
            refundLog.setStatus(RefundLog.STATUS_SUCCESS);
            listener.onRefundSuccess(refundLog);
        } else {
            refundLog.setStatus(RefundLog.STATUS_FAIL);
            listener.onRefundFail(refundLog);
        }
        final RefundLog updateRes = updateById(refundLog);
        log.debug("Update refund status:{}", updateRes);
    }

    /**
     * 通过交易号查询退款记录 不存在时抛出异常
     *
     * @param tradeNo 交易号
     * @return 退款记录
     */
    private RefundLog requireByTradeNo(String tradeNo) {
        RefundLog query = new RefundLog();
        query.setTradeNo(tradeNo);
        final RefundLog refundLog = findOneByQuery(query);
        if (refundLog == null) {
            throw new ResourceNotFoundException(RefundLog.class, tradeNo);
        }
        return refundLog;
    }
}
