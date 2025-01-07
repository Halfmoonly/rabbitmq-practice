package org.lyflexi.customrabbitframeworkv2.biz.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lyflexi.customrabbitframeworkv2.biz.message.DemoMessageData;
import org.lyflexi.customrabbitframeworkv2.commonapi.enums.DeliverStatusEnum;
import org.lyflexi.customrabbitframeworkv2.commonapi.enums.EventTypeEnums;
import org.lyflexi.customrabbitframeworkv2.commonapi.event.AbstractEvent;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 15:47
 */
@Getter
@NoArgsConstructor
public class DemoFanoutEvent extends AbstractEvent<DemoMessageData> {
    /**
     * 初始状态
     */
    private DeliverStatusEnum sourceStatus;

    /**
     * 目标状态
     */
    private DeliverStatusEnum targetStatus;


    public DemoFanoutEvent(String eventType) {
        super(eventType);
    }

    public static DemoFanoutEvent of (DemoMessageData message) {
        DemoFanoutEvent event = new DemoFanoutEvent(EventTypeEnums.DEMO_EVENT_FANOUT.getEvent());
        event.setMessageData(message);
        return event;
    }

    public static DemoFanoutEvent of (DemoMessageData message , DeliverStatusEnum sourceStatus, DeliverStatusEnum targetStatus) {
        DemoFanoutEvent event = new DemoFanoutEvent(EventTypeEnums.DEMO_EVENT_FANOUT.getEvent());
        event.setMessageData(message);
        event.sourceStatus = sourceStatus;
        event.targetStatus = targetStatus;
        return event;
    }


    public DemoMessageData getMessageData() {
        return super.getMessageData();
    }
}