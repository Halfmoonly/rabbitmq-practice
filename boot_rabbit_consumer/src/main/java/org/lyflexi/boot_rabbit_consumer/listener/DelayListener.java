package org.lyflexi.boot_rabbit_consumer.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.boot_rabbit_common.constant.DeadLetterConstant;
import org.lyflexi.boot_rabbit_common.constant.DelayConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class DelayListener {


    @RabbitListener(queues = {DelayConstant.QUEUE})
    public void processMessageDelay(String dataString, Message message, Channel channel) throws IOException {
        log.info("[delay message][消息本身]" + dataString);
        log.info("[delay message][当前时间]" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}