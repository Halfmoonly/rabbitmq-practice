package org.lyflexi.boot_rabbit_consumer.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.boot_rabbit_common.constant.DelayConstant;
import org.lyflexi.boot_rabbit_common.constant.PriorityConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class PriorityListener {


    @RabbitListener(queues = {PriorityConstant.QUEUE})
    public void processMessagePriority(String dataString, Message message, Channel channel) throws IOException {
        log.info("[priority]" + dataString);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}