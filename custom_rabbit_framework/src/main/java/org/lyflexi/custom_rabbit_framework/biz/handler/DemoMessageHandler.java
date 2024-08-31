package org.lyflexi.custom_rabbit_framework.biz.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.custom_rabbit_framework.commonapi.message.DemoMessageData;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 15:45
 */
@Slf4j
@Service
public class DemoMessageHandler {
    public void handleMessage (DemoMessageData message) {
        log.info("开始处理asyncRabbitEventPublisher消息：{}", JSON.toJSONString(message));
    }
}

