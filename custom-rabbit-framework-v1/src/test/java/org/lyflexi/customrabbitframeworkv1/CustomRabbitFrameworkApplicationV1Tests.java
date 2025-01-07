package org.lyflexi.customrabbitframeworkv1;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.lyflexi.customrabbitframeworkv1.biz.event.DemoEvent;
import org.lyflexi.customrabbitframeworkv1.biz.message.DemoMessageData;
import org.lyflexi.customrabbitframeworkv1.commonapi.publisher.IEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CustomRabbitFrameworkApplicationV1Tests {
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
        springEventPublisher.publish(DemoEvent.of(message));
        log.info("testDemoSpringEventPublisher测试结束");
    }

}
