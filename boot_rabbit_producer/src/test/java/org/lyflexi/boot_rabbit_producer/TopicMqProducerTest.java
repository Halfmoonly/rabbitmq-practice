package org.lyflexi.boot_rabbit_producer;

import org.junit.jupiter.api.Test;
import org.lyflexi.boot_rabbit_common.constant.TopicMqConstant;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TopicMqProducerTest {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试topic模式
     */
    @Test
    public void test01SendMessage() {
        rabbitTemplate.convertAndSend(TopicMqConstant.BOOT_TOPIC_EXCHANGE,
                "boot.l.y",
                "Message Test Confirm~~~ ~~~");
    }
    /**
     * 测试消费端限流
     */
    @Test
    public void test02SendMessage() {
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(TopicMqConstant.BOOT_TOPIC_EXCHANGE, "boot.l.y", "Test Prefetch " + i);
        }
    }
    /**
     * 测试消息超时
     */
    @Test
    public void test04SendMessage() {

        // 创建消息后置处理器对象
        MessagePostProcessor postProcessor = message -> {

            // 设置消息的过期时间，单位是毫秒
            message.getMessageProperties().setExpiration("7000");

            return message;
        };

        rabbitTemplate.convertAndSend(TopicMqConstant.BOOT_TOPIC_EXCHANGE, "boot.l.y", "Test timeout", postProcessor);
    }
}
