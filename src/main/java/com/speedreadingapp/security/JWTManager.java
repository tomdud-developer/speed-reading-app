package com.speedreadingapp.security;

public interface JWTManager {
    boolean validate(String token);
    boolean generate();
}
