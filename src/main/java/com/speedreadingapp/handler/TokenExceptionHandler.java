package com.speedreadingapp.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.speedreadingapp.dto.ApiResponse;
import com.speedreadingapp.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler({JWTVerificationException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleUserAlreadyRegisteredException(Exception exception) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("UNAUTHORIZED");
        apiResponse.setErrors(Collections.singletonList(new ErrorDTO("token", exception.getMessage())));

        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }
}
