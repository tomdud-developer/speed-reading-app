package com.speedreadingapp.exception;

public class AuthoritiesNotMatchWithTokenClaimsException extends RuntimeException{
    public AuthoritiesNotMatchWithTokenClaimsException() {
        super("Authorities not match with token claims");
    }
}
