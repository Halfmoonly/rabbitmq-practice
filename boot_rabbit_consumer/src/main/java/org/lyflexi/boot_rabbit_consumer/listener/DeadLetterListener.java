package org.lyflexi.boot_rabbit_consumer.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.boot_rabbit_common.constant.DeadLetterConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class DeadLetterListener {


    /**
     * 测试死信队列的第一种情况：消费者端拒收
     */
    @RabbitListener(queues = {DeadLetterConstant.QUEUE_NORMAL})
    public void processMessageNormal(Message message, Channel channel) throws IOException {
        // 监听正常队列，但是拒绝消息
        log.info("★[normal]消息接收到，但我拒绝。");
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 测试死信队列的第二种情况：消息数量超过队列的最大容量
     * 和三种情况：超时时间
     *
     * 注意这个测试要先停掉消费者程序
     * */
    @RabbitListener(queues = {DeadLetterConstant.QUEUE_DEAD_LETTER})
    public void processMessageDead(String dataString, Message message, Channel channel) throws IOException {
        // 监听死信队列
        log.info("★[dead letter]我是死信监听方法，我接收到了死信消息:"+dataString);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}