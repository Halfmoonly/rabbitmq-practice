package org.lyflexi.customrabbitframeworkv2.commonapi.enums;

/**
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 12:50
 */

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;

import java.util.Arrays;
import java.util.List;

import static org.lyflexi.customrabbitframeworkv2.commonapi.constant.MQIConstant.*;

/**
 * 消息类型枚举
 */
@Getter
public enum EventTypeEnums {


    // TOPIC测试
    DEMO_EVENT_TOPIC(DEMO_EVENT_TOPIC_ENUM_NAME, ExchangeTypes.TOPIC, DEMO_EVENT_TOPIC_QUEUES, DEMO_EVENT_TOPIC_ROUTE, DEMO_EVENT_TOPIC_EX),
    // FANOUT测试
    DEMO_EVENT_FANOUT(DEMO_EVENT_FANOUT_ENUM_NAME, ExchangeTypes.FANOUT, DEMO_EVENT_FANOUT_QUEUES, DEMO_EVENT_FANOUT_ROUTE, DEMO_EVENT_FANOUT_EX),

    ;

    /**
     * event名称
     */
    private final String event;
    /**
     * 模式名称
     */
    private final String type;

    /**
     * queues集合，为了支持广播模式：同交换机绑定的所有队列无差别接收
     */
    private final List<String> queues;

    /**
     * router名称
     */
    private final String routingKey;

    /**
     * exchange名称
     */
    private final String exchange;

    EventTypeEnums(String event, String type, List<String> queues, String routingKey, String exchange) {
        this.event = event;
        this.type = type;
        this.queues = queues;
        this.routingKey = routingKey;
        this.exchange = exchange;
    }

    public static EventTypeEnums getEventType(String event) {
        return Arrays.stream(EventTypeEnums.values()).filter(e -> StringUtils.equals(e.event, event)).findFirst().orElse(null);
    }
}
