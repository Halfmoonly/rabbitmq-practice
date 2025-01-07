package org.lyflexi.customrabbitframeworkv1.commonapi.service;

import org.lyflexi.customrabbitframeworkv1.commonapi.po.SysConsumerMsgLogPo;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:49
 */
public interface IMessageLogService {

    /**
     * 查询消息表数据
     * @param messageId 数据ID
     * @param version 数据版本
     * @return ConsumerMsgLogPo
     */
    SysConsumerMsgLogPo getOne (String messageId, Long version);

    /**
     * 保存消息表数据
     * @param logPo 消息表数据
     */
    void save (SysConsumerMsgLogPo logPo);

    /**
     * 更新消息状态
     * @param logPo 消息表数据
     */
    void updateById (SysConsumerMsgLogPo logPo);
}

