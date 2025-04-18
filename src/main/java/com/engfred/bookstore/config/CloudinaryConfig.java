package com.engfred.bookstore.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-url}")
    private String cloudinaryURL;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(this.cloudinaryURL);
    }
}