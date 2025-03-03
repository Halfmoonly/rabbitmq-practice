package org.lyflexi.customrabbitframeworkv2.commonapi.publisher;


import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customrabbitframeworkv2.commonapi.constant.MQIConstant;
import org.lyflexi.customrabbitframeworkv2.commonapi.enums.EventTypeEnums;
import org.lyflexi.customrabbitframeworkv2.commonapi.event.IEvent;
import org.lyflexi.customrabbitframeworkv2.commonapi.message.IMessageData;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * rabbitmq同步发送模版
 */
@Slf4j
@Component("rabbitEventPublisher")
public class RabbitEventPublisher implements IEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public boolean publish(IEvent event) {
        IMessageData message = event.getMessageData();
        message.assertParams();
        log.info("准备发送MQ消息，seqNo:{}, messageId:{}, version:{}, factoryCode:{}", message.getSeqNo(), message.getMessageId(), message.getVersion(), message.getFactoryCode());
        EventTypeEnums eventType = EventTypeEnums.getEventType(event.getEventType());
        CorrelationData correlationData = new CorrelationData(message.getSeqNo());
        correlationData.setReturned(new ReturnedMessage(new Message(message.getFactoryCode().getBytes(StandardCharsets.UTF_8), new MessageProperties()), 0,"","",""));
        rabbitTemplate.convertAndSend(eventType.getExchange(), eventType.getRoutingKey(), message, process -> {
            process.getMessageProperties().getHeaders().put(MQIConstant.COUNTER_KEY, 0);
            return process;
        }, correlationData);
        log.info("发送MQ消息成功，queue:{}, seqNo:{}", eventType.getQueues(), message.getSeqNo());
        return true;
    }
}
