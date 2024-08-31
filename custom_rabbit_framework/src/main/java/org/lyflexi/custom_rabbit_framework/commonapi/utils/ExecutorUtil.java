package org.lyflexi.custom_rabbit_framework.commonapi.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:58
 */
public class ExecutorUtil {

    public static final ThreadPoolExecutor pool = new ThreadPoolExecutor(8,16,5, TimeUnit.MINUTES, new LinkedBlockingDeque<>(512));
}
