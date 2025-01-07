package org.lyflexi.customrabbitframeworkv1.commonapi.message;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:24
 */

public interface IMessageData {

    /**
     * 全局流水号
     */
    String getSeqNo ();

    /**
     * 消息版本号
     */
    Long getVersion ();

    /**
     * 获取工厂编码
     */
    String getFactoryCode ();

    /**
     * 消息业务唯一ID
     */
    String getMessageId ();

    /**
     * 消息对象关键参数检查
     */
    void assertParams ();
}