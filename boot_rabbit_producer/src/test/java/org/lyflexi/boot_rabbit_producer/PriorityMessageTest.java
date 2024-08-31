package org.lyflexi.boot_rabbit_producer;

import org.junit.jupiter.api.Test;
import org.lyflexi.boot_rabbit_common.constant.PriorityConstant;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class PriorityMessageTest {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testPriority1() {
        rabbitTemplate.convertAndSend(PriorityConstant.EXCHANGE, PriorityConstant.ROUTING_KEY, "message test proirity 1", message -> {

            // 消息本身的优先级数值
            // 切记：不能超过 x-max-priority:	10
            message.getMessageProperties().setPriority(1);

            return message;
        });
    }
    @Test
    public void testPriority2() {
        rabbitTemplate.convertAndSend(PriorityConstant.EXCHANGE, PriorityConstant.ROUTING_KEY, "message test proirity 2", message -> {

            // 消息本身的优先级数值
            // 切记：不能超过 x-max-priority:	10
            message.getMessageProperties().setPriority(2);

            return message;
        });
    }
    @Test
    public void testPriority3() {
        rabbitTemplate.convertAndSend(PriorityConstant.EXCHANGE, PriorityConstant.ROUTING_KEY, "message test proirity 3", message -> {

            // 消息本身的优先级数值
            // 切记：不能超过 x-max-priority:	10
            message.getMessageProperties().setPriority(3);

            return message;
        });
    }

}
