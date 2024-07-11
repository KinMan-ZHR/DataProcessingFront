package com.example.dataplat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 添加映射路径
                .allowedOrigins("http://localhost:5173")// 允许跨域的域名，可以用*表示允许任何域名使用
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")// 允许跨域的请求方式
                .allowedHeaders("*")// 允许跨域的请求头
                .allowCredentials(true)
                .maxAge(3600);
    }
}