package com.speedreadingapp.security;

public class JWTTokenService implements JWTManager {

    //private final

    @Override
    public boolean validate(String token) {
        return false;
    }

    @Override
    public boolean generate() {
        return false;
    }


}
