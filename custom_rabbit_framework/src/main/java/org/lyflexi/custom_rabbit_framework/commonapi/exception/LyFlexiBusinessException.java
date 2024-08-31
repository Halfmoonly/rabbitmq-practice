package org.lyflexi.custom_rabbit_framework.commonapi.exception;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:31
 */
public class LyFlexiBusinessException extends BaseException {

    @Getter
    private Object[] args;

    @Getter
    private String customMsg;


    public LyFlexiBusinessException(String message) {
        super(LyflexiErrorType.INTERNET_SERVER_ERROR);
        this.customMsg = message;
    }

    public LyFlexiBusinessException(ErrorType errorType) {
        super(errorType);
    }

    public LyFlexiBusinessException(ErrorType errorType, String message) {
        super(errorType);
        this.customMsg = message;
    }

    public LyFlexiBusinessException(ErrorType errorType, Object[] args) {
        super(errorType);
        this.args = args;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if(StringUtils.isNotEmpty(customMsg)){
            message = customMsg;
        }
        return message;
    }

    public static LyFlexiBusinessException exception (String message) {
        return new LyFlexiBusinessException(message);
    }

    public static LyFlexiBusinessException exception (String pattern, Object ... args) {
        return new LyFlexiBusinessException(MessageFormat.format(pattern, args));
    }
}
