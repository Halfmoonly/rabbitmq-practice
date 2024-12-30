package org.lyflexi.custom_rabbit_framework.commonapi.listener;

import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * @Description:
 * @Author: Halfmoonly
 * @project: rabbitmq-practice
 * @Date: 2024/12/30 8:59
 */
public class AbstractListener implements  IListener{
    /**
     * 处理失败时对MQ ack
     * @param channel rabbitmq channel
     * @param deliveryTag rabbitmq message deliveryTag
     */
    public void basicReject (Channel channel, long deliveryTag) {
        try {
            channel.basicReject(deliveryTag, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
