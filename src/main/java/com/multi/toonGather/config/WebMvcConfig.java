package com.multi.toonGather.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 인터셉터를 설정에 추가하는 클래스
 *
 * @author : seoyun
 * @fileName : WebMvcConfig
 * @since : 2024-08-06
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CurrentUriInterceptor());
    }
}