package com.myshop.ecommerce;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all API endpoints
                .allowedOrigins("*") // Allow all frontend ports
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // Allow these actions
    }
}