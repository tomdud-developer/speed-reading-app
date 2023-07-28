package com.speedreadingapp.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JWTConfigurationProperties {
    private String secretKey;
    private String publicKey;
    private int expirationInMinutes;
}