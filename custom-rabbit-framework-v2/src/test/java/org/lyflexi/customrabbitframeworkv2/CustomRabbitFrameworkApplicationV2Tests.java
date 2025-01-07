package org.lyflexi.customrabbitframeworkv2;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.lyflexi.customrabbitframeworkv2.biz.event.DemoTopicEvent;
import org.lyflexi.customrabbitframeworkv2.biz.message.DemoMessageData;
import org.lyflexi.customrabbitframeworkv2.commonapi.publisher.IEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CustomRabbitFrameworkApplicationV2Tests {
    @Autowired
    @Qualifier("springEventPublisher")
    private IEventPublisher springEventPublisher;

    @Test
    void contextLoads() {
    }

    @Test
    void testDemoSpringEventPublisher() {
        log.info("testDemoSpringEventPublisher测试开始");
        DemoMessageData message = new DemoMessageData();
        message.setId(RandomUtil.randomNumbers(10));
        message.setName("testDemoSpringEventPublisher");
        message.setSeqNo(RandomUtil.randomNumbers(10));
        message.setVersion(RandomUtil.randomLong());
        message.setFactoryCode("SZ54");
        springEventPublisher.publish(DemoTopicEvent.of(message));
        log.info("testDemoSpringEventPublisher测试结束");
    }

}
