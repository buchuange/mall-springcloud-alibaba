package com.dawn.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.dawn.mall.config.PayStatusEnum;
import com.dawn.mall.consts.MallConsts;
import com.dawn.mall.domain.PayInfo;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.exception.MallException;
import com.dawn.mall.mapper.PayMapper;
import com.dawn.mall.service.PayService;
import com.google.gson.Gson;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: Dawn
 * @Date: 2022/5/23 13:27
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Resource
    private PayMapper payMapper;

    @Resource
    private BestPayService bestPayService;

    @Resource
    private AmqpTemplate amqpTemplate;


    @Override
    @Transactional
    public PayResponse create(Integer userId, String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {

        // 将支付信息写入数据库
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(userId);
        payInfo.setOrderNo(orderId);
        payInfo.setPayPlatform(PayStatusEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode());
        payInfo.setPlatformStatus(OrderStatusEnum.NOTPAY.name());
        payInfo.setPayAmount(amount);

        int result = payMapper.insertSelective(payInfo);
        if (result != 1) {
            throw new MallException(ResultEnum.FAILURE);
        }

        PayRequest payRequest = new PayRequest();
        payRequest.setPayTypeEnum(bestPayTypeEnum);
        payRequest.setOrderId(orderId);
        payRequest.setOrderName("练习用支付订单");
        payRequest.setOrderAmount(amount.doubleValue());

        log.info("【微信支付】发起支付，payRequest={}", JSONUtil.toJsonPrettyStr(payRequest));

        PayResponse payResponse = bestPayService.pay(payRequest);

        log.info("【微信支付】发起支付，payResponse={}", JSONUtil.toJsonPrettyStr(payResponse));

        return payResponse;
    }

    @Override
    public void asyncNotify(String notifyData) {

        // 1、签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】异步通知 payResponse: {}", JSONUtil.toJsonPrettyStr(payResponse));

        // 2、金额校验
        PayInfo payInfo = payMapper.getByOrderNo(payResponse.getOrderId());

        if (ObjectUtils.isEmpty(payInfo)) {

            log.error("【微信支付】异步通知，查询支付信息为空，订单编号：{}", payResponse.getOrderId());

            throw new MallException(ResultEnum.PAY_INFO_NOT_EXIST);
        }

        // 如果订单状态不是已支付
        if (!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())) {

            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0) {

                log.error("【微信支付】异步通知，金额校验失败，orderId={}，微信通知金额={}，数据库金额={}",
                        payResponse.getOrderId(), payResponse.getOrderAmount(), payInfo.getPayAmount());

                throw new MallException(ResultEnum.WECHAT_NOTIFY_MONEY_VERIFY_ERROR);

            }

            // 修改订单支付状态
            payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
            payInfo.setPlatformNumber(payResponse.getOutTradeNo());

            int result = payMapper.updateStatus(payInfo);
            if (result != 1) {
                throw new MallException(ResultEnum.FAILURE);
            }
        }


        // pay微服务发送MQ消息，order微服务接收MQ消息
        amqpTemplate.convertAndSend(MallConsts.QUEUE_PAY_NOTIFY, JSONUtil.toJsonStr(payInfo));
    }

    @Override
    public PayInfo getByOrderNo(String orderId) {

        return payMapper.getByOrderNo(orderId);
    }
}
