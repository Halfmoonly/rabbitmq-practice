package org.lyflexi.customrabbitframeworkv2.commonapi.publisher;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lyflexi.customrabbitframeworkv2.commonapi.enums.DeliverStatusEnum;
import org.lyflexi.customrabbitframeworkv2.commonapi.utils.Assert;
import org.lyflexi.customrabbitframeworkv2.commonapi.utils.ExecutorUtil;
import org.lyflexi.customrabbitframeworkv2.commonapi.constant.MQIConstant;
import org.lyflexi.customrabbitframeworkv2.commonapi.delay.DelayEntity;
import org.lyflexi.customrabbitframeworkv2.commonapi.enums.EventTypeEnums;
import org.lyflexi.customrabbitframeworkv2.commonapi.event.IEvent;
import org.lyflexi.customrabbitframeworkv2.commonapi.message.IMessageData;
import org.lyflexi.customrabbitframeworkv2.commonapi.po.SysConsumerMsgLogPo;
import org.lyflexi.customrabbitframeworkv2.commonapi.service.IMessageLogService;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.DelayQueue;

/**
 * @ClassName: SyncLesMessageProducerImpl
 * @Description: LES消息生产者-通用异步发送
 * @Author: ma.wenlei
 * @Version: v1.0
 * @Date: 2024/6/6
 */
@Slf4j
@Component("asyncRabbitEventPublisher")
public class AsyncRabbitEventPublisher implements IEventPublisher, SmartInitializingSingleton {

    private DelayQueue<DelayEntity<IEvent>> delay = new DelayQueue<>();

    private static final Long DEFAULT_DELAY_TIME = 5000L;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IMessageLogService msgLogService;


    /**
     * 直接发送Event对象
     * @param event 事件对象
     * @return
     */
    @Override
    public boolean publish(IEvent event) {
        Assert.notNull(event.getEventType(), "发布事件事件类型不能为空！");
        Assert.notNull(event.getMessageData(), "发布事件消息体不能为空！");
        event.getMessageData().assertParams();
        log.info("准备发布事件，eventType:{}, seqNo:{}, messageId:{}", event.getEventType(), event.getMessageData().getSeqNo(), event.getMessageData().getMessageId());
        EventTypeEnums eventType = EventTypeEnums.getEventType(event.getEventType());
        Assert.notNull(eventType, "发布事件事件类型无效！");
        delay.put(new DelayEntity<>(event, eventType, DEFAULT_DELAY_TIME));
        log.info("事件已加入队列，稍后会自动发送，当前队列深度：{}", delay.size());
        return true;
    }

    @Override
    public void afterSingletonsInstantiated() {
        ExecutorUtil.pool.execute(()-> {
            while (true) {
                try {
                    DelayEntity<IEvent> delayEntity = delay.take();
                    IEvent event = delayEntity.getData();
                    IMessageData message = event.getMessageData();
                    log.info("准备发送MQ消息，seqNo:{}, messageId:{}, version:{}, factoryCode:{}", message.getSeqNo(), message.getMessageId(), message.getVersion(), message.getFactoryCode());
                    EventTypeEnums eventType = delayEntity.getEventType();
                    if (StringUtils.isNotEmpty(message.getMessageId()) && msgLogService.getOne(message.getMessageId(), message.getVersion())!=null) {
                        log.warn("发送MQ消息系统预判重复消息，忽略此消息，seqNo:{}, messageId:{}, version:{}", message.getSeqNo(), message.getMessageId(), message.getVersion());
                        continue;
                    }
                    SysConsumerMsgLogPo msgLogPo = SysConsumerMsgLogPo.builder()
                            .msgId(message.getSeqNo())
                            .factoryCode(message.getFactoryCode())
                            .dataId(message.getMessageId())
                            .dataVersion(message.getVersion())
                            .exchange(eventType.getExchange())
                            .routingKey(eventType.getRoutingKey())
                            .queue(eventType.getQueue())
                            .msg(JSONObject.toJSONString(message))
                            .status(DeliverStatusEnum.SEND.getCode())
                            .build();
                    msgLogService.save(msgLogPo);
                    CorrelationData correlationData = new CorrelationData(message.getSeqNo());
                    correlationData.setReturned(new ReturnedMessage(new Message(message.getFactoryCode().getBytes(StandardCharsets.UTF_8), new MessageProperties()), 0,"",eventType.getExchange(),eventType.getRoutingKey()));
                    rabbitTemplate.convertAndSend(eventType.getExchange(), eventType.getRoutingKey(), event, process -> {
                        process.getMessageProperties().getHeaders().put(MQIConstant.COUNTER_KEY, 0);
                        return process;
                    }, correlationData);
                    log.info("发送MQ消息成功，queue:{}, seqNo:{}", eventType.getQueue(), message.getSeqNo());
                } catch (Exception e) {
                    log.error("发送MQ消息失败：", e);
                } finally {

                }
            }
        });
    }
}