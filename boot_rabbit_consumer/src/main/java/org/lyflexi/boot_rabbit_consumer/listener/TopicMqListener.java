package org.lyflexi.boot_rabbit_consumer.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.boot_rabbit_common.constant.TopicMqConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TopicMqListener {
//    @RabbitListener(queues = {RabbitMqConstant.BOOT_QUEUE})
    public void bootProcess(String dataString, Message message, Channel channel) throws IOException, InterruptedException {
        log.info("消费端 消息内容：" + dataString);
    }

    /**
     * 测试消息确认机制
     * @param dataString
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = {TopicMqConstant.BOOT_QUEUE})
    public void processMessage(String dataString, Message message, Channel channel) throws IOException {

        // 获取当前消息的 deliveryTag
        // deliveryTag默认是64位整数
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 核心操作
            log.info("消费端 消息内容：" + dataString);

            System.out.println(10 / 0);

            // 消费者手动确认模式：返回 ACK 信息，队列需要利用deliveryTag进行后续的处理，例如删除消息、重新排队或标记为死信等等。
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.info("进入catch异常");
            // 获取当前消息是否是重复投递的
            //      redelivered 为 true：说明当前消息已经重复投递过一次了
            //      redelivered 为 false：说明当前消息是第一次投递
            Boolean redelivered = message.getMessageProperties().getRedelivered();

            // 核心操作失败：返回 NACK 信息
            // requeue 参数：控制消息是否重新放回队列
            //      取值为 true：重新放回队列，broker 会重新投递这个消息
            //      取值为 false：不重新放回队列，broker 会丢弃这个消息

            if (redelivered) {
                // 如果当前消息已经是重复投递的，说明此前已经重试过一次啦，所以 requeue 设置为 false，表示不重新放回队列
                channel.basicNack(deliveryTag, false, false);
            } else {
                // 如果当前消息是第一次投递，说明当前代码是第一次抛异常，尚未重试，所以 requeue 设置为 true，表示重新放回队列在投递一次
                channel.basicNack(deliveryTag, false, true);
            }

            // reject 表示拒绝
            // 辨析：basicNack() 和 basicReject() 方法区别
            // basicNack()能控制是否批量操作
            // basicReject()不能控制是否批量操作
            // channel.basicReject(deliveryTag, true);
        }
    }

    /**
     * 测试消费端限流
     * @param dataString
     * @param message
     * @param channel
     * @throws IOException
     * @throws InterruptedException
     */
    @RabbitListener(queues = {TopicMqConstant.BOOT_QUEUE})
    public void processMessageTestPrefetch(String dataString, Message message, Channel channel) throws IOException, InterruptedException {
        log.info("消费端 消息内容：" + dataString);

        //如果消费者不设置prefetch，光靠睡眠时间是无法控制消费速率的
        TimeUnit.SECONDS.sleep(1);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 测试消费超时
     * @param dataString
     * @param message
     * @param channel
     * @throws IOException
     * @throws InterruptedException
     */
    @RabbitListener(queues = {TopicMqConstant.BOOT_QUEUE})
    public void processMessageTimeOut(String dataString, Message message, Channel channel) throws IOException, InterruptedException {

    }
}