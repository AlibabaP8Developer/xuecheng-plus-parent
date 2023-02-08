package com.xuecheng.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Mr.M
 * @version 1.0
 * @description 跨域过虑器
 * @date 2022/9/7 11:04
 */
@Configuration
public class GlobalCorsConfig {

    /**
     * 允许跨域调用的过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许白名单域名进行跨域调用
        // 添加允许哪个请求进行跨域，*表示所有
        configuration.addAllowedOrigin("*");
        // 允许跨越发送cookie
        configuration.setAllowCredentials(true);
        // 放行全部原始头信息
        configuration.addAllowedHeader("*");
        // 允许所有请求方法跨域调用
        // 添加哪些http方法可以跨域，get、post，（多个方法中间以逗号分隔），*号表示所有
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}