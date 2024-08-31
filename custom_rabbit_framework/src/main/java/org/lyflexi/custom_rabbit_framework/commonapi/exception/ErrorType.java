package org.lyflexi.custom_rabbit_framework.commonapi.exception;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:34
 */
public interface ErrorType {
    /**
     * 返回code
     *
     * @return
     */
    String getCode();

    /**
     * 返回mesg
     *
     * @return
     */
    String getMesg();
}
