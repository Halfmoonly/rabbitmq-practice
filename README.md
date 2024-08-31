# rabbitmq-practice

我们希望在项目启动的时候初始化所有的Exchange|Queue|Bind，而不是手动在management-ui上手动创建

有以下两种方式：
# 基于枚举类，手动注册Exchange|Queue|Bind
枚举类：[EventTypeEnums.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fcommonapi%2Fenums%2FEventTypeEnums.java)
```java
DEMO_EVENT("demo_event", "MES_PASS_STATION_TOPIC", "TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_ROUTING:TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_TOPIC_EXCHANGE"),
```
方式一：配置类：[RabbitMQConfig.java](custom_rabbit_framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustom_rabbit_framework%2Fcommonapi%2Fconfig%2FRabbitMQConfig.java)
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

# 基于枚举类，更简洁的Exchange|Queue|Bind初始化方式

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