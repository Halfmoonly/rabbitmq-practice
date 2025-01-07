package org.lyflexi.customrabbitframeworkv2.commonapi.config;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customrabbitframeworkv2.commonapi.enums.EventTypeEnums;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @Author: lyflexi
 * @project: rabbitmq-practice
 * @Date: 2024/9/4 16:00
 */

@Slf4j
@Component
public class AutoLoadQueuePostprocessor implements BeanFactoryPostProcessor {

    @PostConstruct
    public void init() {
        log.debug("自动加载消息队列后置处理器初始化");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Arrays.stream(EventTypeEnums.values()).forEach(event -> {
            String exName = event.getExchange();
            List<String> queues = event.getQueues();
            String routingKey = event.getRoutingKey();
            String type = event.getType();
            if (type.equals(ExchangeTypes.FANOUT)) {
                // 广播模式
                FanoutExchange exchange = getBean(beanFactory, exName, FanoutExchange.class);
                if (Objects.isNull(exchange)) {
                    exchange = new FanoutExchange(exName);
                    beanFactory.registerSingleton(exName, exchange);
                    log.info("已注册消息FANOUT交换机-{}", exName);
                }
                for (String q : queues) {
                    Queue queue = getBean(beanFactory, q, Queue.class);
                    if (Objects.isNull(queue)) {
                        queue = new Queue(q, true);
                        beanFactory.registerSingleton(q, queue);
                        log.info("已注册消息交队列-{}", q);
                    }
                    String bindingKey = exName + ":" + q;
                    Binding binding = getBean(beanFactory, bindingKey, Binding.class);
                    if (Objects.isNull(binding)) {
                        binding = BindingBuilder.bind(queue).to(exchange);
                        beanFactory.registerSingleton(bindingKey, binding);
                        log.info("已注册消息FANOUT绑定器-{}", bindingKey);
                    }
                }
            } else if (type.equals(ExchangeTypes.TOPIC)) {
                TopicExchange exchange = getBean(beanFactory, exName, TopicExchange.class);
                if (Objects.isNull(exchange)) {
                    exchange = new TopicExchange(exName);
                    beanFactory.registerSingleton(exName, exchange);
                    log.info("已注册消息交换机-{}", exName);
                }
                String qName = queues.get(0);
                Queue queue = getBean(beanFactory, qName, Queue.class);
                if (Objects.isNull(queue)) {
                    queue = new Queue(qName, true);
                    beanFactory.registerSingleton(qName, queue);
                    log.info("已注册消息交队列-{}", qName);
                }
                Binding binding = getBean(beanFactory, routingKey, Binding.class);
                if (Objects.isNull(binding)) {
                    binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
                    beanFactory.registerSingleton(routingKey, binding);
                    log.info("已注册消息绑定器-{}", routingKey);
                }
            }

        });
        log.info("系统已完成LES消息基础配置加载!");
    }

    @Bean
    public MessageConverter messageConverter () {
        return new Jackson2JsonMessageConverter();
    }

    private <T> T getBean (ConfigurableListableBeanFactory beanFactory, String name, Class<T> clazz) {
        try {
            return beanFactory.getBean(name, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
