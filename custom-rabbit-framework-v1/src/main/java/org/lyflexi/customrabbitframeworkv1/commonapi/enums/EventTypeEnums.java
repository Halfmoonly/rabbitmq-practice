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

import static org.lyflexi.customrabbitframeworkv2.commonapi.constant.MQIConstant.DEMO_EVENT_TOPIC_QUEUE;

/**
 * 消息类型枚举
 */
@Getter
public enum EventTypeEnums {

    // 测试事件
    DEMO_EVENT("demo_event", "MES_PASS_STATION_TOPIC", "TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_ROUTING:TASK_SUBMITTED_QUEUE2", "LES_DEFAULT_TOPIC_EXCHANGE"),
    ;

    /**
     * event名称
     */
    private final String event;

    /**
     * topic名称
     */
    private final String topic;

    /**
     * queue名称
     */
    private final String queue;

    /**
     * router名称
     */
    private final String routingKey;

    /**
     * exchange名称
     */
    private final String exchange;

    EventTypeEnums(String event, String topic, String queue, String routingKey, String exchange) {
        this.event = event;
        this.topic = topic;
        this.queue = queue;
        this.routingKey = routingKey;
        this.exchange = exchange;
    }

    public static EventTypeEnums getEventType (String event) {
        return Arrays.stream(EventTypeEnums.values()).filter(e -> StringUtils.equals(e.event, event)).findFirst().orElse(null);
    }
}
