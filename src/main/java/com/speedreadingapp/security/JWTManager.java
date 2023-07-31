package com.speedreadingapp.security;

import org.springframework.security.core.Authentication;

public interface JWTManager {
    void validate(String token);
    String generateAccessToken(Authentication authentication, String issuerUrl);
    String generateRefreshToken(Authentication authentication, String issuerUrl);

}
