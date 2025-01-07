package org.lyflexi.customrabbitframeworkv2.commonapi.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 14:49
 */

public class MQIConstant {


    public static final String COUNTER_KEY = "counter";


    /**
     * TOPIC模式测试用例
     * */
    public static final String DEMO_EVENT_TOPIC_QUEUE = "DEMO_EVENT_TOPIC_QUEUE";
    public static final List<String> DEMO_EVENT_TOPIC_QUEUES = Lists.newArrayList(DEMO_EVENT_TOPIC_QUEUE);
    public static final String DEMO_EVENT_TOPIC_ROUTE = "DEMO_EVENT_TOPIC_ROUTE";
    public static final String DEMO_EVENT_TOPIC_EX = "DEMO_EVENT_TOPIC_EX";
    public static final String DEMO_EVENT_TOPIC_ENUM_NAME = "DEMO_EVENT_TOPIC_ENUM_NAME";


    /**
     * FANOUT广播模式测试用例
     * */
    public static final String DEMO_EVENT_FANOUT_QUEUE1 = "DEMO_EVENT_FANOUT_QUEUE1";
    public static final String DEMO_EVENT_FANOUT_QUEUE2 = "DEMO_EVENT_FANOUT_QUEUE2";
    public static final List<String> DEMO_EVENT_FANOUT_QUEUES = Lists.newArrayList(DEMO_EVENT_FANOUT_QUEUE1,DEMO_EVENT_FANOUT_QUEUE2);
    public static final String DEMO_EVENT_FANOUT_ROUTE = "DEMO_EVENT_FANOUT_ROUTE";
    public static final String DEMO_EVENT_FANOUT_EX = "DEMO_EVENT_FANOUT_EX";
    public static final String DEMO_EVENT_FANOUT_ENUM_NAME = "DEMO_EVENT_FANOUT_ENUM_NAME";









}

