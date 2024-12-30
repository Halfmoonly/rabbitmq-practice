package org.lyflexi.custom_rabbit_framework.commonapi.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.lyflexi.custom_rabbit_framework.commonapi.message.IMessageData;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:53
 */

@NoArgsConstructor
@Data
public abstract class AbstractEvent<T extends IMessageData> implements IEvent {

    public AbstractEvent(String eventType) {
        this.eventType = eventType;
    }

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 消息体
     */
    private T messageData;
}