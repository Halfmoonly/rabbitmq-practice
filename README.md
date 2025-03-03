# rabbitmq-practice
基础知识todo

# 消费者确认模式ack

消费者确认机制是基于数据安全的考虑。当rabbitmq将消息发送给消费者进行消费时，消费者可能会消费消息失败的情况，用户可以设置消费失败的消息给其他消费者消费或者直接丢弃。

自动确认模式积极的一面是能够拥有更高的吞吐量，但是却存在数据安全的问题。默认开启自动确认后，队列中的数据在给消费者后就认为是成功的处理了数据，因此会立马将队列里面的数据进行删除。

当消费者在消费消息时出现了异常，这些消息就会进行丢失。因此，一般情况下我们都需要手动确认去保证数据的安全性。

1. basicAck

basicAck方法是肯定的交付，一般在该消息处理完后执行，该消息才会在队列里面被删除，不然会处于UnAcked的状态存在队列中。

其方法有两个参数：

- 参数1：消费消息的index
- 参数2: 是否批量确认消息,前提是在同一个channel里面，且是在该消息确认前没有被确认的消息才能批量确认。
```java
public class Recv1 {
    public static void main(String[] args) throws IOException {
        Connection connection = MqUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare("work",true,false,false,null);
        // 设置通道的预取数量为1，官方推荐100到300，数据会影响其吞吐量
        channel.basicQos(10);
        // 关闭消息的自动确认机制
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body));
                // 在处理完消息后手动进行确认
                /*
                * 参数1： 消费消息的index
                * 参数2： 是否批量进行确认
                * */
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
```

2. basicReject

basicReject是否定的交付，一般在消费消息时出现异常等的时候执行。可以将该消息丢弃或重排序去重新处理消息

其方法有两个参数：

- 参数1: 消费消息的index
- 参数2: 对异常消息的处理，true表示重排序，false表示丢弃
```java
public class Recv1 {
    public static void main(String[] args) throws IOException {
        Connection connection = MqUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare("work",true,false,false,null);
        // 设置通道的预取数量为1，官方推荐100到300，数据会影响其吞吐量
        channel.basicQos(10);
        // 关闭消息的自动确认机制
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println(new String(body) + "----" + envelope.getDeliveryTag());
                    // 在处理完消息后手动进行确认
                    /*
                     * 参数1： 消息标签
                     * 参数2： 是否批量进行确认
                     * */
                    channel.basicAck(envelope.getDeliveryTag(), true);
                } catch (Exception e) {
                    channel.basicReject(envelope.getDeliveryTag(), false);
                }
            }
        });
    }
}
```

3.basicNack

basicNack也是否定的交付，其功能和basicReject是一样的。区别是basicNack比basicReject的功能更强一些。他能够一次丢弃多个或重排序多个消息

其方法有三个参数：

- 参数1：消费消息的index
- 参数2：是否批量否定多个消息，设为false就与basicReject功能一样，true的前提也是在同一个channel，且在该消息否定前存在未确认的消息
- 参数3： 对异常消息的处理，true表示重排序，false表示丢弃
```java
public class Recv1 {
    public static void main(String[] args) throws IOException {
        Connection connection = MqUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare("work",true,false,false,null);
        // 设置通道的预取数量为1，官方推荐100到300，数据会影响其吞吐量
        channel.basicQos(10);
        // 关闭消息的自动确认机制
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println(new String(body) + "----" + envelope.getDeliveryTag());
                    // 在处理完消息后手动进行确认
                    /*
                     * 参数1： 消息标签
                     * 参数2： 是否批量进行确认
                     * */
                    channel.basicAck(envelope.getDeliveryTag(), true);
                } catch (Exception e) {
                    channel.basicNack(envelope.getDeliveryTag(), false,true);
                }
            }
        });
    }
}
```

# 通道的预取设置

预取设置其含义是允许该通道未确认交付的最大数量。一旦达到该值，rabbitmq将不再往该消费者传递更多的消息。其好处是能够避免内存消耗过大，合理的设置预取值能够增加其吞吐量。官方推荐100到300之间可提供最佳的吞吐量。实际需要反复测试确定。代码参考上面，已经进行过设置

`channel.basicQos(10);`



# [custom-rabbit-framework-v1](custom-rabbit-framework-v1)

## 1.如何使用该框架进行事件发布与消费

