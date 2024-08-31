package org.lyflexi.boot_rabbit_tx_producer.config;

import lombok.Data;
import org.lyflexi.boot_rabbit_common.constant.TopicMqConstant;
import org.lyflexi.boot_rabbit_common.constant.TxMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Data
@Lazy(value = false)
public class TxMqConfig {

    /**
     * 创建rabbitmq事务管理器
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTransactionManager transactionManager(CachingConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    /**
     * 对rabbitTemplate指定setChannelTransacted为true
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    //声明交换机
    @Bean
    public Exchange txExchange(){
        return ExchangeBuilder.topicExchange(TxMqConstant.EXCHANGE).durable(true).build();
    }

    //声明队列
    @Bean
    public Queue txQueue(){
        return QueueBuilder.durable(TxMqConstant.QUEUE).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding txBind(@Qualifier("txQueue")Queue queue,
                                    @Qualifier("txExchange")Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(TxMqConstant.ROUTING_KEY).noargs();
    }
}