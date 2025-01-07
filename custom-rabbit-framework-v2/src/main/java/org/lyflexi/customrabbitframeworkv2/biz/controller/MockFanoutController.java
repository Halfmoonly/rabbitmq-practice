package org.lyflexi.customrabbitframeworkv2.biz.controller;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customrabbitframeworkv2.biz.event.DemoFanoutEvent;
import org.lyflexi.customrabbitframeworkv2.biz.event.DemoTopicEvent;
import org.lyflexi.customrabbitframeworkv2.biz.message.DemoMessageData;
import org.lyflexi.customrabbitframeworkv2.commonapi.publisher.IEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 18:55
 */


@RestController
@RequestMapping("/fanout")
@Slf4j
public class MockFanoutController {
    /**
     * rabbitmq异步发送测试Fanout
     */
    @Autowired
    @Qualifier("asyncRabbitEventPublisher")
    private IEventPublisher asyncRabbitEventPublisher;
    @GetMapping(value = "/asyncRabbitEventPublisherFanout")
    public void asyncRabbitEventPublisher (@RequestParam String name) {
        log.info("testDemoRabbitEventPublisher测试开始");
        DemoMessageData message = new DemoMessageData();
        message.setId(RandomUtil.randomNumbers(10));
        message.setName("testDemoRabbitEventPublisher");
        message.setSeqNo(RandomUtil.randomNumbers(10));
        message.setVersion(RandomUtil.randomLong());
        message.setFactoryCode("SZ54");
        asyncRabbitEventPublisher.publish(DemoFanoutEvent.of(message));
        log.info("testDemoRabbitEventPublisher测试结束");
    }

}
