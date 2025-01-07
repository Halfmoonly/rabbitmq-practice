package org.lyflexi.customrabbitframeworkv1.commonapi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lyflexi.customrabbitframeworkv1.commonapi.holder.LoginUserVo;
import org.lyflexi.customrabbitframeworkv1.commonapi.holder.BizContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: ly
 * @Date: 2024/6/11 21:40
 */
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("preHandle: 请求处理之前，进行日志记录");
        //解析出登录用户信息，并设置上下文
        BizContextHolder contextHolder = BizContextHolder.getInstance();
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setUserName("mockUser");
        loginUserVo.setUserCode("mockUser");
        loginUserVo.setFactoryCode("mockFactory");
        contextHolder.setContext(loginUserVo);
        return true; // 允许请求继续
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle: 请求处理之后，视图渲染之前，可以在这里修改响应数据");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        System.out.println("afterCompletion: 整个请求完成，可以进行资源清理");
        BizContextHolder.getInstance().clear();
    }
}