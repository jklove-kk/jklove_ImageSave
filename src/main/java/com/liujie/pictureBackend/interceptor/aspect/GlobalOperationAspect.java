package com.liujie.pictureBackend.interceptor.aspect;


import cn.hutool.core.util.StrUtil;
import com.liujie.pictureBackend.constans.UserConstant;
import com.liujie.pictureBackend.exception.BusinessException;
import com.liujie.pictureBackend.interceptor.annotation.GlobalInterceptor;
import com.liujie.pictureBackend.model.vo.LoginUserVO;
import com.liujie.pictureBackend.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;



//切面
@Component("operationAspect")
@Aspect
@Slf4j
public class GlobalOperationAspect {

    @Resource
    private RedisUtils redisUtils;

    @Before("@annotation(com.liujie.pictureBackend.interceptor.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint point) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
        if (null == interceptor) {
            return;
        }
        /**
         * 校验登录
         */
        if (interceptor.checkLogin()) {
            checkLogin();
        }
    }

    //校验登录
    private void checkLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(UserConstant.TOKEN_WEB_COOKIE);
        if (StrUtil.isEmpty(token)) {
            throw new BusinessException(503,"登录校验未获取到token");
        }
        LoginUserVO tokenUserInfoDto = (LoginUserVO) redisUtils.get(UserConstant.TOKEN_REDIS_KEY + token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(400,"未获取到登录信息");
        }
    }
}