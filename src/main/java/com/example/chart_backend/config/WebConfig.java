package com.example.chart_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${avatar.upload-dir}")
    private String avatarUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(avatarUploadDir).toAbsolutePath().normalize();
        String filePath = "file:" + uploadPath.toString() + "/";

        // FE sẽ gọi: http://localhost:8080/avatars/xxx.png
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations(filePath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/avatars/**")
                .allowedOrigins("*")
                .allowedMethods("GET");
    }
}
