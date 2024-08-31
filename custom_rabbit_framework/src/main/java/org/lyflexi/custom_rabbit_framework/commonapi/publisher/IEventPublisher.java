package org.lyflexi.custom_rabbit_framework.commonapi.publisher;

import org.lyflexi.custom_rabbit_framework.commonapi.event.IEvent;


public interface IEventPublisher {

    /**
     * 发布事件
     * @param event 事件对象
     * @return true/false
     */
    boolean publish (IEvent event);
}
