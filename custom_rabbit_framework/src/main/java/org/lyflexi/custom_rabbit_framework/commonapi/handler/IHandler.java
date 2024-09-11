package org.lyflexi.custom_rabbit_framework.commonapi.handler;

import org.lyflexi.custom_rabbit_framework.commonapi.message.BaseMessageData;

/**
 * @Description:
 * @Author: lyflexi
 * @project: rabbitmq-practice
 * @Date: 2024/9/11 14:12
 */
public interface IHandler <M extends BaseMessageData>{
    public void process (M messageData);
}
