package org.lyflexi.custom_rabbit_framework;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.lyflexi.custom_rabbit_framework.biz.event.DemoEvent;
import org.lyflexi.custom_rabbit_framework.commonapi.message.DemoMessageData;
import org.lyflexi.custom_rabbit_framework.commonapi.publisher.IEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootTest
@Slf4j
class CustomRabbitFrameworkApplicationTests {
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
