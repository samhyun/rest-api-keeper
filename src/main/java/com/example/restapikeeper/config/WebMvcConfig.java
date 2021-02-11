package com.example.restapikeeper.config;

import com.example.restapikeeper.RestApiKeeper;
import com.example.restapikeeper.interceptor.RestApiKeeperInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RestApiKeeperInterceptor(new RestApiKeeper(60 * 1000, 5)));
    }
}
