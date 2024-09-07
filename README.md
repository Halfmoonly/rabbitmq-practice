# rabbitmq-practice

# custom_rabbit_framework

如何发布事件：见MockController
- 发送异步rabbitmq消息 
- 同步发送rabbitmq消息
- 发布spring消息（仅用于相同服务内）

如何编写事件消费逻辑：
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
public class DemoEvent extends AbstractEvent {

    public DemoEvent(String eventType) {
        super(eventType);
    }

    public static DemoEvent of (IMessageData message) {
        DemoEvent event = new DemoEvent(EventTypeEnums.DEMO_EVENT.getEvent());
        event.setMessageData(message);
        return event;
    }

    public DemoMessageData getMessageData() {
        return (DemoMessageData) super.getMessageData();
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
public class DemoEventListener implements IListener {

    @Autowired
    private DemoMessageHandler demoMessageHandler;

    /**
     * 监听RabbitMQ消息
     * @param message 消息对象
     * @param deliveryTag MQ消息唯一标识
     * @param channel MQ通道
     */
    @RabbitListener(queues = MQIConstant.TASK_SUBMITTED_QUEUE, concurrency = "1")
    public void onRabbitMQEvent(DemoMessageData message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) {
        demoMessageHandler.process(message);
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