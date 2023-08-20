package com.xuecheng.learning.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.learning.config.PayNotifyConfig;
import com.xuecheng.learning.service.MyCourseTablesService;
import com.xuecheng.messagesdk.model.po.MqMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mr.M
 * @version 1.0
 * @description 接收消息通知处理类
 * @date 2023/2/23 19:04
 */
@Slf4j
@Service
public class ReceivePayNotifyService {
    @Autowired
    private MyCourseTablesService myCourseTablesService;

    //监听消息队列接收支付结果通知
    @RabbitListener(queues = PayNotifyConfig.PAYNOTIFY_QUEUE)
    public void receive(Message message) {
        byte[] body = message.getBody();
        String jsonStr = new String(body);
        // 转成对象
        MqMessage mqMessage = JSON.parseObject(jsonStr, MqMessage.class);
        // 解析消息内容
        // 选课ID
        String chooseCourseId = mqMessage.getBusinessKey1();
        // 订单类型
        String orderType = mqMessage.getBusinessKey2();
        // 学习中心服务只要购买课程类的支付订单的结果
        if (orderType.equals("60201")) {
            // 根据消息内容，更新选课记录，向我的课程表插入记录
            myCourseTablesService.saveChooseCourseSuccess(chooseCourseId);
        }
    }
}
