package org.lyflexi.customrabbitframeworkv1.commonapi.holder;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/15 13:26
 */
@Data
@Accessors(chain = true)
public class LoginUserVo implements Serializable {

    private String id;

    private String userName;

    private String userCode;

    private String factoryCode;

    private String token;

    private String datasourceUrl;

    private String datasourceUsr;

    private String datasourcePwd;

    private String tenantId;

    private String editUserName;

    private String editUserCode;

    public static LoginUserVo buildAdminUser (String factoryCode, String tenantId) {
        LoginUserVo adminUser = new LoginUserVo();
        adminUser.setUserName("admin");
        adminUser.setUserCode("admin");
        adminUser.setEditUserName("admin");
        adminUser.setEditUserCode("admin");
        adminUser.setFactoryCode(factoryCode);
        adminUser.setTenantId(tenantId);
        return adminUser;
    }


}
