package org.lyflexi.boot_rabbit_common.constant;

/**
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/4 17:42
 */
public class DeadLetterConstant {
    public static final String EXCHANGE_NORMAL = "exchange.normal.video";
    public static final String EXCHANGE_DEAD_LETTER = "exchange.dead.letter.video";

    public static final String ROUTING_KEY_NORMAL = "routing.key.normal.video";
    public static final String ROUTING_KEY_DEAD_LETTER = "routing.key.dead.letter.video.#";

    public static final String QUEUE_NORMAL = "queue.normal.video";
    public static final String QUEUE_DEAD_LETTER = "queue.dead.letter.video";
}
