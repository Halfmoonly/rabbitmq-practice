package org.lyflexi.custom_rabbit_framework.commonapi.config;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.custom_rabbit_framework.commonapi.constant.MQIConstant;
import org.lyflexi.custom_rabbit_framework.commonapi.utils.KeyUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    /**
     * 定义默认交换机
     */
    @Bean(MQIConstant.LES_DEFAULT_TOPIC_EXCHANGE)
    public TopicExchange lesDefaultTopicExchange() {
        return new TopicExchange(MQIConstant.LES_DEFAULT_TOPIC_EXCHANGE);
    }

    /**
     * 定义默认交换机
     */
    @Bean(MQIConstant.TASK_SUBMITTED_QUEUE)
    public Queue passStationQueue() {
        // durable：队列持久化属性 true-持久化 false-不持久化
        // 注意不是消息持久化
        return new Queue(MQIConstant.TASK_SUBMITTED_QUEUE, true);
    }

    // 将队列绑定到交换机
    @Bean
    public Binding binding(@Autowired @Qualifier(MQIConstant.TASK_SUBMITTED_QUEUE) Queue passStationQueue,
                           @Autowired @Qualifier(MQIConstant.LES_DEFAULT_TOPIC_EXCHANGE) Exchange lesDefaultTopicExchange) {
        return BindingBuilder.bind(passStationQueue).to(lesDefaultTopicExchange).with(KeyUtil.generatorRoutingKey(MQIConstant.TASK_SUBMITTED_QUEUE)).noargs();
    }


    @Bean
    public MessageConverter messageConverter () {
        return new Jackson2JsonMessageConverter();
    }

}