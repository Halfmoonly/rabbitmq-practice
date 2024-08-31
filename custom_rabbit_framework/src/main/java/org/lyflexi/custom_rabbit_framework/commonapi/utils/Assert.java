package org.lyflexi.custom_rabbit_framework.commonapi.utils;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.lyflexi.custom_rabbit_framework.commonapi.exception.LyFlexiBusinessException;

import java.util.Objects;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:27
 */

public class Assert {

    public static void isTrue (Boolean o, String message) {
        if (!o) throw LyFlexiBusinessException.exception(message);
    }

    public static void notTrue (Boolean o, String message) {
        if (o) throw LyFlexiBusinessException.exception(message);
    }

    public static void isNull (Object o, String message) {
        if (Objects.nonNull(o)) throw LyFlexiBusinessException.exception(message);
    }

    public static void notNull (Object o, String message) {
        if (Objects.isNull(o)) throw LyFlexiBusinessException.exception(message);
    }

    public static void isEmpty (String o, String message) {
        if (StringUtils.isNotEmpty(o)) throw LyFlexiBusinessException.exception(message);
    }

    public static void notEmpty (String o, String message) {
        if (StringUtils.isEmpty(o)) throw LyFlexiBusinessException.exception(message);
    }

    public static void isBlack (String o, String message) {
        if (StringUtils.isNotBlank(o)) throw LyFlexiBusinessException.exception(message);
    }

    public static void notBlack (String o, String message) {
        if (StringUtils.isBlank(o)) throw LyFlexiBusinessException.exception(message);
    }

    public static void isEmpty (Iterable<?> o, String message) {
        if (CollectionUtil.isNotEmpty(o)) throw LyFlexiBusinessException.exception(message);
    }

    public static void notEmpty (Iterable<?> o, String message) {
        if (CollectionUtil.isEmpty(o)) throw LyFlexiBusinessException.exception(message);
    }
}

