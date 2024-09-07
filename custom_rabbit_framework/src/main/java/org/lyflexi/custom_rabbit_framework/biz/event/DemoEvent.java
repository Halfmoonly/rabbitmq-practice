package org.lyflexi.custom_rabbit_framework.biz.event;

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