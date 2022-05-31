package com.dawn.mall.listener;

import cn.hutool.json.JSONUtil;
import com.dawn.mall.domain.PayInfo;
import com.dawn.mall.enums.OrderStatusEnum;
import com.dawn.mall.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: Dawn
 * @Date: 2022/3/13 00:11
 */
@Slf4j
@Component
@RabbitListener(queues = "payNotify")
public class PayMsgListener {


    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void process(String msg) {
        log.info("【接收到消息】=> {}", msg);

        PayInfo payInfo = JSONUtil.toBean(msg, PayInfo.class);

        if (payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())) {
            // 修改订单里的状态
            orderService.paid(payInfo.getOrderNo());
        }
    }
}
