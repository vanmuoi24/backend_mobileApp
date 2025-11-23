package com.example.chart_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${avatar.upload-dir:uploads/avatars}")
    private String avatarUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(avatarUploadDir).toAbsolutePath().normalize();
        String uploadPathStr = uploadPath.toUri().toString();

        // Khi FE dùng src="/uploads/avatars/xxx.jpg" thì Spring sẽ lấy file từ thư mục
        // upload
        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations(uploadPathStr);
    }
}
