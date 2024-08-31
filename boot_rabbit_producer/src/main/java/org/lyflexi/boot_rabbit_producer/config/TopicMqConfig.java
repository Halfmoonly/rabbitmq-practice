package org.lyflexi.boot_rabbit_producer.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.boot_rabbit_common.constant.TopicMqConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;


@Configuration
@Slf4j
@Lazy(false)
public class TopicMqConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    //声明交换机
    @Bean("bootTopicExchange")
    public Exchange bootTopicExchange(){
        return ExchangeBuilder.topicExchange(TopicMqConstant.BOOT_TOPIC_EXCHANGE).durable(true).build();
    }

    //声明队列
    @Bean("bootQueue")
    public Queue bootQueue(){
        return QueueBuilder.durable(TopicMqConstant.BOOT_QUEUE).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding bootQueueExchage(@Qualifier("bootQueue")Queue bootQueue,
                                    @Qualifier("bootTopicExchange")Exchange bootTopicExchange){
        return BindingBuilder.bind(bootQueue).to(bootTopicExchange).with(TopicMqConstant.BOOT_TOPIC_EXCHANGE_KEY).noargs();
    }


    /**
     * 使用@PostConstruct注解的方法必须满足以下条件：
     * 1. 方法不能有任何参数
     * 2. 方法必须是非静态的
     * 3. 方法不能返回任何值
     *
     * @PostConstruct的执行时机
     * 当容器实例化一个带有@PostConstruct注解的Bean时，它会在调用构造函数之后，并在被依赖注入完成之前调用被@PostConstruct注解标记的方法。
     * 这样，我们可以在该方法中进行一些初始化操作，比如读取配置文件、建立数据库连接等。
     */
    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 消息发送到交换机成功时调用这个ConfirmCallback#confirm
     * 消息发送到交换机失败时调用这个ConfirmCallback#confirm
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("confirm() 回调函数打印 CorrelationData：" + correlationData);
        log.info("confirm() 回调函数打印 ack：" + ack);
        log.info("confirm() 回调函数打印 cause：" + cause);
    }

    /**
     * 仅在发送到队列失败时才调用这个ReturnsCallback#returnedMessage
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("returnedMessage() 回调函数 消息主体: " + new String(returned.getMessage().getBody()));
        log.info("returnedMessage() 回调函数 应答码: " + returned.getReplyCode());
        log.info("returnedMessage() 回调函数 描述：" + returned.getReplyText());
        log.info("returnedMessage() 回调函数 消息使用的交换器 exchange : " + returned.getExchange());
        log.info("returnedMessage() 回调函数 消息使用的路由键 routing : " + returned.getRoutingKey());
    }
}
