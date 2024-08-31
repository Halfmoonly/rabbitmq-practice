package org.lyflexi.boot_rabbit_producer.config;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.boot_rabbit_common.constant.DeadLetterConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/24 12:11
 */
@Configuration
@Slf4j
@Lazy(false)
public class DeadLetterConfig {
    // 添加json格式序列化器
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    // 创建死信交换机
    @Bean
    public Exchange deadLetterExchange(){
        return ExchangeBuilder.topicExchange(DeadLetterConstant.EXCHANGE_DEAD_LETTER).durable(true).build();
    }

    // 创建死信队列
    @Bean
    public Queue deadLetterQueue(){
        return QueueBuilder.durable(DeadLetterConstant.QUEUE_DEAD_LETTER).build();
    }

    // 创建死信交换机和死信队列的绑定关系
    @Bean
    public Binding deadLetterBinding(@Qualifier("deadLetterExchange") Exchange exchange, @Qualifier("deadLetterQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(DeadLetterConstant.ROUTING_KEY_DEAD_LETTER).noargs();
    }


    // 创建普通交换机
    @Bean
    public Exchange normalExchange(){
        return ExchangeBuilder.topicExchange(DeadLetterConstant.EXCHANGE_NORMAL).durable(true).build();
    }

    /**
     * 普通队列也要设置待测试的死信交换机和死信交换机相应的key
     * @return
     */
    @Bean
    public Queue normalQueue(){
        // 创建普通队列，设置ttl为10秒
        // 创建普通队列，队列最大长度为10
        return QueueBuilder.durable(DeadLetterConstant.QUEUE_NORMAL).ttl(10000).maxLength(10)
                //绑定死信交换机
                .deadLetterExchange(DeadLetterConstant.EXCHANGE_DEAD_LETTER).deadLetterRoutingKey("routing.key.dead.letter.video.ly").build();
    }
    // 创建普通交换机和普通队列的绑定关系
    @Bean
    public Binding normalBinding(@Qualifier("normalExchange") Exchange exchange, @Qualifier("normalQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(DeadLetterConstant.ROUTING_KEY_NORMAL).noargs();
    }
}
