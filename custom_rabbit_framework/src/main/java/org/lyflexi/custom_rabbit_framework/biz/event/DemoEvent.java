package org.lyflexi.custom_rabbit_framework.biz.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lyflexi.custom_rabbit_framework.commonapi.enums.DeliverStatusEnum;
import org.lyflexi.custom_rabbit_framework.commonapi.enums.EventTypeEnums;
import org.lyflexi.custom_rabbit_framework.commonapi.event.AbstractEvent;
import org.lyflexi.custom_rabbit_framework.biz.message.DemoMessageData;
import org.lyflexi.custom_rabbit_framework.commonapi.message.IMessageData;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 15:47
 */
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