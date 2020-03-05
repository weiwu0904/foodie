package com.weiwu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// 设置项目 跨域配置
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        //1. 添加跨域配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 设置允许访问的URL和端口
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfiguration.addAllowedOrigin("http://127.0.0.1:8080");
        corsConfiguration.addAllowedOrigin("*");

        // 设置是否发送cookie信息
        corsConfiguration.setAllowCredentials(true);

        // 设置允许请求的方式
        corsConfiguration.addAllowedMethod("*");

        // 设置允许的header值
        corsConfiguration.addAllowedHeader("*");

        //2. 上面的配置 对访问哪些URL的生效
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**",corsConfiguration);

        //3.
        return new CorsFilter(corsSource);
    }
}
