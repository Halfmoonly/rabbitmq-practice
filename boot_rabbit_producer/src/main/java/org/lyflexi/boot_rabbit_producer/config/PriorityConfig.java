package org.lyflexi.boot_rabbit_producer.config;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.boot_rabbit_common.constant.DeadLetterConstant;
import org.lyflexi.boot_rabbit_common.constant.PriorityConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/24 12:11
 */
@Configuration
@Slf4j
@Lazy(false)
public class PriorityConfig {

    @Bean
    DirectExchange priorityExchange(){
        return new DirectExchange(PriorityConstant.EXCHANGE);
    }
    @Bean
    Queue priorityQueue(){
        Map<String,Object> map = new HashMap<>();
        map.put("x-max-priority",10);//设置最大的优先级数量,之后发送消息的时候给消息设置的优先级不能大于此值
        return new Queue(PriorityConstant.QUEUE,true,false,false,map);
    }
    @Bean
    public Binding priorityBinding(){
        return BindingBuilder.bind(priorityQueue()).to(priorityExchange()).with(PriorityConstant.ROUTING_KEY);
    }
}
