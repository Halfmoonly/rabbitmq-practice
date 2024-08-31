package org.lyflexi.custom_rabbit_framework.commonapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.lyflexi.custom_rabbit_framework.commonapi.po.SysConsumerMsgLogPo;
import org.lyflexi.custom_rabbit_framework.commonapi.service.IMessageLogService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 14:05
 */
@Service
@Slf4j
public class MessageLogServiceImpl implements IMessageLogService {
    @Override
    public SysConsumerMsgLogPo getOne(String messageId, Long version) {
        return null;
    }

    @Override
    public void save(SysConsumerMsgLogPo logPo) {
        log.info("消息日志保存成功：{}",logPo);
    }

    @Override
    public void updateById(SysConsumerMsgLogPo logPo) {

    }
}
