package org.lyflexi.customrabbitframeworkv2.commonapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 15:54
 */
@Configuration
@Slf4j
public class RabbitMQConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     * Shutdown Signal: channel error; protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - unknown delivery tag 1, class-id=60, method-id=90)
     * Restarting Consumer@50110971: tags=[[amq.ctag-UvbT-lPKFt2W9E0c5saOhg]], channel=Cached Rabbit Channel: PublisherCallbackChannelImpl: AMQChannel(amqp://MY_USER@10.28.20.10:31401/,1), conn: Proxy@2921a36a Shared Rabbit Connection: SimpleConnection@298b64f7 [delegate=amqp://MY_USER@10.28.20.10:31401/, localPort=57972], acknowledgeMode=MANUAL local queue size=0
     * @param connectionFactory
     * @return
     *
     *     体现重点的有两条：
     *     1、com.rabbitmq.client.ShutdownSignalException: channel error; protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - unknown delivery tag 1, class-id=60, method-id=80)
     *
     *     表明这条消息对于MQ来说没找到，重点是这个---unknown delivery tag 1-参考rabbitMQ的tag机制可知这条消息已经完成了消费。
     *
     *     2、Restarting Consumer: tags=[{amq.ctag-zms15dchTPRiv8SH2W0_8Q=videoChange}], channel=Cached Rabbit Channel: AMQChannel(amqp://admin@59.110.229.19:5672/,2), conn: Proxy@131c5bd Shared Rabbit Connection: SimpleConnection@3a500321 [delegate=amqp://admin@59.110.229.19:5672/, localPort= 50032], acknowledgeMode=AUTO local queue size=0
     *
     *     表明这条消息被一个设置为自动确认的任务（至于他具体是啥，怎么执行的，没深入看源码，等大神给解释了。）给确认了。
     *
     *     各种百度，发现MQ配置的时候，如果配置了json解析器。如下：
     *
     *     @Bean
     *     public MessageConverter messageConverter() {
     *         return new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter());
     *     }
     *     则程序会走自动确认消费，配置文件的配置就不生效了（这个不知道为什么，也可能springboot中某些配置的先后顺序的问题）
     *
     *     这个问题解决办法就是重写一下这个东东，用代码设置手动确认。就OK了
     */
    //TODO 还是未解决
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

}