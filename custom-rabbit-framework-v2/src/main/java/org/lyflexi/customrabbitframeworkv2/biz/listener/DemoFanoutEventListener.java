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
public class DemoFanoutEventListener extends AbstractListener {

    @Autowired
    private DemoTopicMessageHandler demoTopicMessageHandler;

    /**
     * 监听 Fanout1
     * @param message 消息对象
     * @param channel MQ通道
     * @param event 时间对象
     */
    @RabbitListener(queues = MQIConstant.DEMO_EVENT_FANOUT_QUEUE1, concurrency = "1")
    public void onRabbitMQEvent1(Message message, Channel channel, DemoTopicEvent event) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("DEMO_EVENT_FANOUT_QUEUE1:{}",event);
            //由于消息是异步的，切记一定要设置系统上下文，后续BizContextHolder将填充为admin信息
            SystemTaskerContextHolder.getInstance().mount();
            demoTopicMessageHandler.process(event.getMessageData());
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            super.basicReject(channel,deliveryTag);
            throw new RuntimeException(e);
        } finally {
            //清理上下文
        }
    }
    /**
     * 监听 Fanout2
     * @param message 消息对象
     * @param channel MQ通道
     * @param event 时间对象
     */
    @RabbitListener(queues = MQIConstant.DEMO_EVENT_FANOUT_QUEUE2, concurrency = "1")
    public void onRabbitMQEvent2(Message message, Channel channel, DemoTopicEvent event) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("DEMO_EVENT_FANOUT_QUEUE2:{}",event);
            //由于消息是异步的，切记一定要设置系统上下文，后续BizContextHolder将填充为admin信息
            SystemTaskerContextHolder.getInstance().mount();
            demoTopicMessageHandler.process(event.getMessageData());
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            super.basicReject(channel,deliveryTag);
            throw new RuntimeException(e);
        } finally {
            //清理上下文
        }
    }
}