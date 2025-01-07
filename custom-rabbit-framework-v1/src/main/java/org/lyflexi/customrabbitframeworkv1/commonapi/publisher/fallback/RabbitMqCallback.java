package org.lyflexi.customrabbitframeworkv1.commonapi.publisher.fallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lyflexi.customrabbitframeworkv1.commonapi.enums.DeliverStatusEnum;
import org.lyflexi.customrabbitframeworkv1.commonapi.utils.ExecutorUtil;
import org.lyflexi.customrabbitframeworkv1.commonapi.message.BaseMessageData;
import org.lyflexi.customrabbitframeworkv1.commonapi.po.SysConsumerMsgLogPo;
import org.lyflexi.customrabbitframeworkv1.commonapi.service.IMessageLogService;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Objects;


@Slf4j
@Component
public class RabbitMqCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private IMessageLogService msgLogService;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        ExecutorUtil.pool.execute(()-> {
            try {
                if (Objects.isNull(correlationData)) {
                    log.error("MQ消息发送状态确认，但是CorrelationData为空！");
                    return;
                }
                String id = correlationData.getId();
                ReturnedMessage returnedMessage = correlationData.getReturned();
                log.info("MQ消息发送状态确认，确认结果: id = {} , ack = {}", id , ack);
                SysConsumerMsgLogPo msgLogPo = SysConsumerMsgLogPo.builder().msgId(id).editTime(LocalDateTime.now()).build();
                if (ack) {
                    msgLogPo.setStatus(DeliverStatusEnum.SUCCESS.getCode());
                    log.info("MQ消息发送状态确认 - 发送成功，correlationData：${}$", correlationData);
                } else {
                    msgLogPo.setStatus(DeliverStatusEnum.FAIL.getCode());
                    log.info("MQ消息发送状态确认 - 发送失败，correlationData：${}$，returned：${}$，cause：${}$", correlationData, returnedMessage, cause);
                }
                Message message = (Objects.nonNull(returnedMessage)) ? returnedMessage.getMessage() : null;
                if (Objects.isNull(message)) {
                    log.error("MQ消息发送状态确认，解析Message对象为空！msgLog:{}", JSON.toJSONString(msgLogPo));
                    return;
                }
                byte[] data = message.getBody();
                if (Objects.isNull(data)) {
                    log.error("MQ消息发送状态确认，解析Message.body数据为空！msgLog:{}", JSON.toJSONString(msgLogPo));
                    return;
                }
                String factoryCode = new String(data);
                log.info("MQ消息发送状态确认，factoryCode:{}", factoryCode);
                if (StringUtils.isBlank(factoryCode)) {
                    log.error("MQ消息发送状态确认，解析factoryCode为空！msgLog:{}", JSON.toJSONString(msgLogPo));
                    return;
                }
                msgLogService.updateById(msgLogPo);
                log.info("MQ消息发送状态确认，消息状态更新完成！msgId:{}, status:{}", id, msgLogPo.getStatus());
            } catch (Exception e) {
                log.error("MQ消息发送状态确认，消息状态处理失败！", e);
            } finally {
            }
        });
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        ExecutorUtil.pool.execute(()-> {
            try {
                Message message = returnedMessage.getMessage();
                log.info("MQ消息消费状态确认 - 失败了，message：${}$", message);
                byte[] body = message.getBody();
                if(Objects.isNull(body)) {
                    log.error("MQ消息消费状态确认，解析Message.body数据为空！");
                    return;
                }
                BaseMessageData baseMessage = JSONObject.parseObject(new String(body), BaseMessageData.class);
                if(Objects.isNull(baseMessage)) {
                    log.error("MQ消息消费状态确认，解析BaseMessage数据为空！");
                    return;
                }
                String factoryCode = baseMessage.getFactoryCode();
                log.info("MQ消息消费状态确认，factoryCode:{}, msgId:{}", factoryCode, baseMessage.getSeqNo());
                if (StringUtils.isBlank(factoryCode)) {
                    log.error("MQ消息消费状态确认，解析factoryCode为空！factoryCode:{}, msgId:{}", factoryCode, baseMessage.getSeqNo());
                    return;
                }
                SysConsumerMsgLogPo msgLogPo = SysConsumerMsgLogPo.builder().msgId(baseMessage.getSeqNo()).status(DeliverStatusEnum.FAIL.getCode()).editTime(LocalDateTime.now()).build();
                msgLogService.updateById(msgLogPo);
                log.info("MQ消息消费状态确认，消息状态更新完成！msgId:{}, status:{}", baseMessage.getSeqNo(), msgLogPo.getStatus());
            } catch (Exception e) {
                log.error("MQ消息消费状态确认，消息状态处理失败！", e);
            } finally {
            }
        });
    }

    @PostConstruct
    public void init () {
        log.info("设置RabbitTemplate ConfirmCallback..");
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setConfirmCallback(this);
        log.info("设置RabbitTemplate setReturnCallback..");
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(this);
    }
}
