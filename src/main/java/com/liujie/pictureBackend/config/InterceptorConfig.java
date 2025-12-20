package com.liujie.pictureBackend.config;



import com.liujie.pictureBackend.interceptor.GeneralInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 统一拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private GeneralInterceptor generalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器并配置拦截路径和排除路径
        registry.addInterceptor(generalInterceptor)
                // 拦截的路径
                .addPathPatterns("/admin/**")
                // 放行的路径
                .excludePathPatterns("/admin/login");
    }
}