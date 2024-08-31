package org.lyflexi.boot_rabbit_common.constant;

/**
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/4 17:42
 */
public class TxMqConstant {
    //交换机名称
    public static final String EXCHANGE = "tx.exchange";
    //队列名称
    public static final String QUEUE = "tx_queue";

    public static final String ROUTING_KEY = "tx.#";
}
