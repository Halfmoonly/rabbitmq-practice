package org.lyflexi.custom_rabbit_framework.commonapi.enums;

import lombok.Getter;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 14:51
 */
public enum DeliverStatusEnum {
    SEND(0),
    SUCCESS(1),
    FAIL(2),
    USED(3),
    STOCK(4);

    @Getter
    private int code;

    DeliverStatusEnum(int code){
        this.code = code;
    }
}
