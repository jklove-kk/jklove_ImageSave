package com.liujie.pictureBackend.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//全局权限校验
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {

    /**
     * 校验登录
     *
     * @return
     */
    boolean checkLogin() default false;
}
