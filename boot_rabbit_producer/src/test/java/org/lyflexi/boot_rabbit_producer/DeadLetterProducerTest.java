package org.lyflexi.boot_rabbit_producer;

import org.junit.jupiter.api.Test;
import org.lyflexi.boot_rabbit_common.constant.DeadLetterConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeadLetterProducerTest {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试死信队列的第一种情况：消费者端拒收
     */
    @Test
    public void testDeadReject() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(
                    DeadLetterConstant.EXCHANGE_NORMAL,
                    DeadLetterConstant.ROUTING_KEY_NORMAL,
                    "测试死信情况1：消费者端拒收" + i);
        }
    }
    /**
     * 测试死信队列的第二种情况：消息数量超过队列的最大容量
     * 和三种情况：超时时间
     *
     * 注意这个测试要先停掉消费者程序
     * */
    @Test
    public void testDeadMaxLengthAndTTL() {
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(
                    DeadLetterConstant.EXCHANGE_NORMAL,
                    DeadLetterConstant.ROUTING_KEY_NORMAL,
                    "测试死信情况2：消息数量超过队列的最大容量" + i);
        }
    }
}
