package com.liujie.pictureBackend.aop;


import com.liujie.pictureBackend.annotation.AuthCheck;
import com.liujie.pictureBackend.exception.BusinessException;
import com.liujie.pictureBackend.exception.ErrorCode;
import com.liujie.pictureBackend.model.enums.UserRoleEnum;
import com.liujie.pictureBackend.model.vo.LoginUserVO;
import com.liujie.pictureBackend.redis.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Aspect
@Component
/**
 * 权限拦截器
 */
public class AuthInterceptor {

    @Resource
    private RedisService redisService;

    /**
     * 执行拦截
     * @param joinPoint
     * @param authCheck
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {

        //1.获取注解权限
        UserRoleEnum mustRole = authCheck.mustRole();

        //2.获取请求者权限
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = servletRequestAttributes.getRequest();
        //获取登录信息
        LoginUserVO tokenInfoFromCookie = redisService.getTokenInfoFromCookie();
        //获取权限
        UserRoleEnum role = UserRoleEnum.getEnumByValue(tokenInfoFromCookie.getUserRole());
        //3.判断权限是否放行
        if(mustRole==null){
            //不需要权限放行
            return  joinPoint.proceed();
        }
        if(role==null||mustRole!=role)
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return joinPoint.proceed();
    }


}
