package org.lyflexi.customrabbitframeworkv2.commonapi.holder;



import java.util.Objects;
import java.util.Optional;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/15 13:24
 */
public class BizContextHolder {

    private final ThreadLocal<LoginUserVo> threadLocal;


    /**
     * 适用于子线程上下文
     */
    private BizContextHolder() {
        this.threadLocal = new InheritableThreadLocal<>();
    }

    /**
     * 创建实例
     *
     * @return
     */
    public static BizContextHolder getInstance() {
        return SingletonHolder.S_INSTANCE;
    }

    /**
     * 静态内部类单例模式
     * 单例初使化
     */
    private static class SingletonHolder {
        private static final BizContextHolder S_INSTANCE = new BizContextHolder();
    }

    /**
     * 用户上下文中放入信息
     *
     * @param loginUserVo
     */
    public void setContext(LoginUserVo loginUserVo) {
        threadLocal.set(loginUserVo);
    }

    /**
     * 获取上下文中的信息
     *
     * @return
     */
    public LoginUserVo getContext() {
        LoginUserVo loginUserVo = threadLocal.get();
        boolean systemTask = SystemTaskerContextHolder.getInstance().isSystemTasker();
        if(systemTask && Objects.isNull(loginUserVo)){
            loginUserVo = new LoginUserVo();
            loginUserVo.setId(LyFlexiConstant.DEFAULT_USER_ID);
            loginUserVo.setUserName(LyFlexiConstant.DEFAULT_USER_NAME);
            loginUserVo.setUserCode(LyFlexiConstant.DEFAULT_USER_CODE);
            loginUserVo.setEditUserName(LyFlexiConstant.DEFAULT_USER_NAME);
            loginUserVo.setEditUserCode(LyFlexiConstant.DEFAULT_USER_CODE);
            BizContextHolder.getInstance().setContext(loginUserVo);
        }
        return loginUserVo;
    }

    /**
     * 获取上下文中的用户名
     */
    public String getUsername() {
        LoginUserVo loginUserVo = getContext();
        return Optional.ofNullable(loginUserVo).orElse(new LoginUserVo()).getUserName();
    }

    /**
     * 获取上下文中的用户ID
     */
    public String getUserID() {
        LoginUserVo loginUserVo = getContext();
        return Optional.ofNullable(loginUserVo).orElse(new LoginUserVo()).getId();
    }

    /**
     * 获取上下文中的租户ID
     */
    public String getTenantId() {
        LoginUserVo loginUserVo = getContext();
//        return Optional.ofNullable(loginUserVo).orElse(new LoginUserVo()).getTenantId();
        return Optional.ofNullable(loginUserVo).orElse(new LoginUserVo().setTenantId("123")).getTenantId();
    }

    /**
     * 获取上下文中的工厂编码
     */
    public String getFactoryCode() {
        LoginUserVo loginUserVo = getContext();
        return Optional.ofNullable(loginUserVo).orElse(new LoginUserVo()).getFactoryCode();
    }

    /**
     * 清空上下文
     */
    public void clear() {
        threadLocal.remove();
    }

}
