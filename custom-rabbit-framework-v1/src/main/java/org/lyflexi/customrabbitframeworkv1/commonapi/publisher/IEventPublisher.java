package org.lyflexi.customrabbitframeworkv1.commonapi.publisher;

import org.lyflexi.customrabbitframeworkv1.commonapi.event.IEvent;


public interface IEventPublisher {

    /**
     * 发布事件
     * @param event 事件对象
     * @return true/false
     */
    boolean publish (IEvent event);
}