### 如何发布事件：见MockController
- 发送异步rabbitmq消息 
- 同步发送rabbitmq消息
- 发布spring消息（仅用于相同服务内）

### 如何编写事件消费逻辑：
- 在[EventTypeEnums.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fcommonapi%2Fenums%2FEventTypeEnums.java)中新增枚举值，枚举值涵盖所有的rabbitmq配置信息：如
```java
DEMO_EVENT("demo_event", "MES_PASS_STATION_TOPIC", "TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_ROUTING:TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_TOPIC_EXCHANGE"),
```
- 自定义消息对象：如[DemoMessageData.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fbiz%2Fmessage%2FDemoMessageData.java)
```java
@EqualsAndHashCode(callSuper = true)
@Data
public class DemoMessageData extends BaseMessageData {

    /**
     * 业务ID
     */
    private String id;

    /**
     * 业务名称
     */
    private String name;

    @Override
    public String getMessageId() {
        return id;
    }
}
```
- 封装事件对象，将消息对象封装于事件之中：如[DemoEvent.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fbiz%2Fevent%2FDemoEvent.java)
```java
@Getter
@NoArgsConstructor
public class DemoEvent extends AbstractEvent<DemoMessageData> {
    /**
     * 初始状态
     */
    private DeliverStatusEnum sourceStatus;

    /**
     * 目标状态
     */
    private DeliverStatusEnum targetStatus;


    public DemoEvent(String eventType) {
        super(eventType);
    }

    public static DemoEvent of (DemoMessageData message) {
        DemoEvent event = new DemoEvent(EventTypeEnums.DEMO_EVENT.getEvent());
        event.setMessageData(message);
        return event;
    }

    public static DemoEvent of (DemoMessageData message ,DeliverStatusEnum sourceStatus,DeliverStatusEnum targetStatus) {
        DemoEvent event = new DemoEvent(EventTypeEnums.DEMO_EVENT.getEvent());
        event.setMessageData(message);
        event.sourceStatus = sourceStatus;
        event.targetStatus = targetStatus;
        return event;
    }


    public DemoMessageData getMessageData() {
        return super.getMessageData();
    }
}
```
- 自定义消息处理器，如[DemoMessageHandler.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fbiz%2Fhandler%2FDemoMessageHandler.java)
```java
@Slf4j
@Service
public class DemoMessageHandler extends AbstractHandler<DemoMessageData> {

    @Override
    public String getHandlerName() {
        return "DemoMessageHandler";
    }

    @Override
    public void doHandle(DemoMessageData messageData) {
        log.info("消息处理器：{}，开始处理消息：{}", this.getHandlerName(),JSON.toJSONString(messageData));
    }
}


```
- 如：[DemoEventListener.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fbiz%2Flistener%2FDemoEventListener.java)，编写监听器，并注入消息处理器
```java

@Slf4j
@Component
public class DemoEventListener extends AbstractListener {

    @Autowired
    private DemoMessageHandler demoMessageHandler;

    /**
     * 监听RabbitMQ消息
     * @param message 消息对象
     * @param channel MQ通道
     * @param event 时间对象
     */
    @RabbitListener(queues = MQIConstant.TASK_SUBMITTED_QUEUE, concurrency = "1")
    public void onRabbitMQEvent(Message message, Channel channel, DemoEvent event) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("DemoEventListener:{}",event);
            //由于消息是异步的，切记一定要设置系统上下文，后续BizContextHolder将填充为admin信息
            SystemTaskerContextHolder.getInstance().mount();
            demoMessageHandler.process(event.getMessageData());
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            super.basicReject(channel,deliveryTag);
        }
    }

    /**
     * 监听Spring事件消息
     * @param event 事件对象
     */
    @Async
    @EventListener
    public void onSpringEvent(DemoEvent event) {
        log.info("[Demo-Listener]接收到事件：{}", JSON.toJSONString(event));
        demoMessageHandler.process(event.getMessageData());
    }
}
```


## 2. 如何自动注册rabbitmq的关键配置信息
我们希望在项目启动的时候初始化所有的Exchange|Queue|Bind，而不是手动在management-ui上手动创建

