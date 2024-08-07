package com.multi.toonGather.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 모든 요청에 대해 currentUri를 자동으로 설정하는 인터셉터
 *
 * @author : seoyun
 * @fileName : CurrentUriInterceptor
 * @since : 2024-08-06
 */
public class CurrentUriInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("currentUri", request.getRequestURI());
        return true;
    }
}
