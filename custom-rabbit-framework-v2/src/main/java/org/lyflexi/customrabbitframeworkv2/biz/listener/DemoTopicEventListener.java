package org.lyflexi.customrabbitframeworkv2.biz.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customrabbitframeworkv2.biz.event.DemoTopicEvent;
import org.lyflexi.customrabbitframeworkv2.biz.handler.DemoTopicMessageHandler;
import org.lyflexi.customrabbitframeworkv2.commonapi.constant.MQIConstant;
import org.lyflexi.customrabbitframeworkv2.commonapi.holder.SystemTaskerContextHolder;
import org.lyflexi.customrabbitframeworkv2.commonapi.listener.AbstractListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
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
public class DemoTopicEventListener extends AbstractListener {

    @Autowired
    private DemoTopicMessageHandler demoTopicMessageHandler;

    /**
     * 监听TOPIC
     * @param message 消息对象
     * @param channel MQ通道
     * @param event 时间对象
     */
    @RabbitListener(queues = MQIConstant.DEMO_EVENT_TOPIC_QUEUE, concurrency = "1")
    public void onRabbitMQEvent(Message message, Channel channel, DemoTopicEvent event) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("DemoEventListener:{}",event);
            //由于消息是异步的，切记一定要设置系统上下文，后续BizContextHolder将填充为admin信息
            SystemTaskerContextHolder.getInstance().mount();
            demoTopicMessageHandler.process(event.getMessageData());
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            super.basicReject(channel,deliveryTag);
        }
    }




    /**
     * 监听Spring事件消息
     * @param event 事件对象
     */
    @Async
    @EventListener
    public void onSpringEvent(DemoTopicEvent event) {
        log.info("[Demo-Listener]接收到事件：{}", JSON.toJSONString(event));
        demoTopicMessageHandler.process(event.getMessageData());
    }
}