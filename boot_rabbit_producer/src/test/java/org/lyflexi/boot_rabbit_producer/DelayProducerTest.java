package org.lyflexi.boot_rabbit_producer;

import org.junit.jupiter.api.Test;
import org.lyflexi.boot_rabbit_common.constant.DelayConstant;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class DelayProducerTest {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test05SendMessageDelay() {

        // 创建消息后置处理器对象
        MessagePostProcessor postProcessor = message -> {

            // 设置消息过期时间（以毫秒为单位）
            // x-delay 参数必须基于 x-delayed-message-exchange 插件才能生效
            message.getMessageProperties().setHeader("x-delay", "10000");

            return message;
        };

        // 发送消息
        rabbitTemplate.convertAndSend(
                DelayConstant.EXCHANGE,
                "delay.ly",
                "Test delay message by plugin " + new SimpleDateFormat("HH:mm:ss").format(new Date()),
                postProcessor);
    }

}
