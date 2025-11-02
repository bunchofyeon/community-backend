package com.example.week5.config;

import com.example.week5.common.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMVCConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/users/login", "/users/register",
                        "/users/checkEmail", "/users/checkNickname",
                        // 게시글 작성, 수정은 비공개로 처리했음
                        "/css/**", "/js/**", "/images/**", "/favicon.ico"
                );
    }
}
