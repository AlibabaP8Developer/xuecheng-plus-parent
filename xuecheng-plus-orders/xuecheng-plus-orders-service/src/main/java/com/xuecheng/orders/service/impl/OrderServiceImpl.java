package com.xuecheng.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xuecheng.orders.config.PayNotifyConfig;
import com.xuecheng.orders.model.dto.AddOrderDto;
import com.xuecheng.orders.model.dto.PayRecordDto;
import com.xuecheng.orders.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MqMessageService mqMessageService;

    @Transactional
    @Override
    public PayRecordDto createOrder(String userId, AddOrderDto addOrderDto) {
        //插入订单表

        //插入支付记录

        //生成二维码

        return null;
    }

    @Override
    public void notifyPayResult(MqMessage message) {
        //消息内容
        String jsonString = JSON.toJSONString(message);

        // 持久化消息
        Message messageObj = MessageBuilder.withBody(jsonString.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();

        Long id = message.getId();

        // 全局的消息ID
        CorrelationData correlationData = new CorrelationData(id.toString());
        // 使用correlationData指定回调方法
        correlationData.getFuture().addCallback(result -> {
            if (result.isAck()) {
                // 消息成功发送到了交换机
                log.debug("发送消息成功:{}", jsonString);
                // 完成消息，将消息从数据库表删除
                mqMessageService.completed(id);
            } else {
                // 消息发送失败
                log.debug("发送消息失败:{}", jsonString);
            }
        }, ex -> {
            // 发生异常了
            log.debug("发送消息异常:{}", jsonString);
        });
        // 发送消息
        rabbitTemplate.convertAndSend(PayNotifyConfig.PAYNOTIFY_EXCHANGE_FANOUT, "", messageObj, correlationData);
    }
}
