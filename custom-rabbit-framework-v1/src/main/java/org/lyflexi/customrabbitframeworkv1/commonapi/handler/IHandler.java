package org.lyflexi.customrabbitframeworkv1.commonapi.handler;

import org.lyflexi.customrabbitframeworkv1.commonapi.message.BaseMessageData;

/**
 * @Description:
 * @Author: lyflexi
 * @project: rabbitmq-practice
 * @Date: 2024/9/11 14:12
 */
public interface IHandler <M extends BaseMessageData>{
    public void process (M messageData);
}
