package org.lyflexi.custom_rabbit_framework.commonapi.po;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:50
 */

@Builder
@Data
public class SysConsumerMsgLogPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msgId;

    private String dataId;

    private Long dataVersion;

    private String msg;

    private String exchange;

    private String routingKey;

    private String queue;

    private Integer status;

    private Integer tryCount;

    private LocalDateTime addTime;

    private String addUserName;

    private String addUserCode;

    private String editUserName;

    private String editUserCode;

    private LocalDateTime editTime;

    private Integer dataStatus;

    private String factoryCode;
}
