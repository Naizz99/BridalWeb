package com.kdm360.bridalweb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {

    @Value("${file.image.location}")
    private String imageLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/resources/**",
                "/settings/**",
                "/gallery/**",
                "/blog/**",
                "/vendor/**",
                "/wedding/**",
                "/events/**",
        		"/partners/**")
            .addResourceLocations(
            	"classpath:/resources/",
            	"file:" + imageLocation + "settings/",
            	"file:" + imageLocation + "gallery/",
//                "classpath:/resources/static/video/",
                "file:" + imageLocation + "blog/",
                "file:" + imageLocation + "vendor/",
                "file:" + imageLocation + "wedding/",
                "file:" + imageLocation + "events/",
                "file:" + imageLocation + "partners/");
    }

    @Bean
    MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
