package org.lyflexi.boot_rabbit_tx_producer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.lyflexi.boot_rabbit_common.constant.TxMqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
class BootRabbitTxProducerApplicationTests {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    @Transactional
//    @Rollback(value = false) // junit 默认回滚事务，所以想提交事务就设置为 false
    public void testSendMessageInTx() {
        // 1、发送第一条消息
        rabbitTemplate.convertAndSend(TxMqConstant.EXCHANGE, "tx.ly", "I am a dragon(tx msg ~~~01~~~)");

        // 2、抛出异常
        log.info("do bad:" + 10 / 0);

        // 3、发送第二条消息
        rabbitTemplate.convertAndSend(TxMqConstant.EXCHANGE, "tx.ly", "I am a dragon(tx msg ~~~02~~~)");
    }

}
