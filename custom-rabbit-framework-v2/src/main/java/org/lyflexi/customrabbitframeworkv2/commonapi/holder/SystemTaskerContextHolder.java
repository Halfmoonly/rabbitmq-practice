package org.lyflexi.customrabbitframeworkv2.commonapi.holder;

import java.util.Objects;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/15 13:27
 */
public class SystemTaskerContextHolder {

    private final ThreadLocal<Boolean> threadLocal;


    private SystemTaskerContextHolder() {
        this.threadLocal = new ThreadLocal<>();
    }

    /**
     * 创建实例
     *
     * @return
     */
    public static SystemTaskerContextHolder getInstance() {
        return SingletonHolder.S_INSTANCE;
    }

    /**
     * 静态内部类单例模式
     * 单例初使化
     */
    private static class SingletonHolder {
        private static final SystemTaskerContextHolder S_INSTANCE = new SystemTaskerContextHolder();
    }

    /**
     * 挂载上下文
     *
     */
    public void mount() {
        threadLocal.set(true);
    }

    /**
     * 是否后台任务
     *
     * @return
     */
    public Boolean isSystemTasker() {
        Boolean result = threadLocal.get();
        return !Objects.isNull(result) && result;
    }

    /**
     * 清空上下文
     */
    public void unmount() {
        threadLocal.remove();
    }
}
