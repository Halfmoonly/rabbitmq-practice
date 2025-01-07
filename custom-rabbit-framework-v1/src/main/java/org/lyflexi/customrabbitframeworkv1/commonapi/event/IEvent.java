package org.lyflexi.customrabbitframeworkv2.commonapi.event;

import org.lyflexi.customrabbitframeworkv2.commonapi.message.IMessageData;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:52
 */
public interface IEvent {

    /**
     * 获取时间类型，里面会包含topic、队列、路由、及交换机信息
     * 后续对接MQ扩展
     */
    String getEventType ();

    /**
     * 获取消息
     */
    IMessageData getMessageData();
}
