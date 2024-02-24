package com.ipmcpmjournal.journal.ipmcpm.configuration.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.Arrays;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8080/")
                .allowedOrigins(" https://github.com/")
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedMethods(
                        String.valueOf(
                                Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                        )
                )
                .allowedHeaders(
                        String.valueOf(
                                Arrays.asList("Origin", "Content-Type", "Accept", "Authorisation")
                        )
                );
    }


}
