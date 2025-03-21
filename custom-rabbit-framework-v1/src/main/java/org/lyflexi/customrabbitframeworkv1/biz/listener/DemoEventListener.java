package org.lyflexi.customrabbitframeworkv1.biz.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customrabbitframeworkv1.biz.event.DemoEvent;
import org.lyflexi.customrabbitframeworkv1.biz.handler.DemoMessageHandler;
import org.lyflexi.customrabbitframeworkv1.commonapi.constant.MQIConstant;
import org.lyflexi.customrabbitframeworkv1.commonapi.holder.SystemTaskerContextHolder;
import org.lyflexi.customrabbitframeworkv1.commonapi.listener.AbstractListener;
import org.lyflexi.customrabbitframeworkv1.commonapi.listener.IListener;
import org.lyflexi.customrabbitframeworkv1.biz.message.DemoMessageData;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 15:46
 */

@Slf4j
@Component
public class DemoEventListener extends AbstractListener {

    @Autowired
    private DemoMessageHandler demoMessageHandler;

    /**
     * 监听RabbitMQ消息
     * @param message 消息对象
     * @param channel MQ通道
     * @param event 时间对象
     */
    @RabbitListener(queues = MQIConstant.TASK_SUBMITTED_QUEUE, concurrency = "1")
    public void onRabbitMQEvent(Message message, Channel channel, DemoEvent event) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("DemoEventListener:{}",event);
            //由于消息是异步的，切记一定要设置系统上下文，后续BizContextHolder将填充为admin信息
            SystemTaskerContextHolder.getInstance().mount();
            demoMessageHandler.process(event.getMessageData());
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            super.basicReject(channel,deliveryTag);
            throw new RuntimeException(e);
        } finally {
            //清理上下文
        }
    }

    /**
     * 监听Spring事件消息
     * @param event 事件对象
     */
    @Async
    @EventListener
    public void onSpringEvent(DemoEvent event) {
        log.info("[Demo-Listener]接收到事件：{}", JSON.toJSONString(event));
        demoMessageHandler.process(event.getMessageData());
    }
}