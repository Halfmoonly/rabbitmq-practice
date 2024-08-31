package org.lyflexi.boot_rabbit_producer.config;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.boot_rabbit_common.constant.DelayConstant;
import org.springframework.amqp.core.*;
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
public class DelayConfig {
    /**
     * x-delayed-type：指定rabbitmq原生的交换机类型
     * @return  具体实现时，需要在配置类中创建延迟交换机（CustomExchange）并设置其类型为`x-delayed-message`
     */
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "topic"); // 这里的"direct"可以根据需要替换为其他类型的交换机
        return new CustomExchange(DelayConstant.EXCHANGE, "x-delayed-message", true, false, args);
    }


    // 创建死信队列
    @Bean
    public Queue delayQueue(){
        return QueueBuilder.durable(DelayConstant.QUEUE).build();
    }

    // 创建死信交换机和死信队列的绑定关系
    @Bean
    public Binding delayBinding(@Qualifier("delayExchange") Exchange exchange, @Qualifier("delayQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(DelayConstant.ROUTING_KEY).noargs();
    }

}
