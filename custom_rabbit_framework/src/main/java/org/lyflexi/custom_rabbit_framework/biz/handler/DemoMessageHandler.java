package org.lyflexi.custom_rabbit_framework.biz.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.custom_rabbit_framework.biz.message.DemoMessageData;
import org.lyflexi.custom_rabbit_framework.commonapi.handler.AbstractHandler;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 15:45
 */
@Slf4j
@Service
public class DemoMessageHandler extends AbstractHandler<DemoMessageData> {

    @Override
    public String getHandlerName() {
        return "DemoMessageHandler";
    }

    @Override
    public void doHandle(DemoMessageData messageData) {
        log.info("消息处理器：{}，开始处理消息：{}", this.getHandlerName(),JSON.toJSONString(messageData));
    }
}