有以下两种方式：
### 方式一：基于枚举类，手动注册Exchange|Queue|Bind
枚举类：[EventTypeEnums.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fcommonapi%2Fenums%2FEventTypeEnums.java)
```java
DEMO_EVENT("demo_event", "MES_PASS_STATION_TOPIC", "TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_ROUTING:TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_TOPIC_EXCHANGE"),
```
配置类：[RabbitMQConfig.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fcommonapi%2Fconfig%2FRabbitMQConfig.java)
```java

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
```

### 方式二：基于枚举类，更简洁的Exchange|Queue|Bind初始化方式

枚举类：[EventTypeEnums.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fcommonapi%2Fenums%2FEventTypeEnums.java)
```java
DEMO_EVENT("demo_event", "MES_PASS_STATION_TOPIC", "TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_ROUTING:TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_TOPIC_EXCHANGE"),
```
当项目逐渐庞大起来，Exchange|Queue|Bind会有很多，这个时候方式一就显得麻烦了。

方式二：后置处理器：[AutoLoadQueuePostprocessor.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fcommonapi%2Fconfig%2FAutoLoadQueuePostprocessor.java)
```java
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
            String qName = event.getQueue();
            String routingKey = event.getRoutingKey();
            TopicExchange exchange = getBean(beanFactory, exName, TopicExchange.class);
            if (Objects.isNull(exchange)) {
                exchange = new TopicExchange(exName);
                beanFactory.registerSingleton(exName, exchange);
                log.info("已注册消息交换机-{}", exName);
            }
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

```

## 3. 消息重复消费问题
当消费者成功处理完一条消息后，应该向 RabbitMQ 发送一个确认（ACK），告知它可以安全地移除这条消息。

高可用业务集群模式下，手动签收模式，如果消费者执行成功没有发送ACK，或者在处理完消息后才崩溃，那么这条消息可能会被重新排队并重新负载均衡至另一个消费者实例处理，这可能导致同服务副本处理多次相同的消息。

对于某些业务逻辑来说，重复处理可能是不可接受的，因为它会导致数据不一致或其他问题。

因此，无论处理成功或者异常，都请确保手动签收或者使用自动确认机制是重要的:
- 在 try 块中: channel.basicAck(deliveryTag, false);
- 在 catch 块中： channel.basicReject(deliveryTag, false);false表示拒绝消息的同时不再重新排队投递

以保证消息不会因为程序异常而丢失。

## 4. 消息重复发送问题

诸如Nginx和各种Mq中间件，都会存在一个问题，就是路由/投递的消息如果太久没有被确认，就会造成路由/消息重发，碰巧遇到业务逻辑校验失效的情况，就会导致业务逻辑重复被执行。如果是支付业务，后果则不堪设想

这往往是由于业务耗时过长导致的，但并不意味着业务执行失败

rabbitmq的解决方案如下，适当提高消息尝试重发的最大时间间隔

```yaml
  #-----------------------------------------------------------------
  ##                       RabbitMQ 通用配置                        --
  #-----------------------------------------------------------------
  rabbitmq:
    # 消息发布确认模式：none-不确认（默认） correlated-消息发送到交换机后回调（异步确认） simple-同步确认，结合waitForConfirms使用
    publisher-confirm-type: correlated
    publisher-returns: true
    listener:
      simple:
        acknowledge-mode: manual
        retry:
          #60秒后重试
          initial-interval: 60000
          #启用发布重试
          enabled: true
          #传递消息的最大尝试次数
          max-attempts: 3
          #尝试的最大时间间隔
          max-interval: 60000
          #应用于先前传递重试时间间隔的乘数
          multiplier: 1.0
```

# [custom-rabbit-framework-v2](custom-rabbit-framework-v2)

增加广播模式。

v1版本的topic模式通过枚举配置，以及spring工厂自动配置，大大提升了扩展性，灵活性

但是v1版本的toptc模式顶多是向交换机发送多种key，然后由正则模糊路由匹配到同一个队列接收，想象一下这种属于多对1接收模式

但是，在后期分布式业务拆分中，同生产者的消费者很有可能位于不同的微服务。

因此v2版本要做的就是增加一种模式，即向交换机发送一种key，可以被多个队列接收，所谓广播模式fanout支持:

- 绑定在同exchange上的所有队列无差别接收来自同生产者的消息
- 额外实现可配置化的单对多消息发送

至此，多对一模式接收，一对多模式接收，共同组成了Rabbitmq的业务生态