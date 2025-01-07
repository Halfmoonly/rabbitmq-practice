package org.lyflexi.customrabbitframeworkv1.commonapi.publisher;


import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customrabbitframeworkv1.commonapi.event.IEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Slf4j
@Component("springEventPublisher")
//@ConditionalOnMissingBean(RabbitEventPublisher.class)
public class SpringEventPublisher implements IEventPublisher {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public boolean publish(IEvent event) {
        log.info("P0-准备发布事件：{} {}", event.getEventType(), event.getMessageData().getSeqNo());
        this.publisher.publishEvent(event);
        log.info("P1-事件发布成功：{} {}", event.getEventType(), event.getMessageData().getSeqNo());
        return true;
    }
}
