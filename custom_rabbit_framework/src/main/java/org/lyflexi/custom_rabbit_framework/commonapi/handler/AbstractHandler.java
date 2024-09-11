package org.lyflexi.custom_rabbit_framework.commonapi.handler;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.custom_rabbit_framework.commonapi.message.BaseMessageData;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 15:45
 */
@Slf4j
public abstract class AbstractHandler<M extends BaseMessageData> implements IHandler<M> {

    @Override
    public void process (M messageData) {
        try {
            doHandle(messageData);
        } catch (Exception e) {
            log.error("[DemandingExchangedEvent:{}-Handler]事件处理失败，messageId:{}", getHandlerName(), messageData.getMessageId(), e);
        }
    }

    public abstract String getHandlerName ();

    public abstract void doHandle (M messageData);
}