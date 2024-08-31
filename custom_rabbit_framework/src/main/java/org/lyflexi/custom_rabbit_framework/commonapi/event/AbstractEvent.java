package org.lyflexi.custom_rabbit_framework.commonapi.event;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.lyflexi.custom_rabbit_framework.commonapi.message.IMessageData;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:53
 */

@Data
public abstract class AbstractEvent implements IEvent {

    public AbstractEvent(String eventType) {
        this.eventType = eventType;
    }

    /**
     * 事件发送目标，多个用逗号”,“分割
     *  local 本地事件，事件将会在本地服务内传播
     *  les-core 远程事件，事件将会推送至远程les-core服务
     */
    private String target;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 消息体
     */
    private IMessageData messageData;

    public void ofTargetLocal () {
        if (StringUtils.isNotBlank(this.target)) {
            if (this.target.indexOf(EVENT_TARGET_LOCAL) == 0) {
                this.target = this.target + "," + EVENT_TARGET_LOCAL;
            }
        } else {
            this.target = EVENT_TARGET_LOCAL;
        }
    }

    public void ofTargetLesCore () {
        if (StringUtils.isNotBlank(this.target)) {
            if (this.target.indexOf(EVENT_TARGET_CORE) == 0) {
                this.target = this.target + "," + EVENT_TARGET_CORE;
            }
        } else {
            this.target = EVENT_TARGET_CORE;
        }
    }
}
