package org.lyflexi.customrabbitframeworkv1.commonapi.utils;

import org.lyflexi.customrabbitframeworkv1.commonapi.constant.MQIConstant;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 15:55
 */

public class KeyUtil {
    private static final String KEY_CONCAT_CHAR = ":";
    private static final String MQ_QUEUE_CONCAT_CHAR = "_";

//    public static String redisKey(String... keys){
//        return RedisKey.generator(ProjectModule.LES_DIS_SERVICE, keys);
//    }

    public static String generatorRoutingKey(String queueName){
        return MQIConstant.LES_DEFAULT_ROUTING_KEY + KEY_CONCAT_CHAR + queueName;
    }

//    public static String generateFactoryQueueKey(QueueEnum queueEnum, String factory) {
//        return queueEnum.getName() + MQ_QUEUE_CONCAT_CHAR + factory;
//    }
}
