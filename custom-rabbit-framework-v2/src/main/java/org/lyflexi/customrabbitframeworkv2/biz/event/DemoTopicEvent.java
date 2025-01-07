package org.lyflexi.customrabbitframeworkv2.biz.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lyflexi.customrabbitframeworkv2.commonapi.enums.DeliverStatusEnum;
import org.lyflexi.customrabbitframeworkv2.commonapi.enums.EventTypeEnums;
import org.lyflexi.customrabbitframeworkv2.commonapi.event.AbstractEvent;
import org.lyflexi.customrabbitframeworkv2.biz.message.DemoMessageData;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 15:47
 */
@Getter
@NoArgsConstructor
public class DemoTopicEvent extends AbstractEvent<DemoMessageData> {
    /**
     * 初始状态
     */
    private DeliverStatusEnum sourceStatus;

    /**
     * 目标状态
     */
    private DeliverStatusEnum targetStatus;


    public DemoTopicEvent(String eventType) {
        super(eventType);
    }

    public static DemoTopicEvent of (DemoMessageData message) {
        DemoTopicEvent event = new DemoTopicEvent(EventTypeEnums.DEMO_EVENT_TOPIC.getEvent());
        event.setMessageData(message);
        return event;
    }

    public static DemoTopicEvent of (DemoMessageData message , DeliverStatusEnum sourceStatus, DeliverStatusEnum targetStatus) {
        DemoTopicEvent event = new DemoTopicEvent(EventTypeEnums.DEMO_EVENT_TOPIC.getEvent());
        event.setMessageData(message);
        event.sourceStatus = sourceStatus;
        event.targetStatus = targetStatus;
        return event;
    }


    public DemoMessageData getMessageData() {
        return super.getMessageData();
    }
}