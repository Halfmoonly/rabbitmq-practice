package org.lyflexi.customrabbitframeworkv1.commonapi.delay;

import lombok.Data;
import org.lyflexi.customrabbitframeworkv1.commonapi.enums.EventTypeEnums;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 12:50
 */

@Data
public class DelayEntity<T> implements Delayed {

    private T data;

    private EventTypeEnums eventType;

    @Deprecated
    private String queue;

    private Long expire;

    public DelayEntity(T data, EventTypeEnums eventType, Long delay){
        this.data = data;
        this.eventType = eventType;
        this.expire = System.currentTimeMillis() + delay;
    }

    public DelayEntity(T data, String queue, Long delay){
        this.data = data;
        this.queue = queue;
        this.expire = System.currentTimeMillis() + delay;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return expire - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }
}