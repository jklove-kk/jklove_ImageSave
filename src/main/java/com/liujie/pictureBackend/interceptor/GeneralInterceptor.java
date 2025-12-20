package com.liujie.pictureBackend.interceptor;


import com.liujie.pictureBackend.model.vo.LoginUserVO;
import com.liujie.pictureBackend.redis.RedisService;
import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器 - 通用拦截器
 */
@Slf4j
public class GeneralInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param handler  处理器对象
     * @return  true表示继续流程，false表示中断流程
     * @throws Exception 可能抛出的异常
     */

    @Resource
    private RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("方法已拦截前置执行。。。");
        //执行登录校验
        //1.用请求头中存储的cookie获取token从缓存中获取数据
        LoginUserVO tokenInfo = redisService.getTokenInfoFromCookie();

        if(tokenInfo==null)
        {
            //登录认证失效
            return false;
        }else {
            //刷新登录时间

        }

        return true; // 返回true表示放行请求
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     *
     * @param request      HTTP请求对象
     * @param response     HTTP响应对象
     * @param handler      处理器对象
     * @param modelAndView 模型和视图对象，可以为null
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 可在此方法中修改ModelAndView对象
        // 例如：添加公共模型数据
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet渲染了对应的视图之后执行
     * （主要用于进行资源清理工作）
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param handler  处理器对象
     * @param ex       处理过程中抛出的异常，如果没有异常则为null
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        // 可在此方法中进行资源清理，如关闭数据库连接等
        // 如果有异常，可以进行异常处理或记录日志
    }
}