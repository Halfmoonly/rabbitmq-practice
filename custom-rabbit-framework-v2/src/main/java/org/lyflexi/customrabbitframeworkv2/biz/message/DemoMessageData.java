package org.lyflexi.customrabbitframeworkv2.biz.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lyflexi.customrabbitframeworkv2.commonapi.message.BaseMessageData;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:47
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class DemoMessageData extends BaseMessageData {

    /**
     * 业务ID
     */
    private String id;

    /**
     * 业务名称
     */
    private String name;

    @Override
    public String getMessageId() {
        return id;
    }
}

